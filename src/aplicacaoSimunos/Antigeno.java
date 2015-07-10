package aplicacaoSimunos;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class Antigeno {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean moved;
	private int energia;

	public Antigeno(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;// variaveis grid e space
		this.grid = grid;
		energia = 2;
	}

	@ScheduledMethod(start = 1, interval = 10)
	public void step() {
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<Neutrofilo> nghCreator = new GridCellNgh<Neutrofilo>(grid,
				pt, Neutrofilo.class, 1, 1);
		List<GridCell<Neutrofilo>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint pointWithMostNeutrofilo = null;
		int maxCount = -1;
		for (GridCell<Neutrofilo> cell : gridCells) {
			if (cell.size() > maxCount) {
				pointWithMostNeutrofilo = cell.getPoint();
				maxCount = cell.size();
			}
		}
		moveTowards(pointWithMostNeutrofilo);
		infect();// Chamada do metodo moveTowards.
	}

	public void moveTowards(GridPoint pt) {
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint,
					otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
			moved = true;
		}
	}

	public void infect() {
		GridPoint pt = grid.getLocation(this);
		List<Object> neutrofilo = new ArrayList<Object>();
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof Neutrofilo) {
				neutrofilo.add(obj);
			}
		}
		if (neutrofilo.size() > 0) {
			int index = RandomHelper.nextIntFromTo(0, neutrofilo.size() - 1);
			Neutrofilo obj = (Neutrofilo) neutrofilo.get(index);
			NdPoint spacePt = space.getLocation(obj);
			Context<Object> context = ContextUtils.getContext(obj);
			obj.setEnergia();
			if (obj.getEnergia() == 0) {
				context.remove(obj);
				Antigeno novoAntigeno = new Antigeno(space, grid);
				context.add(novoAntigeno);
				space.moveTo(novoAntigeno, spacePt.getX(), spacePt.getY());
				grid.moveTo(novoAntigeno, pt.getX(), pt.getY());
				Network<Object> net = (Network<Object>) context
						.getProjection("infection network");
				net.addEdge(this, novoAntigeno);

			}
		}
	}

	public int getEnergia() {
		return energia;
	}

	public void setEnergia() {
		this.energia--;
	}
}

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
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class Neutrofilo {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean moved;
	private int energia;

	private static int remainingAmount;
	
	public Neutrofilo(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;// variaveis grid e space
		this.grid = grid;
		energia= 3;
	}
	
	public static void setRemainingAmount(int remainingAmount) {
		Neutrofilo.remainingAmount = remainingAmount;
	}
	
	public static int getRemainingAmount() {
		return remainingAmount;
	}
	
	@ScheduledMethod(start = 1, interval = 10)
	public void step() {
		GridPoint pt = grid.getLocation(this);
		
		GridCellNgh<Antigeno> nghCreator = new GridCellNgh<Antigeno>(grid, pt,
				Antigeno.class, 1, 1);
		List<GridCell<Antigeno>> gridCells = nghCreator.getNeighborhood(true);
		
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint pointWithMostAntigeno = null;
		int maxCount = -1;
		for (GridCell<Antigeno> cell : gridCells) {
			if (cell.size() > maxCount) {
				pointWithMostAntigeno = cell.getPoint();
				maxCount = cell.size();
			}
		}
		moveTowards(pointWithMostAntigeno);
		atacar();
	}

	private void atacar() {
		GridPoint pt = grid.getLocation(this);
		List<Object> antigeno = new ArrayList<Object>();
		
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof Antigeno) {
				antigeno.add(obj);
			}
		}
		
		if (antigeno.size() > 0) {
			int index = RandomHelper.nextIntFromTo(0, antigeno.size() - 1);
			Antigeno antigenoVictim = (Antigeno) antigeno.get(index);
			
			NdPoint spacePt = space.getLocation(antigenoVictim);
			
			antigenoVictim.setEnergia(antigenoVictim.getEnergia() - 1);
		
			if(antigenoVictim.getEnergia()==0){
				Context<Object> context = ContextUtils.getContext(antigenoVictim);
				context.remove(antigenoVictim);
				
				Antigeno.setRemainingAmount(Antigeno.getRemainingAmount() - 1);
				
				energia=3;
			}
		}
	}

	private void moveTowards(GridPoint pt) {
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

	public int getEnergia() {
		return energia;
	}

	public void setEnergia(int energia) {
		this.energia = energia;
	}
}

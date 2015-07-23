package aplicacaoSimunos;

import javax.swing.JPanel;

import repast.simphony.userpanel.ui.UserPanelCreator;

public class InPanel extends Thread implements UserPanelCreator{

	public JPanel createPanel() {
		JPanel j = new JPanel();

		quantAntijLabel = new javax.swing.JLabel();
		quantNeutjLabel = new javax.swing.JLabel();
		antigenoQuant = new javax.swing.JLabel();
		neutrofiloQuant = new javax.swing.JLabel();

		quantAntijLabel.setText("Quantidade de Antigenos:");
		quantNeutjLabel.setText("Quantidade de Neutrofilos:");
		this.start();

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(j);
		j.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(23, 23, 23)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(quantAntijLabel)
												.addComponent(quantNeutjLabel))
								.addGap(27, 27, 27)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														antigenoQuant,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														neutrofiloQuant,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addContainerGap(67, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(34, 34, 34)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(quantAntijLabel)
												.addComponent(antigenoQuant))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(quantNeutjLabel)
												.addComponent(
														neutrofiloQuant,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														14,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap(40, Short.MAX_VALUE)));
		return j;
	}// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JLabel quantAntijLabel;
	private javax.swing.JLabel quantNeutjLabel;
	private javax.swing.JLabel neutrofiloQuant;
	private javax.swing.JLabel antigenoQuant;
	@Override
	public void run() {
		while(true){
			neutrofiloQuant.setText(String.format("%d", Neutrofilo.getRemainingAmount()));
			antigenoQuant.setText(String.format("%d", Antigeno.getRemainingAmount()));
		}
	}
}

package biosim.client.simulation.power.gui;

import java.awt.BorderLayout;

import biosim.client.framework.gui.UpdatablePanel;

/**
 * This is the JPanel that displays a chart about the Power
 *
 * @author    Scott Bell
 */
public class PowerChartPanel extends UpdatablePanel
{
	private PowerStorePanel myPowerStorePanel;

	public PowerChartPanel() {
		setLayout(new BorderLayout());
		myPowerStorePanel = new PowerStorePanel();
		add(myPowerStorePanel, BorderLayout.CENTER);
	}
	
	public void visibilityChange(boolean nowVisible){
		myPowerStorePanel.visibilityChange(nowVisible);
	}
	
	public void refresh(){
		myPowerStorePanel.refresh();
	}
}

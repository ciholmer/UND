package com.traclabs.biosim.client.simulation.air.cdrs;


public class LSSMPanel extends GridButtonPanel {
	public LSSMPanel(){
		setName("LSSM");
		addButton(new WaterRackPanel());
		addButton(new ARRackPanel());
		addButton(new TCSRackPanel());
		addButton(new RpcmM1Panel());
		addButton(new RpcmMAPanel());
		addButton(new RpcmM6Panel());
	}
	
}

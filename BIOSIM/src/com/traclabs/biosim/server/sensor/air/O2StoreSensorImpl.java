package biosim.server.sensor.air;

import biosim.idl.framework.BioModule;
import biosim.idl.sensor.air.O2StoreSensorOperations;
import biosim.idl.simulation.air.O2Store;
import biosim.server.sensor.framework.GenericSensorImpl;

public abstract class O2StoreSensorImpl extends GenericSensorImpl implements O2StoreSensorOperations{
	private O2Store myO2Store;
	
	public O2StoreSensorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected abstract void gatherData();
	protected abstract void notifyListeners();

	public void setInput(O2Store source){
		myO2Store = source;
	}
	
	public O2Store getInput(){
		return myO2Store;
	}
	
	public float getMax(){
		return myO2Store.getCapacity();
	}
	
	public BioModule getInputModule(){
		return (BioModule)(getInput());
	}
}

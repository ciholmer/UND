package biosim.server.sensor.air;

import biosim.idl.framework.BioModule;
import biosim.idl.sensor.air.H2StoreSensorOperations;
import biosim.idl.simulation.air.H2Store;
import biosim.server.sensor.framework.GenericSensorImpl;

public abstract class H2StoreSensorImpl extends GenericSensorImpl implements H2StoreSensorOperations{
	private H2Store myH2Store;
	
	public H2StoreSensorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected abstract void gatherData();
	protected abstract void notifyListeners();

	public void setInput(H2Store source){
		myH2Store = source;
	}
	
	public H2Store getInput(){
		return myH2Store;
	}
	
	public float getMax(){
		return myH2Store.getCapacity();
	}
	
	public BioModule getInputModule(){
		return (BioModule)(getInput());
	}
}

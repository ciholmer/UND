package biosim.server.sensor.environment;

import biosim.server.sensor.framework.*;
import biosim.idl.sensor.environment.*;
import biosim.idl.simulation.environment.*;

public class O2AirStoreOutFlowRateSensorImpl extends GenericSensorImpl implements O2AirStoreOutFlowRateSensorOperations{
	private O2AirProducer myProducer;
	private int myIndex;
	
	public O2AirStoreOutFlowRateSensorImpl(int pID){
		super(pID);
	}

	protected void gatherData(){
		double preFilteredValue = getInput().getO2AirStoreOutputActualFlowRate(myIndex);
		myValue = randomFilter(preFilteredValue);
	}
	
	protected void notifyListeners(){
		//does nothing right now
	}

	public void setInput(O2AirProducer pProducer, int pIndex){
		myProducer = pProducer;
		myIndex = pIndex;
	}
	
	public O2AirProducer getInput(){
		return myProducer;
	}
	
	public int getIndex(){
		return myIndex;
	}
}

package biosim.server.actuator.environment;

import biosim.idl.actuator.environment.WaterAirStoreInFlowRateActuatorOperations;
import biosim.idl.framework.BioModule;
import biosim.idl.framework.WaterAirConsumer;
import biosim.server.actuator.framework.GenericActuatorImpl;

public class WaterAirStoreInFlowRateActuatorImpl extends GenericActuatorImpl implements WaterAirStoreInFlowRateActuatorOperations{
	private WaterAirConsumer myConsumer;
	private int myIndex;
	
	public WaterAirStoreInFlowRateActuatorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected void processData(){
		float myFilteredValue = randomFilter(myValue);
		getOutput().setWaterAirStoreInputDesiredFlowRate(myFilteredValue, myIndex);
	}
	
	protected void notifyListeners(){
		//does nothing right now
	}

	public void setOutput(WaterAirConsumer pConsumer, int pIndex){
		myConsumer = pConsumer;
		myIndex = pIndex;
	}
	
	public float getMax(){
		return myConsumer.getWaterAirStoreInputMaxFlowRate(myIndex);
	}
	
	public BioModule getOutputModule(){
		return (BioModule)(myConsumer);
	}
	
	public WaterAirConsumer getOutput(){
		return myConsumer;
	}
	
	public int getIndex(){
		return myIndex;
	}
}

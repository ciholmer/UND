package biosim.server.actuator.environment;

import biosim.idl.actuator.environment.O2AirEnvironmentInFlowRateActuatorOperations;
import biosim.idl.framework.BioModule;
import biosim.idl.framework.O2AirConsumer;
import biosim.server.actuator.framework.GenericActuatorImpl;

public class O2AirEnvironmentInFlowRateActuatorImpl extends GenericActuatorImpl implements O2AirEnvironmentInFlowRateActuatorOperations{
	private O2AirConsumer myConsumer;
	private int myIndex;
	
	public O2AirEnvironmentInFlowRateActuatorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected void processData(){
		float myFilteredValue = randomFilter(myValue);
		getOutput().setO2AirEnvironmentInputDesiredFlowRate(myFilteredValue, myIndex);
	}
	
	protected void notifyListeners(){
		//does nothing right now
	}

	public void setOutput(O2AirConsumer pConsumer, int pIndex){
		myConsumer = pConsumer;
		myIndex = pIndex;
	}
	
	public float getMax(){
		return myConsumer.getO2AirEnvironmentInputMaxFlowRate(myIndex);
	}
	
	public BioModule getOutputModule(){
		return (BioModule)(myConsumer);
	}
	
	public O2AirConsumer getOutput(){
		return myConsumer;
	}
	
	public int getIndex(){
		return myIndex;
	}
}

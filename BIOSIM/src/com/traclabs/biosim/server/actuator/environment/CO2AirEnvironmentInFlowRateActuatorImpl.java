package biosim.server.actuator.environment;

import biosim.idl.actuator.environment.CO2AirEnvironmentInFlowRateActuatorOperations;
import biosim.idl.framework.BioModule;
import biosim.idl.framework.CO2AirConsumer;
import biosim.server.actuator.framework.GenericActuatorImpl;

public class CO2AirEnvironmentInFlowRateActuatorImpl extends GenericActuatorImpl implements CO2AirEnvironmentInFlowRateActuatorOperations{
	private CO2AirConsumer myConsumer;
	private int myIndex;
	
	public CO2AirEnvironmentInFlowRateActuatorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected void processData(){
		float myFilteredValue = randomFilter(myValue);
		getOutput().setCO2AirEnvironmentInputDesiredFlowRate(myFilteredValue, myIndex);
	}
	
	protected void notifyListeners(){
		//does nothing right now
	}

	public void setOutput(CO2AirConsumer pConsumer, int pIndex){
		myConsumer = pConsumer;
		myIndex = pIndex;
	}
	
	public CO2AirConsumer getOutput(){
		return myConsumer;
	}
	
	public BioModule getOutputModule(){
		return (BioModule)(myConsumer);
	}
	
	public int getIndex(){
		return myIndex;
	}
	
	public float getMax(){
		return myConsumer.getCO2AirEnvironmentInputMaxFlowRate(myIndex);
	}
}

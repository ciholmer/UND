package biosim.server.actuator.environment;

import biosim.idl.actuator.environment.NitrogenAirStoreInFlowRateActuatorOperations;
import biosim.idl.framework.BioModule;
import biosim.idl.framework.NitrogenAirConsumer;
import biosim.server.actuator.framework.GenericActuatorImpl;

public class NitrogenAirStoreInFlowRateActuatorImpl extends GenericActuatorImpl implements NitrogenAirStoreInFlowRateActuatorOperations{
	private NitrogenAirConsumer myConsumer;
	private int myIndex;
	
	public NitrogenAirStoreInFlowRateActuatorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected void processData(){
		float myFilteredValue = randomFilter(myValue);
		getOutput().setNitrogenAirStoreInputDesiredFlowRate(myFilteredValue, myIndex);
	}
	
	protected void notifyListeners(){
		//does nothing right now
	}

	public void setOutput(NitrogenAirConsumer pConsumer, int pIndex){
		myConsumer = pConsumer;
		myIndex = pIndex;
	}
	
	public float getMax(){
		return myConsumer.getNitrogenAirStoreInputMaxFlowRate(myIndex);
	}
	
	public BioModule getOutputModule(){
		return (BioModule)(myConsumer);
	}
	
	public NitrogenAirConsumer getOutput(){
		return myConsumer;
	}
	
	public int getIndex(){
		return myIndex;
	}
}

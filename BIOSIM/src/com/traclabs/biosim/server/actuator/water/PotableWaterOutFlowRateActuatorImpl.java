package biosim.server.actuator.water;

import biosim.idl.actuator.water.PotableWaterOutFlowRateActuatorOperations;
import biosim.idl.framework.BioModule;
import biosim.idl.framework.PotableWaterProducer;
import biosim.server.actuator.framework.GenericActuatorImpl;

public class PotableWaterOutFlowRateActuatorImpl extends GenericActuatorImpl implements PotableWaterOutFlowRateActuatorOperations{
	private PotableWaterProducer myProducer;
	private int myIndex;
	
	public PotableWaterOutFlowRateActuatorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected void processData(){
		float myFilteredValue = randomFilter(myValue);
		getOutput().setPotableWaterOutputDesiredFlowRate(myFilteredValue, myIndex);
	}
	
	protected void notifyListeners(){
		//does nothing right now
	}

	public void setOutput(PotableWaterProducer pProducer, int pIndex){
		myProducer = pProducer;
		myIndex = pIndex;
	}
	
	public BioModule getOutputModule(){
		return (BioModule)(myProducer);
	}
	
	public PotableWaterProducer getOutput(){
		return myProducer;
	}
	
	public int getIndex(){
		return myIndex;
	}
	
	public float getMax(){
		return myProducer.getPotableWaterOutputMaxFlowRate(myIndex);
	}
}

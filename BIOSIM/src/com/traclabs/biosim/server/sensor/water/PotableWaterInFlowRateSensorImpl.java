package biosim.server.sensor.water;

import biosim.idl.framework.BioModule;
import biosim.idl.framework.PotableWaterConsumer;
import biosim.idl.sensor.water.PotableWaterInFlowRateSensorOperations;
import biosim.server.sensor.framework.GenericSensorImpl;

public class PotableWaterInFlowRateSensorImpl extends GenericSensorImpl implements PotableWaterInFlowRateSensorOperations{
	private PotableWaterConsumer myConsumer;
	private int myIndex;
	
	public PotableWaterInFlowRateSensorImpl(int pID, String pName){
		super(pID, pName);
	}

	protected void gatherData(){
		float preFilteredValue = getInput().getPotableWaterInputActualFlowRate(myIndex);
		myValue = randomFilter(preFilteredValue);
	}
	
	protected void notifyListeners(){
		//does nothing right now
	}

	public void setInput(PotableWaterConsumer pConsumer, int pIndex){
		myConsumer = pConsumer;
		myIndex = pIndex;
	}
	
	public PotableWaterConsumer getInput(){
		return myConsumer;
	}
	
	public int getIndex(){
		return myIndex;
	}
	
	public BioModule getInputModule(){
		return (BioModule)(myConsumer);
	}
	
	public float getMax(){
		return myConsumer.getPotableWaterInputMaxFlowRate(myIndex);
	}
}

package biosim.server.sensor.environment;

import biosim.server.sensor.framework.*;
import biosim.idl.sensor.environment.*;
import biosim.idl.simulation.environment.*;

public class OtherAirMolesSensorImpl extends EnvironmentSensorImpl implements OtherAirMolesSensorOperations{
	public OtherAirMolesSensorImpl(int pID){
		super(pID);
	}

	protected void gatherData(){
		float preFilteredValue = getInput().getOtherMoles();
		myValue = randomFilter(preFilteredValue);
	}
	
	protected void notifyListeners(){
	}
	
	/**
	* Returns the name of this module (OtherAirMolesSensor)
	* @return the name of the module
	*/
	public String getModuleName(){
		return "OtherAirMolesSensor"+getID();
	}
}

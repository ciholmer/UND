package com.traclabs.biosim.idl.sensor.environment;

/**
 *	Generated from IDL interface "GasMoleSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */


public interface GasMoleSensorOperations
	extends com.traclabs.biosim.idl.sensor.framework.GenericSensorOperations
{
	/* constants */
	/* operations  */
	void setInput(com.traclabs.biosim.idl.simulation.environment.SimEnvironment environment, com.traclabs.biosim.idl.simulation.environment.EnvironmentStore gas);
	com.traclabs.biosim.idl.simulation.environment.SimEnvironment getEnvironment();
	com.traclabs.biosim.idl.simulation.environment.EnvironmentStore getGas();
}

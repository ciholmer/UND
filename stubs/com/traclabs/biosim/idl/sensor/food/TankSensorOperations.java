package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "TankSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */


public interface TankSensorOperations
	extends com.traclabs.biosim.idl.sensor.framework.GenericSensorOperations
{
	/* constants */
	/* operations  */
	void setInput(com.traclabs.biosim.idl.simulation.food.AquaculturePS source, int index);
	com.traclabs.biosim.idl.simulation.food.Tank getInput();
}

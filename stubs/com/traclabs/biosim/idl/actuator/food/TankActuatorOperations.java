package com.traclabs.biosim.idl.actuator.food;

/**
 *	Generated from IDL interface "TankActuator"
 *	@author CHolmer
 */


public interface TankActuatorOperations
	extends com.traclabs.biosim.idl.actuator.framework.GenericActuatorOperations
{
	/* constants */
	/* operations  */
	void setOutput(com.traclabs.biosim.idl.simulation.food.AquaculturePS source, int index);
	com.traclabs.biosim.idl.simulation.food.Tank getOutput();
}

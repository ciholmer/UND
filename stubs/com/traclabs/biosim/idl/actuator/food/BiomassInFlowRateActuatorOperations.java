package com.traclabs.biosim.idl.actuator.food;

/**
 *	Generated from IDL interface "BiomassInFlowRateActuator"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */


public interface BiomassInFlowRateActuatorOperations
	extends com.traclabs.biosim.idl.actuator.framework.GenericActuatorOperations
{
	/* constants */
	/* operations  */
	void setOutput(com.traclabs.biosim.idl.simulation.food.BiomassConsumer pConsumer, int pIndex);
	com.traclabs.biosim.idl.simulation.food.BiomassConsumer getOutput();
	int getIndex();
}

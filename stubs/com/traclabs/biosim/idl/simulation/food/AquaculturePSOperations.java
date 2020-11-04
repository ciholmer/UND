package com.traclabs.biosim.idl.simulation.food;

/**
 *	Generated from IDL interface "AquaculturePS"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */


public interface AquaculturePSOperations
	extends com.traclabs.biosim.idl.simulation.framework.SimBioModuleOperations , com.traclabs.biosim.idl.simulation.power.PowerConsumerOperations , com.traclabs.biosim.idl.simulation.water.PotableWaterConsumerOperations , com.traclabs.biosim.idl.simulation.water.GreyWaterConsumerOperations , com.traclabs.biosim.idl.simulation.food.BiomassProducerOperations , com.traclabs.biosim.idl.simulation.environment.AirConsumerOperations , com.traclabs.biosim.idl.simulation.environment.AirProducerOperations , com.traclabs.biosim.idl.simulation.water.DirtyWaterProducerOperations
{
	/* constants */
	/* operations  */
	com.traclabs.biosim.idl.simulation.food.Tank[] getTanks();
	com.traclabs.biosim.idl.simulation.food.Tank getTank(int index);
	com.traclabs.biosim.idl.simulation.food.Tank createNewTank(com.traclabs.biosim.idl.simulation.food.FishType pType, float pCropArea, int pStartTick);
	void clearTanks();
	void setautoHarvestAndRespawnEnabled(boolean pHarvestEnabled);
	boolean autoHarvestAndRespawnEnabled();
	boolean isAnyFishDead();
	void killFish();
	void setDeathEnabled(boolean deathEnabled);
	boolean getDeathEnabled();
}

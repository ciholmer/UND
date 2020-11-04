package com.traclabs.biosim.idl.simulation.food;

/**
 *	Generated from IDL interface "Tank"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */


public interface TankOperations
{
	/* constants */
	/* operations  */
	void harvest();
	float getHarvestInterval();
	boolean isReadyForHavest();
	boolean isDead();
	com.traclabs.biosim.idl.simulation.food.FishType getFishType();
	java.lang.String getFishTypeString();
	float getTimeTillSchoolClosure();
	void respawn(com.traclabs.biosim.idl.simulation.food.FishType pType, float volume);
	float getTankVolTotal();
	float getTankVolUsed();  
	com.traclabs.biosim.idl.simulation.food.AquaculturePS getAquaculturePS();
	void setStartTick(int tick);
	void kill();
	com.traclabs.biosim.idl.simulation.food.Fish getFish();
}

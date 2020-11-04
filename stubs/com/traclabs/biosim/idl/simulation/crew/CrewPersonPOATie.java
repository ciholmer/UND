package com.traclabs.biosim.idl.simulation.crew;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "CrewPerson"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public class CrewPersonPOATie
	extends CrewPersonPOA
{
	private CrewPersonOperations _delegate;

	private POA _poa;
	public CrewPersonPOATie(CrewPersonOperations delegate)
	{
		_delegate = delegate;
	}
	public CrewPersonPOATie(CrewPersonOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public com.traclabs.biosim.idl.simulation.crew.CrewPerson _this()
	{
		return com.traclabs.biosim.idl.simulation.crew.CrewPersonHelper.narrow(_this_object());
	}
	public com.traclabs.biosim.idl.simulation.crew.CrewPerson _this(org.omg.CORBA.ORB orb)
	{
		return com.traclabs.biosim.idl.simulation.crew.CrewPersonHelper.narrow(_this_object(orb));
	}
	public CrewPersonOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(CrewPersonOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public int getTimeActivityPerformed()
	{
		return _delegate.getTimeActivityPerformed();
	}

	public void setLogLevel(com.traclabs.biosim.idl.framework.LogLevel pLogLevel)
	{
_delegate.setLogLevel(pLogLevel);
	}

	public boolean isSick()
	{
		return _delegate.isSick();
	}

	public java.lang.String getName()
	{
		return _delegate.getName();
	}

	public int getDepartureTick()
	{
		return _delegate.getDepartureTick();
	}

	public float getO2Consumed()
	{
		return _delegate.getO2Consumed();
	}

	public float getWeight()
	{
		return _delegate.getWeight();
	}

	public boolean isSuffocating()
	{
		return _delegate.isSuffocating();
	}

	public float getAge()
	{
		return _delegate.getAge();
	}

	public void insertActivityInSchedule(com.traclabs.biosim.idl.simulation.crew.Activity newActivity, int order)
	{
_delegate.insertActivityInSchedule(newActivity,order);
	}

	public boolean isStarving()
	{
		return _delegate.isStarving();
	}

	public com.traclabs.biosim.idl.simulation.crew.Activity getActivityByName(java.lang.String name)
	{
		return _delegate.getActivityByName(name);
	}

	public float getGreyWaterProduced()
	{
		return _delegate.getGreyWaterProduced();
	}

	public float getFoodConsumed()
	{
		return _delegate.getFoodConsumed();
	}

	public int getArrivalTick()
	{
		return _delegate.getArrivalTick();
	}

	public com.traclabs.biosim.idl.simulation.crew.Activity getScheduledActivityByOrder(int order)
	{
		return _delegate.getScheduledActivityByOrder(order);
	}

	public void sicken()
	{
_delegate.sicken();
	}

	public void reset()
	{
_delegate.reset();
	}

	public boolean isOnBoard()
	{
		return _delegate.isOnBoard();
	}

	public void kill()
	{
_delegate.kill();
	}

	public boolean isDead()
	{
		return _delegate.isDead();
	}

	public float getPotableWaterConsumed()
	{
		return _delegate.getPotableWaterConsumed();
	}

	public com.traclabs.biosim.idl.simulation.crew.CrewGroup getCurrentCrewGroup()
	{
		return _delegate.getCurrentCrewGroup();
	}

	public com.traclabs.biosim.idl.simulation.crew.Sex getSex()
	{
		return _delegate.getSex();
	}

	public int getOrderOfScheduledActivity(java.lang.String name)
	{
		return _delegate.getOrderOfScheduledActivity(name);
	}

	public boolean isThirsty()
	{
		return _delegate.isThirsty();
	}

	public void tick()
	{
_delegate.tick();
	}

	public float getDirtyWaterProduced()
	{
		return _delegate.getDirtyWaterProduced();
	}

	public void insertActivityInScheduleNow(com.traclabs.biosim.idl.simulation.crew.Activity newActivity)
	{
_delegate.insertActivityInScheduleNow(newActivity);
	}

	public boolean isPoisoned()
	{
		return _delegate.isPoisoned();
	}

	public void setArrivalTick(int arrivalTick)
	{
_delegate.setArrivalTick(arrivalTick);
	}

	public void setDepartureTick(int departureTick)
	{
_delegate.setDepartureTick(departureTick);
	}

	public void setCurrentActivity(com.traclabs.biosim.idl.simulation.crew.Activity newActivity)
	{
_delegate.setCurrentActivity(newActivity);
	}

	public com.traclabs.biosim.idl.simulation.crew.Activity getCurrentActivity()
	{
		return _delegate.getCurrentActivity();
	}

	public float getCO2Produced()
	{
		return _delegate.getCO2Produced();
	}
	
	public float getDryWasteProduced()
	{
		return _delegate.getDryWasteProduced();
	}

}

package com.traclabs.biosim.idl.simulation.food;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "Tank"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public class TankPOATie
	extends TankPOA
{
	private TankOperations _delegate;

	private POA _poa;
	public TankPOATie(TankOperations delegate)
	{
		_delegate = delegate;
	}
	public TankPOATie(TankOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public com.traclabs.biosim.idl.simulation.food.Tank _this()
	{
		return com.traclabs.biosim.idl.simulation.food.TankHelper.narrow(_this_object());
	}
	public com.traclabs.biosim.idl.simulation.food.Tank _this(org.omg.CORBA.ORB orb)
	{
		return com.traclabs.biosim.idl.simulation.food.TankHelper.narrow(_this_object(orb));
	}
	public TankOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(TankOperations delegate)
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
	public void setStartTick(int tick)
	{
_delegate.setStartTick(tick);
	}

	public com.traclabs.biosim.idl.simulation.food.Fish getFish()
	{
		return _delegate.getFish();
	}

	public void respawn(com.traclabs.biosim.idl.simulation.food.FishType pType, float area)
	{
_delegate.respawn(pType,area);
	}

	public void harvest()
	{
_delegate.harvest();
	}

	public boolean isDead()
	{
		return _delegate.isDead();
	}

	public void kill()
	{
_delegate.kill();
	}

	public com.traclabs.biosim.idl.simulation.food.AquaculturePS getAquaculturePS()
	{
		return _delegate.getAquaculturePS();
	}

	public float getHarvestInterval()
	{
		return _delegate.getHarvestInterval();
	}

	public java.lang.String getFishTypeString()
	{
		return _delegate.getFishTypeString();
	}

	public com.traclabs.biosim.idl.simulation.food.FishType getFishType()
	{
		return _delegate.getFishType();
	}

	public boolean isReadyForHavest()
	{
		return _delegate.isReadyForHavest();
	}

	public float getTankVolUsed()
	{
		return _delegate.getTankVolUsed();
	}

	public float getTimeTillSchoolClosure()
	{
		return _delegate.getTimeTillSchoolClosure();
	}

	public float getTankVolTotal()
	{
		return _delegate.getTankVolTotal();
	}

}

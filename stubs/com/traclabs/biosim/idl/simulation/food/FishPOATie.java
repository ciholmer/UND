package com.traclabs.biosim.idl.simulation.food;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "Fish"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public class FishPOATie
	extends FishPOA
{
	private FishOperations _delegate;

	private POA _poa;
	public FishPOATie(FishOperations delegate)
	{
		_delegate = delegate;
	}
	public FishPOATie(FishOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public com.traclabs.biosim.idl.simulation.food.Fish _this()
	{
		return com.traclabs.biosim.idl.simulation.food.FishHelper.narrow(_this_object());
	}
	public com.traclabs.biosim.idl.simulation.food.Fish _this(org.omg.CORBA.ORB orb)
	{
		return com.traclabs.biosim.idl.simulation.food.FishHelper.narrow(_this_object(orb));
	}
	public FishOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(FishOperations delegate)
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
	public float getMolesOfO2Inhaled()
	{
		return _delegate.getMolesOfO2Inhaled();
	}

}

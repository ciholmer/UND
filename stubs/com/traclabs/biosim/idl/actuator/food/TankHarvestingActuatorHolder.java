package com.traclabs.biosim.idl.actuator.food;

/**
 *	Generated from IDL interface "HarvestingActuator"
 *	@author CHolmer
 */

public final class TankHarvestingActuatorHolder	implements org.omg.CORBA.portable.Streamable{
	 public TankHarvestingActuator value;
	public TankHarvestingActuatorHolder()
	{
	}
	public TankHarvestingActuatorHolder (final TankHarvestingActuator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return TankHarvestingActuatorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TankHarvestingActuatorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		TankHarvestingActuatorHelper.write (_out,value);
	}
}

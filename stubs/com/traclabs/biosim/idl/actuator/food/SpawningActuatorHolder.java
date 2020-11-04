package com.traclabs.biosim.idl.actuator.food;

/**
 *	Generated from IDL interface "SpawningActuator"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class SpawningActuatorHolder	implements org.omg.CORBA.portable.Streamable{
	 public SpawningActuator value;
	public SpawningActuatorHolder()
	{
	}
	public SpawningActuatorHolder (final SpawningActuator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return SpawningActuatorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SpawningActuatorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		SpawningActuatorHelper.write (_out,value);
	}
}

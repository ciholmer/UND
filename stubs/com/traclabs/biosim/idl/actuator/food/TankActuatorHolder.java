package com.traclabs.biosim.idl.actuator.food;

/**
 *	Generated from IDL interface "TankActuator"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class TankActuatorHolder	implements org.omg.CORBA.portable.Streamable{
	 public TankActuator value;
	public TankActuatorHolder()
	{
	}
	public TankActuatorHolder (final TankActuator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return TankActuatorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TankActuatorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		TankActuatorHelper.write (_out,value);
	}
}

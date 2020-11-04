package com.traclabs.biosim.idl.actuator.food;

/**
 *	Generated from IDL interface "AquaculturePSActuator"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class AquaculturePSActuatorHolder	implements org.omg.CORBA.portable.Streamable{
	 public AquaculturePSActuator value;
	public AquaculturePSActuatorHolder()
	{
	}
	public AquaculturePSActuatorHolder (final AquaculturePSActuator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return AquaculturePSActuatorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AquaculturePSActuatorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		AquaculturePSActuatorHelper.write (_out,value);
	}
}

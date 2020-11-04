package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "TankSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class TankSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public TankSensor value;
	public TankSensorHolder()
	{
	}
	public TankSensorHolder (final TankSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return TankSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TankSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		TankSensorHelper.write (_out,value);
	}
}

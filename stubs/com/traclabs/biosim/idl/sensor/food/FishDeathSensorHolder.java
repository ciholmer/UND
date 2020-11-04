package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "FishDeathSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class FishDeathSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public FishDeathSensor value;
	public FishDeathSensorHolder()
	{
	}
	public FishDeathSensorHolder (final FishDeathSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return FishDeathSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = FishDeathSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		FishDeathSensorHelper.write (_out,value);
	}
}

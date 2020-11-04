package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "FishHarvestSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class FishHarvestSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public FishHarvestSensor value;
	public FishHarvestSensorHolder()
	{
	}
	public FishHarvestSensorHolder (final FishHarvestSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return FishHarvestSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = FishHarvestSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		FishHarvestSensorHelper.write (_out,value);
	}
}

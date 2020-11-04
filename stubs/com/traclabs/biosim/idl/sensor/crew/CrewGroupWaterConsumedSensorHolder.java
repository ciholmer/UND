package com.traclabs.biosim.idl.sensor.crew;

/**
 *	Generated from IDL interface "CrewGroupWaterConsumedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class CrewGroupWaterConsumedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public CrewGroupWaterConsumedSensor value;
	public CrewGroupWaterConsumedSensorHolder()
	{
	}
	public CrewGroupWaterConsumedSensorHolder (final CrewGroupWaterConsumedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return CrewGroupWaterConsumedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = CrewGroupWaterConsumedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		CrewGroupWaterConsumedSensorHelper.write (_out,value);
	}
}

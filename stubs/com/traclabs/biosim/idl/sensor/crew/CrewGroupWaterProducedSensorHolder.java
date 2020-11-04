package com.traclabs.biosim.idl.sensor.crew;

/**
 *	Generated from IDL interface "CrewGroupWaterProducedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class CrewGroupWaterProducedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public CrewGroupWaterProducedSensor value;
	public CrewGroupWaterProducedSensorHolder()
	{
	}
	public CrewGroupWaterProducedSensorHolder (final CrewGroupWaterProducedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return CrewGroupWaterProducedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = CrewGroupWaterProducedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		CrewGroupWaterProducedSensorHelper.write (_out,value);
	}
}

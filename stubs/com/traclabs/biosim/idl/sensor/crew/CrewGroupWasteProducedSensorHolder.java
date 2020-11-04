package com.traclabs.biosim.idl.sensor.crew;

/**
 *	Generated from IDL interface "CrewGroupWasteProducedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class CrewGroupWasteProducedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public CrewGroupWasteProducedSensor value;
	public CrewGroupWasteProducedSensorHolder()
	{
	}
	public CrewGroupWasteProducedSensorHolder (final CrewGroupWasteProducedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return CrewGroupWasteProducedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = CrewGroupWasteProducedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		CrewGroupWasteProducedSensorHelper.write (_out,value);
	}
}

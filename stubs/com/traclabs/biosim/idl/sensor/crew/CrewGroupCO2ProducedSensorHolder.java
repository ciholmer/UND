package com.traclabs.biosim.idl.sensor.crew;

/**
 *	Generated from IDL interface "CrewGroupCO2ProducedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class CrewGroupCO2ProducedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public CrewGroupCO2ProducedSensor value;
	public CrewGroupCO2ProducedSensorHolder()
	{
	}
	public CrewGroupCO2ProducedSensorHolder (final CrewGroupCO2ProducedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return CrewGroupCO2ProducedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = CrewGroupCO2ProducedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		CrewGroupCO2ProducedSensorHelper.write (_out,value);
	}
}

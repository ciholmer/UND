package com.traclabs.biosim.idl.sensor.crew;

/**
 *	Generated from IDL interface "CrewGroupO2ConsumedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class CrewGroupO2ConsumedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public CrewGroupO2ConsumedSensor value;
	public CrewGroupO2ConsumedSensorHolder()
	{
	}
	public CrewGroupO2ConsumedSensorHolder (final CrewGroupO2ConsumedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return CrewGroupO2ConsumedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = CrewGroupO2ConsumedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		CrewGroupO2ConsumedSensorHelper.write (_out,value);
	}
}

package com.traclabs.biosim.idl.sensor.crew;

/**
 *	Generated from IDL interface "CrewGroupFoodConsumedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class CrewGroupFoodConsumedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public CrewGroupFoodConsumedSensor value;
	public CrewGroupFoodConsumedSensorHolder()
	{
	}
	public CrewGroupFoodConsumedSensorHolder (final CrewGroupFoodConsumedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return CrewGroupFoodConsumedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = CrewGroupFoodConsumedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		CrewGroupFoodConsumedSensorHelper.write (_out,value);
	}
}

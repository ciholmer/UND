package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "TimeTillSchoolClosureSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class TimeTillSchoolClosureSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public TimeTillSchoolClosureSensor value;
	public TimeTillSchoolClosureSensorHolder()
	{
	}
	public TimeTillSchoolClosureSensorHolder (final TimeTillSchoolClosureSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return TimeTillSchoolClosureSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TimeTillSchoolClosureSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		TimeTillSchoolClosureSensorHelper.write (_out,value);
	}
}

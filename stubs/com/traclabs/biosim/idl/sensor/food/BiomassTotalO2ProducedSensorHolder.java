package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "BiomassTotalO2ProducedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class BiomassTotalO2ProducedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public BiomassTotalO2ProducedSensor value;
	public BiomassTotalO2ProducedSensorHolder()
	{
	}
	public BiomassTotalO2ProducedSensorHolder (final BiomassTotalO2ProducedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BiomassTotalO2ProducedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BiomassTotalO2ProducedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BiomassTotalO2ProducedSensorHelper.write (_out,value);
	}
}

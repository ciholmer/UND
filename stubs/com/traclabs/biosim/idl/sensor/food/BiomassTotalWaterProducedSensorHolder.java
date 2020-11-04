package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "BiomassTotalWaterProducedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class BiomassTotalWaterProducedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public BiomassTotalWaterProducedSensor value;
	public BiomassTotalWaterProducedSensorHolder()
	{
	}
	public BiomassTotalWaterProducedSensorHolder (final BiomassTotalWaterProducedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BiomassTotalWaterProducedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BiomassTotalWaterProducedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BiomassTotalWaterProducedSensorHelper.write (_out,value);
	}
}

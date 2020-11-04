package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "BiomassTotalWaterConsumedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class BiomassTotalWaterConsumedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public BiomassTotalWaterConsumedSensor value;
	public BiomassTotalWaterConsumedSensorHolder()
	{
	}
	public BiomassTotalWaterConsumedSensorHolder (final BiomassTotalWaterConsumedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BiomassTotalWaterConsumedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BiomassTotalWaterConsumedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BiomassTotalWaterConsumedSensorHelper.write (_out,value);
	}
}

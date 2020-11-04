package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "BiomassTotalCO2ConsumedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class BiomassTotalCO2ConsumedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public BiomassTotalCO2ConsumedSensor value;
	public BiomassTotalCO2ConsumedSensorHolder()
	{
	}
	public BiomassTotalCO2ConsumedSensorHolder (final BiomassTotalCO2ConsumedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BiomassTotalCO2ConsumedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BiomassTotalCO2ConsumedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BiomassTotalCO2ConsumedSensorHelper.write (_out,value);
	}
}

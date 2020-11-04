package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "BiomassTotalCO2ConsumedSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class BiomassShelfCO2ConsumedSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public BiomassShelfCO2ConsumedSensor value;
	public BiomassShelfCO2ConsumedSensorHolder()
	{
	}
	public BiomassShelfCO2ConsumedSensorHolder (final BiomassShelfCO2ConsumedSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BiomassShelfCO2ConsumedSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BiomassShelfCO2ConsumedSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BiomassShelfCO2ConsumedSensorHelper.write (_out,value);
	}
}

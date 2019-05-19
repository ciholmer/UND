package com.traclabs.biosim.idl.sensor.environment;

/**
 *	GasMoleSensorHolder
 *  Based on  *	Generated from IDL interface "GasConcentrationSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 *  Modified by CHolmer 19-May-2019
 */


public final class GasMoleSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public GasMoleSensor value;
	public GasMoleSensorHolder()
	{
	}
	public GasMoleSensorHolder (final GasMoleSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return GasMoleSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = GasMoleSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		GasMoleSensorHelper.write (_out,value);
	}
}

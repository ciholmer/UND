package com.traclabs.biosim.idl.sensor.environment;

/**
 *	Generated from IDL interface "GasMoleSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
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

package com.traclabs.biosim.idl.sensor.food;

/**
 *	Generated from IDL interface "BiomassSensor"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class BiomassSensorHolder	implements org.omg.CORBA.portable.Streamable{
	 public BiomassSensor value;
	public BiomassSensorHolder()
	{
	}
	public BiomassSensorHolder (final BiomassSensor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BiomassSensorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BiomassSensorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BiomassSensorHelper.write (_out,value);
	}
}

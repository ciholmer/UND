package com.traclabs.biosim.idl.simulation.food;

/**
 *	Generated from IDL interface "AquaculturePS"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class AquaculturePSHolder	implements org.omg.CORBA.portable.Streamable{
	 public AquaculturePS value;
	public AquaculturePSHolder()
	{
	}
	public AquaculturePSHolder (final AquaculturePS initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return AquaculturePSHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AquaculturePSHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		AquaculturePSHelper.write (_out,value);
	}
}

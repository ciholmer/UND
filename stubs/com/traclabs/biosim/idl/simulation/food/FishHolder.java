package com.traclabs.biosim.idl.simulation.food;

/**
 *	Generated from IDL interface "Fish"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class FishHolder	implements org.omg.CORBA.portable.Streamable{
	 public Fish value;
	public FishHolder()
	{
	}
	public FishHolder (final Fish initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return FishHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = FishHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		FishHelper.write (_out,value);
	}
}

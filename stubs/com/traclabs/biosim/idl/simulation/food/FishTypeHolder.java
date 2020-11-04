package com.traclabs.biosim.idl.simulation.food;
/**
 *	Generated from IDL definition of enum "FishType"
 *	@author JacORB IDL compiler 
 */

public final class FishTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public FishType value;

	public FishTypeHolder ()
	{
	}
	public FishTypeHolder (final FishType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return FishTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = FishTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		FishTypeHelper.write (out,value);
	}
}

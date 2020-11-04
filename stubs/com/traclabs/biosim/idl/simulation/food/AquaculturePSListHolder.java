package com.traclabs.biosim.idl.simulation.food;

/**
 *	Generated from IDL definition of alias "AquaculturePSList"
 *	@author JacORB IDL compiler 
 */

public final class AquaculturePSListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public com.traclabs.biosim.idl.simulation.food.AquaculturePS[] value;

	public AquaculturePSListHolder ()
	{
	}
	public AquaculturePSListHolder (final com.traclabs.biosim.idl.simulation.food.AquaculturePS[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return AquaculturePSListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AquaculturePSListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		AquaculturePSListHelper.write (out,value);
	}
}

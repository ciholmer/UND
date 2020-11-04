package com.traclabs.biosim.idl.simulation.food;

/**
 *	Generated from IDL definition of alias "TankList"
 *	@author JacORB IDL compiler 
 */

public final class TankListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public com.traclabs.biosim.idl.simulation.food.Tank[] value;

	public TankListHolder ()
	{
	}
	public TankListHolder (final com.traclabs.biosim.idl.simulation.food.Tank[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TankListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TankListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TankListHelper.write (out,value);
	}
}

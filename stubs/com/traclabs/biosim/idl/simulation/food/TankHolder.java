package com.traclabs.biosim.idl.simulation.food;

/**
 *	Generated from IDL interface "Tank"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public final class TankHolder	implements org.omg.CORBA.portable.Streamable{
	 public Tank value;
	public TankHolder()
	{
	}
	public TankHolder (final Tank initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return TankHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TankHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		TankHelper.write (_out,value);
	}
}

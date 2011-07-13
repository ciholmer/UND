package com.traclabs.biosim.idl.simulation.air.cdrs;
/**
 *	Generated from IDL definition of enum "CDRSCommandStatus"
 *	@author JacORB IDL compiler 
 */

public final class CDRSCommandStatus
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _inibited = 0;
	public static final CDRSCommandStatus inibited = new CDRSCommandStatus(_inibited);
	public static final int _enabled = 1;
	public static final CDRSCommandStatus enabled = new CDRSCommandStatus(_enabled);
	public int value()
	{
		return value;
	}
	public static CDRSCommandStatus from_int(int value)
	{
		switch (value) {
			case _inibited: return inibited;
			case _enabled: return enabled;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected CDRSCommandStatus(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}

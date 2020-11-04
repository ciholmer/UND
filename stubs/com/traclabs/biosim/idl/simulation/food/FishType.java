package com.traclabs.biosim.idl.simulation.food;
/**
 *	Generated from IDL definition of enum "FishType"
 *	@author JacORB IDL compiler 
 */

public final class FishType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _TILAPIA = 0;
	public static final FishType TILAPIA = new FishType(_TILAPIA);
	public static final int _SHRIMP = 1;
	public static final FishType SHRIMP = new FishType(_SHRIMP);
	public static final int _UNKNOWN_FISH = 9;
	public static final FishType UNKNOWN_FISH = new FishType(_UNKNOWN_FISH);
	public int value()
	{
		return value;
	}
	public static FishType from_int(int value)
	{
		switch (value) {
			case _TILAPIA: return TILAPIA;
			case _SHRIMP: return SHRIMP;
			case _UNKNOWN_FISH: return UNKNOWN_FISH;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected FishType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}

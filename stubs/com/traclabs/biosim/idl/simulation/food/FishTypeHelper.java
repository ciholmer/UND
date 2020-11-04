package com.traclabs.biosim.idl.simulation.food;
/**
 *	Generated from IDL definition of enum "FishType"
 *	@author JacORB IDL compiler 
 */

public final class FishTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(com.traclabs.biosim.idl.simulation.food.FishTypeHelper.id(),"FishType",new String[]{"TILAPIA","SHRIMP","UNKNOWN_FISH"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final com.traclabs.biosim.idl.simulation.food.FishType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static com.traclabs.biosim.idl.simulation.food.FishType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:com/traclabs/biosim/idl/simulation/food/FishType:1.0";
	}
	public static FishType read (final org.omg.CORBA.portable.InputStream in)
	{
		return FishType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final FishType s)
	{
		out.write_long(s.value());
	}
}

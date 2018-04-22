package com.traclabs.biosim.idl.simulation.air.cdrs;
/**
 *	Generated from IDL definition of enum "CDRSPowerState"
 *	@author JacORB IDL compiler 
 */

public final class CDRSPowerStateHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(com.traclabs.biosim.idl.simulation.air.cdrs.CDRSPowerStateHelper.id(),"CDRSPowerState",new String[]{"on","off"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final com.traclabs.biosim.idl.simulation.air.cdrs.CDRSPowerState s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static com.traclabs.biosim.idl.simulation.air.cdrs.CDRSPowerState extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:com/traclabs/biosim/idl/simulation/air/cdrs/CDRSPowerState:1.0";
	}
	public static CDRSPowerState read (final org.omg.CORBA.portable.InputStream in)
	{
		return CDRSPowerState.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final CDRSPowerState s)
	{
		out.write_long(s.value());
	}
}

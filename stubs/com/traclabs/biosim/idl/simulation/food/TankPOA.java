package com.traclabs.biosim.idl.simulation.food;

/**
 *	Generated from IDL interface "Tank"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */


public abstract class TankPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, com.traclabs.biosim.idl.simulation.food.TankOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setStartTick", new java.lang.Integer(0));
		m_opsHash.put ( "getFish", new java.lang.Integer(1));
		m_opsHash.put ( "respawn", new java.lang.Integer(2));
		m_opsHash.put ( "harvest", new java.lang.Integer(3));
		m_opsHash.put ( "isDead", new java.lang.Integer(4));
		m_opsHash.put ( "kill", new java.lang.Integer(5));
		m_opsHash.put ( "getAquaculturePS", new java.lang.Integer(6));
		m_opsHash.put ( "getHarvestInterval", new java.lang.Integer(7));
		m_opsHash.put ( "getFishTypeString", new java.lang.Integer(8));
		m_opsHash.put ( "getFishType", new java.lang.Integer(9));
		m_opsHash.put ( "isReadyForHavest", new java.lang.Integer(10));
		m_opsHash.put ( "getCropAreaUsed", new java.lang.Integer(11));
		m_opsHash.put ( "getTimeTillSchoolClosure", new java.lang.Integer(12));
		m_opsHash.put ( "getTankVolumeTotal", new java.lang.Integer(13));
	}
	private String[] ids = {"IDL:com/traclabs/biosim/idl/simulation/food/Tank:1.0"};
	public com.traclabs.biosim.idl.simulation.food.Tank _this()
	{
		return com.traclabs.biosim.idl.simulation.food.TankHelper.narrow(_this_object());
	}
	public com.traclabs.biosim.idl.simulation.food.Tank _this(org.omg.CORBA.ORB orb)
	{
		return com.traclabs.biosim.idl.simulation.food.TankHelper.narrow(_this_object(orb));
	}
	public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler)
		throws org.omg.CORBA.SystemException
	{
		org.omg.CORBA.portable.OutputStream _out = null;
		// do something
		// quick lookup of operation
		java.lang.Integer opsIndex = (java.lang.Integer)m_opsHash.get ( method );
		if ( null == opsIndex )
			throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
		switch ( opsIndex.intValue() )
		{
			case 0: // setStartTick
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				setStartTick(_arg0);
				break;
			}
			case 1: // getFish
			{
				_out = handler.createReply();
				com.traclabs.biosim.idl.simulation.food.FishHelper.write(_out,getFish());
				break;
			}
			case 2: // respawn
			{
				com.traclabs.biosim.idl.simulation.food.FishType _arg0=com.traclabs.biosim.idl.simulation.food.FishTypeHelper.read(_input);
				float _arg1=_input.read_float();
				_out = handler.createReply();
				respawn(_arg0,_arg1);
				break;
			}
			case 3: // harvest
			{
				_out = handler.createReply();
				harvest();
				break;
			}
			case 4: // isDead
			{
				_out = handler.createReply();
				_out.write_boolean(isDead());
				break;
			}
			case 5: // kill
			{
				_out = handler.createReply();
				kill();
				break;
			}
			case 6: // getAquaculturePS
			{
				_out = handler.createReply();
				com.traclabs.biosim.idl.simulation.food.AquaculturePSHelper.write(_out,getAquaculturePS());
				break;
			}
			case 7: // getHarvestInterval
			{
				_out = handler.createReply();
				_out.write_float(getHarvestInterval());
				break;
			}
			case 8: // getFishTypeString
			{
				_out = handler.createReply();
				_out.write_string(getFishTypeString());
				break;
			}
			case 9: // getFishType
			{
				_out = handler.createReply();
				com.traclabs.biosim.idl.simulation.food.FishTypeHelper.write(_out,getFishType());
				break;
			}
			case 10: // isReadyForHavest
			{
				_out = handler.createReply();
				_out.write_boolean(isReadyForHavest());
				break;
			}
			case 11: // getTankVolUsed 
			{ 
				_out = handler.createReply();
				_out.write_float(getTankVolUsed());
				break;
			}
			case 12: // getTimeTillSchoolClosure
			{
				_out = handler.createReply();
				_out.write_float(getTimeTillSchoolClosure());
				break;
			}
			case 13: // getTankVolumeTotal
			{
				_out = handler.createReply();
				_out.write_float(getTankVolTotal());
				break;
			}
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}

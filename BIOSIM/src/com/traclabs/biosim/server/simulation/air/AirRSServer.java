package biosim.server.air;

import biosim.server.framework.*;
import biosim.idl.air.*;
/**
 * The Air Revitalization System Server.  Creates an instance of the AirRS and registers it with the nameserver.
 *
 * @author    Scott Bell
 */

public class AirRSServer extends GenericServer{
	
	/**
	* Instantiates the server and binds it to the name server.
	* @param args aren't used for anything
	*/
	public static void main(String args[]) {
		AirRSServer myServer = new AirRSServer();
		AirRSImpl myAirRS = new AirRSImpl(myServer.getIDfromArgs(args));
		myServer.registerServerAndRun(new AirRSPOATie(myAirRS), myAirRS.getModuleName());
	}
}


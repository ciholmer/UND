package biosim.server.simulation.framework;

import biosim.idl.simulation.framework.*;
import biosim.server.framework.*;
import biosim.server.simulation.framework.*;
/**
 * The Accumulator Server.  Creates an instance of the Sim Environment and registers it with the nameserver.
 *
 * @author    Scott Bell
 */

public class AccumulatorServer extends GenericServer{
	
	/**
	* Instantiates the server and binds it to the name server.
	* @param args first argument checked for ID
	*/
	public static void main(String args[]) {
		AccumulatorServer myServer = new AccumulatorServer();
		AccumulatorImpl myAccumulator = new AccumulatorImpl(myServer.getIDfromArgs(args), myServer.getNamefromArgs(args));
		myServer.registerServerAndRun(new AccumulatorPOATie(myAccumulator), myAccumulator.getModuleName(), myAccumulator.getID());
	}
}


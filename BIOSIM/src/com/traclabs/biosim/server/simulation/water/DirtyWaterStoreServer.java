package biosim.server.water;

import biosim.server.framework.*;
import biosim.idl.water.*;
/**
 * The Dirty Water Store Server.  Creates an instance of the Dirty Water Store and registers it with the nameserver.
 *
 * @author    Scott Bell
 */

public class DirtyWaterStoreServer extends GenericServer{
	
	/**
	* Instantiates the server and binds it to the name server.
	* @param args aren't used for anything
	*/
	public static void main(String args[]) {
		DirtyWaterStoreServer myServer = new DirtyWaterStoreServer();
		DirtyWaterStoreImpl myDirtyWaterStore = new DirtyWaterStoreImpl(myServer.getIDfromArgs(args));
		myServer.registerServerAndRun(new DirtyWaterStorePOATie(myDirtyWaterStore), myDirtyWaterStore.getModuleName());
	}
}


package biosim.server.simulation.power;

/**
 * Nuclear Power Production System
 * @author    Scott Bell
 */

public class NuclearPowerPS extends PowerPSImpl {
	public NuclearPowerPS(int pID, String pName){
		super(pID, pName);
	}
	float calculatePowerProduced(){
		//Constant steady stream of power
		return randomFilter(500f);
	}
	
}

package biosim.server.water;

import biosim.idl.water.*;
import biosim.idl.util.*;
import biosim.server.util.*;
import biosim.server.framework.*;
/**
 * The Water Recovery System takes grey/dirty water and refines it to potable water for the crew members and grey water for the crops..
 * Class modeled after the paper:.
 * "Intelligent Control of a Water Recovery System: Three Years in the Trenches" by Bonasso, Kortenkamp, and Thronesbery
 * @author    Scott Bell
 */

public class WaterRSImpl extends BioModuleImpl implements WaterRSOperations {
	//The various subsystems of Water RS that clean the water
	private BWP myBWP;
	private RO myRO;
	private AES myAES;
	private PPS myPPS;
	private LogIndex myLogIndex;
	//References to the servers the Water RS takes/puts resources (power and water)
	
	private PotableWaterStore myPotableWaterStore;
	private DirtyWaterStore myDirtyWaterStore;
	private GreyWaterStore myGreyWaterStore;

	/**
	* Creates the Water RS and it's subsystems
	*/
	public WaterRSImpl(){
		myBWP = new BWP(this);
		myRO = new RO(this);
		myAES = new AES(this);
		myPPS = new PPS(this);
	}

	/**
	* Resets production/consumption levels and resets all the subsystems
	*/
	public void reset(){
		myBWP.reset();
		myRO.reset();
		myAES.reset();
		myPPS.reset();
	}

	/**
	* Checks whether RO subsystem has enough power or not
	* @return <code>true</code> if the RO subsystem has enough power, <code>false</code> if not.
	*/
	public boolean ROHasPower(){
		return myRO.hasPower();
	}
	
	public boolean ROIsEnabled(){
		return myRO.isEnabled();
	}
	
	public void setROEnabled(boolean pEnabled){
		myRO.setEnabled(pEnabled);
	}
	

	/**
	* Checks whether AES subsystem has enough power or not
	* @return <code>true</code> if the AES subsystem has enough power, <code>false</code> if not.
	*/
	public boolean AESHasPower(){
		return myAES.hasPower();
	}
	
	public boolean AESIsEnabled(){
		return myAES.isEnabled();
	}
	
	public void setAESEnabled(boolean pEnabled){
		myAES.setEnabled(pEnabled);
	}

	/**
	* Checks whether PPS subsystem has enough power or not
	* @return <code>true</code> if the PPS subsystem has enough power, <code>false</code> if not.
	*/
	public boolean PPSHasPower(){
		return myPPS.hasPower();
	}

	/**
	* Checks whether BWP subsystem has enough power or not
	* @return <code>true</code> if the BWP subsystem has enough power, <code>false</code> if not.
	*/
	public boolean BWPHasPower(){
		return myBWP.hasPower();
	}

	/**
	* Checks whether RO subsystem has enough water or not
	* @return <code>true</code> if the RO subsystem has enough water, <code>false</code> if not.
	*/
	public boolean ROHasWater(){
		return myRO.hasWater();
	}

	/**
	* Checks whether AES subsystem has enough water or not
	* @return <code>true</code> if the AES subsystem has enough water, <code>false</code> if not.
	*/
	public boolean AESHasWater(){
		return myAES.hasWater();
	}

	/**
	* Checks whether PPS subsystem has enough water or not
	* @return <code>true</code> if the PPS subsystem has enough water, <code>false</code> if not.
	*/
	public boolean PPSHasWater(){
		return myPPS.hasWater();
	}

	/**
	* Checks whether BWP subsystem has enough water or not
	* @return <code>true</code> if the BWP subsystem has enough water, <code>false</code> if not.
	*/
	public boolean BWPHasWater(){
		return myBWP.hasWater();
	}

	/**
	* Returns the RO subsystem
	* @return the RO subsystem
	*/
	protected RO getRO(){
		return myRO;
	}

	/**
	* Returns the AES subsystem
	* @return the AES subsystem
	*/
	protected AES getAES(){
		return myAES;
	}

	/**
	* Returns the PPS subsystem
	* @return the PPS subsystem
	*/
	protected PPS getPPS(){
		return myPPS;
	}

	/**
	* Returns the BWP subsystem
	* @return the BWP subsystem
	*/
	protected BWP getBWP(){
		return myBWP;
	}

	/**
	* Returns the potable water produced (in liters) by the Water RS during the current tick
	* @return the potable water produced (in liters) by the Water RS during the current tick
	*/
	public float getPotableWaterProduced(){
		return myPPS.getPotableWaterProduced();
	}

	/**
	* Returns the grey water produced (in liters) by the Water RS during the current tick
	* @return the grey water produced (in liters) by the Water RS during the current tick
	*/
	public float getGreyWaterProduced(){
		return 0f;
	}

	/**
	* Returns the grey water consumed (in liters) by the Water RS during the current tick
	* @return the grey water consumed (in liters) by the Water RS during the current tick
	*/
	public float getGreyWaterConsumed(){
		return myBWP.getGreyWaterConsumed();
	}

	/**
	* Returns the power consumed (in watts) by the Water RS during the current tick
	* @return the power consumed (in watts) by the Water RS during the current tick
	*/
	public float getPowerConsumed(){
		float powerConsumed = myAES.getPowerConsumed() + myPPS.getPowerConsumed() + 
							myBWP.getPowerConsumed() + myRO.getPowerConsumed();
		return powerConsumed;
	}

	/**
	* Returns the dirty water consumed (in liters) by the Water RS during the current tick
	* @return the dirty water consumed (in liters) by the Water RS during the current tick
	*/
	public float getDirtyWaterConsumed(){
		return myBWP.getDirtyWaterConsumed();
	}

	/**
	* When ticked, the Water RS:
	* 1) ticks each subsystem.
	*/
	public void tick(){
		//tick each system
		myBWP.tick();
		myRO.tick();
		myAES.tick();
		myPPS.tick();
		if (moduleLogging)
			log();
	}


	/**
	* Returns the name of this module (WaterRS)
	* @return the name of the module
	*/
	public String getModuleName(){
		return "WaterRS";
	}

	private void log(){
		//If not initialized, fill in the log
		if (!logInitialized){
			myLogIndex = new LogIndex();
			LogNode currentPotableWaterProducedHead = myLog.getHead().addChild("Potable Water Produced");
			myLogIndex.currentPotableWaterProducedIndex = currentPotableWaterProducedHead.addChild(""+getPotableWaterProduced());
			LogNode currentGreyWaterProducedHead = myLog.getHead().addChild("Grey Water Produced");
			myLogIndex.currentGreyWaterProducedIndex = currentGreyWaterProducedHead.addChild(""+getGreyWaterProduced());
			LogNode currentPowerConsumedHead = myLog.getHead().addChild("Power Consumed");
			myLogIndex.currentPowerConsumedIndex = currentPowerConsumedHead.addChild(""+getPowerConsumed());
			LogNode currentDirtyWaterConsumedHead = myLog.getHead().addChild("Dirty Water Consumed");
			myLogIndex.currentDirtyWaterConsumedIndex = currentDirtyWaterConsumedHead.addChild(""+getDirtyWaterConsumed());
			LogNode currentGreyWaterConsumedHead = myLog.getHead().addChild("Grey Water Consumed");
			myLogIndex.currentGreyWaterConsumedIndex = currentGreyWaterConsumedHead.addChild(""+getGreyWaterConsumed());
			myLogIndex.AESIndex = myLog.getHead().addChild("AES");
			myAES.log(myLogIndex.AESIndex);
			myLogIndex.BWPIndex = myLog.getHead().addChild("BWP");
			myBWP.log(myLogIndex.BWPIndex);
			myLogIndex.ROIndex = myLog.getHead().addChild("RO");
			myRO.log(myLogIndex.ROIndex);
			myLogIndex.PPSIndex = myLog.getHead().addChild("PPS");
			myPPS.log(myLogIndex.PPSIndex);
			logInitialized = true;
		}
		else{
			myLogIndex.currentPotableWaterProducedIndex.setValue(""+getPotableWaterProduced());
			myLogIndex.currentGreyWaterProducedIndex.setValue(""+getGreyWaterProduced());
			myLogIndex.currentPowerConsumedIndex.setValue(""+getPowerConsumed());
			myLogIndex.currentDirtyWaterConsumedIndex.setValue(""+getDirtyWaterConsumed());
			myLogIndex.currentGreyWaterConsumedIndex.setValue(""+getGreyWaterConsumed());
			myAES.log(myLogIndex.AESIndex);
			myBWP.log(myLogIndex.BWPIndex);
			myRO.log(myLogIndex.ROIndex);
			myPPS.log(myLogIndex.PPSIndex);
		}
		sendLog(myLog);
	}

	/**
	* For fast reference to the log tree
	*/
	private class LogIndex{
		public LogNode powerNeededIndex;
		public LogNode currentPotableWaterProducedIndex;
		public LogNode currentGreyWaterProducedIndex;
		public LogNode currentPowerConsumedIndex;
		public LogNode currentDirtyWaterConsumedIndex;
		public LogNode currentGreyWaterConsumedIndex;
		public LogNode AESIndex;
		public LogNode PPSIndex;
		public LogNode BWPIndex;
		public LogNode ROIndex;
	}
}

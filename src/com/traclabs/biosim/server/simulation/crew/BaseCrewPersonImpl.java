package com.traclabs.biosim.server.simulation.crew;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.framework.BioModuleHelper;
import com.traclabs.biosim.idl.framework.LogLevel;
import com.traclabs.biosim.idl.simulation.crew.Activity;
import com.traclabs.biosim.idl.simulation.crew.CrewGroup;
import com.traclabs.biosim.idl.simulation.crew.CrewGroupHelper;
import com.traclabs.biosim.idl.simulation.crew.CrewPersonHelper;
import com.traclabs.biosim.idl.simulation.crew.CrewPersonPOA;
import com.traclabs.biosim.idl.simulation.crew.EVAActivity;
import com.traclabs.biosim.idl.simulation.crew.MaitenanceActivity;
import com.traclabs.biosim.idl.simulation.crew.RepairActivity;
import com.traclabs.biosim.idl.simulation.crew.Sex;
import com.traclabs.biosim.util.OrbUtils;

/**
 * The Base Crew Person Implementation. Eats/drinks/excercises away resources
 * according to a set schedule.
 * 
 * @author Scott Bell
 */

public abstract class BaseCrewPersonImpl extends CrewPersonPOA {
	// The name of this crew memeber
	private String myName = "No Name";

	// The current activity index this crew member is on
	private int myCurrentActivityIndex = 0;

	// The current activity this crew member is on
	private Activity myCurrentActivity;

	// How long this crew member has been performing thier current activity (in
	// ticks)
	private int myTimeActivityPerformed = 0;

	// The current age of the crew memeber
	private float myAge = 30f;

	// The current weight of the crew memeber
	private float myWeight = 170f;

	// The sex of the crew memeber
	private Sex mySex = Sex.male;

	// The schedule used for this crew memeber
	private Schedule mySchedule;

	private int myArrivalTick = 0;

	private int myDepartureTick = Integer.MAX_VALUE;

	private CrewGroup myBaseCrewGroup;

	private CrewGroup myCurrentCrewGroup;

	protected Logger myLogger;

	/**
	 * Constructor that creates a new crew person
	 * 
	 * @param pName
	 *            the name of the new crew person
	 * @param pAge
	 *            the age of the new crew person
	 * @param pSex
	 *            the sex of the new crew person
	 * @param pCrewGroup
	 *            the crew that the new crew person belongs in
	 */
	public BaseCrewPersonImpl(String pName, float pAge, float pWeight,
			Sex pSex, int pArrivalTick, int pDepartureTick,
			CrewGroup pBaseCrewGroup, CrewGroup pCurrentCrewGroup) {
		this(pName, pAge, pWeight, pSex, pArrivalTick, pDepartureTick,
				pBaseCrewGroup, new Schedule(pBaseCrewGroup));
	}

	public BaseCrewPersonImpl(String pName, float pAge, float pWeight,
			Sex pSex, int pArrivalTick, int pDepartureTick,
			CrewGroup bCrewGroup, Schedule pSchedule) {
		myLogger = Logger.getLogger(this.getClass() + "." + pName);
		myName = pName;
		myAge = pAge;
		myWeight = pWeight;
		mySex = pSex;
		myArrivalTick = pArrivalTick;
		myDepartureTick = pDepartureTick;
		myBaseCrewGroup = myCurrentCrewGroup = bCrewGroup;
		mySchedule = pSchedule;
		myCurrentActivity = mySchedule
				.getScheduledActivityByOrder(myCurrentActivityIndex);
	}

	/**
	 * Resets the state of this crew member
	 */
	public void reset() {
		myCurrentActivityIndex = 0;
		myTimeActivityPerformed = 0;
		myCurrentActivity = mySchedule.getScheduledActivityByOrder(myCurrentActivityIndex);
		myCurrentCrewGroup = myBaseCrewGroup;
	}

	public void setLogLevel(LogLevel pLevel) {
		if (pLevel == LogLevel.OFF)
			myLogger.setLevel(Level.OFF);
		else if (pLevel == LogLevel.INFO)
			myLogger.setLevel(Level.INFO);
		else if (pLevel == LogLevel.DEBUG)
			myLogger.setLevel(Level.DEBUG);
		else if (pLevel == LogLevel.ERROR)
			myLogger.setLevel(Level.ERROR);
		else if (pLevel == LogLevel.WARN)
			myLogger.setLevel(Level.WARN);
		else if (pLevel == LogLevel.FATAL)
			myLogger.setLevel(Level.FATAL);
		else if (pLevel == LogLevel.ALL)
			myLogger.setLevel(Level.ALL);
	}

	public void setArrivalTick(int pArrivalTick) {
		myArrivalTick = pArrivalTick;
	}

	public int getArrivalTick() {
		return myArrivalTick;
	}

	public void setDepartureTick(int pDepartureTick) {
		myDepartureTick = pDepartureTick;
	}

	public int getDepartureTick() {
		return myDepartureTick;
	}

	/**
	 * Returns the name given to this crew memeber
	 * 
	 * @return the name of the crew member
	 */
	public String getName() {
		return myName;
	}

	/**
	 * Returns the age given to this crew memeber
	 * 
	 * @return the age of the crew member
	 */
	public float getAge() {
		return myAge;
	}

	/**
	 * Returns how much the crew member weighs (in kilograms)
	 * 
	 * @return the crew person's weight in kilograms
	 */
	public float getWeight() {
		return myWeight;
	}

	/**
	 * Checks whether the crew member is dead or not
	 * 
	 * @return <code>true</code> if the crew memeber is dead,
	 *         <code>false</code> if not.
	 */
	public boolean isDead() {
		return (myCurrentActivity.getName().equals("dead"));
	}

	/**
	 * Returns the crew member's sex
	 * 
	 * @return the crew person's sex
	 */
	public Sex getSex() {
		return mySex;
	}

	public void setSchedule(Schedule pSchedule) {
		mySchedule = pSchedule;
	}

	/**
	 * Makes this crew member sick (sleep like)
	 */
	public void sicken() {
		myCurrentActivity = mySchedule.getActivityByName("sick");
	}

	/**
	 * Returns the crew member's current activity as an activity object
	 * 
	 * @return the crew member's current activity as an activity object
	 */
	public Activity getCurrentActivity() {
		return myCurrentActivity;
	}
	
	/** CIH 05/2019
	 * Returns the crew member's current activity name as a string
	 * 
	 * @return the crew member's current activity name as a string
	 */
	public String getCurrentActivityName() {
		return myCurrentActivity.getName();
	}

	/**
	 * Sets the crew memeber's activity to a new one specified.
	 * 
	 * @param pActivity
	 *            the crew member's new activity
	 */
	public void setCurrentActivity(Activity pActivity) {
		myCurrentActivity = pActivity;
		myCurrentActivityIndex = mySchedule
				.getOrderOfScheduledActivity(myCurrentActivity.getName());
	}

	/**
	 * Returns a scheduled activity by name (like "sleeping")
	 * 
	 * @param name
	 *            the name of the activity to fetch
	 * @return the activity in the schedule asked for by name
	 */
	public Activity getActivityByName(String name) {
		return mySchedule.getActivityByName(name);
	}

	/**
	 * Returns a scheduled activity by order
	 * 
	 * @param order
	 *            the order of the activity to fetch
	 * @return the activity in the schedule asked for by order
	 */
	public Activity getScheduledActivityByOrder(int order) {
		return mySchedule.getScheduledActivityByOrder(order);
	}

	/**
	 * Returns how long the crew member's been performing the current activity
	 * 
	 * @return the time the crew memeber has been performing the current
	 *         activity
	 */
	public int getTimeActivityPerformed() {
		return myTimeActivityPerformed;
	}

	/**
	 * Returns a string representation of the crew memeber in the form: [name]
	 * now performing [activity] for [x] hours
	 * 
	 * @return a string representation of the crew member
	 */
	public String toString() {
		return (myName + " now performing activity "
				+ myCurrentActivity.getName() + " for "
				+ myTimeActivityPerformed + " of "
				+ myCurrentActivity.getTimeLength() + " hours");
	}

	public boolean isSick() {
		return (myCurrentActivity.getName().equals("sick"));
	}

	/**
	 * If the crew member has been performing the current activity long enough,
	 * the new scheduled activity is assigned.
	 */
	private void advanceActivity() {
        checkForMeaningfulActivity();
		if (myTimeActivityPerformed >= myCurrentActivity.getTimeLength()) {
			myCurrentActivityIndex++;
			if (myCurrentActivityIndex >= (mySchedule
					.getNumberOfScheduledActivities()))
				myCurrentActivityIndex = 1;
			myCurrentActivity = mySchedule
					.getScheduledActivityByOrder(myCurrentActivityIndex);
			myTimeActivityPerformed = 0;
		}
	}

	/**
	 * @param pActivity
	 */
	private void handleEVA(EVAActivity pEVAActivity) {
		myLogger.debug("Handling EVA");
		if (myTimeActivityPerformed <= 1)
			startEVA(pEVAActivity.getEVACrewGroupName());
		else if (myTimeActivityPerformed >= pEVAActivity.getTimeLength())
			endEVA(pEVAActivity.getBaseCrewGroupName());
	}

	/**
	 * @param evaCrewGroupName
	 */
	private void startEVA(String evaCrewGroupName) {
		myLogger.debug("Starting EVA");
		// remove 5% from base environment (assume 3.7 m3 airlock)
		myCurrentCrewGroup.getAirConsumerDefinition().getEnvironments()[0]
				.removeAirlockPercentage(0.05f);
		// detach from current crew group
		myCurrentCrewGroup.detachCrewPerson(getName());
		// attach to eva crew group
		CrewGroup evaCrewGroup = CrewGroupHelper.narrow(OrbUtils.getBioModule(
				myCurrentCrewGroup.getID(), evaCrewGroupName));
		evaCrewGroup.attachCrewPerson(CrewPersonHelper.narrow((OrbUtils
				.poaToCorbaObj(this))));
		myCurrentCrewGroup = evaCrewGroup;
		// perform activity for X ticks
	}

	/**
	 * @param baseCrewGroupName
	 */
	private void endEVA(String baseCrewGroupName) {
		myLogger.debug("Ending EVA");
		// detach from EVA crew group
		myCurrentCrewGroup.detachCrewPerson(getName());
		// reattach to base crew group
		CrewGroup baseCrewGroup = CrewGroupHelper.narrow((OrbUtils
				.getBioModule(myCurrentCrewGroup.getID(), baseCrewGroupName)));
		baseCrewGroup.attachCrewPerson(CrewPersonHelper.narrow((OrbUtils
				.poaToCorbaObj(this))));
		myCurrentCrewGroup = baseCrewGroup;
		// remove 5% from base environment (assume 3.7 m3 airlock)
		myCurrentCrewGroup.getAirConsumerDefinition().getEnvironments()[0]
				.removeAirlockPercentage(0.05f);
	}
	 /** 
     * CIH 052019 changed && to ^, && was not evaluating correctly to determine if someone was 'on board'
     * Added else statement to change from "absent" to "onboard" if it was set
     */
	private void arriveOrDepart() {
		if ((myCurrentCrewGroup.getMyTicks() < myArrivalTick)
				^ (myCurrentCrewGroup.getMyTicks() >= myDepartureTick)) {
			myCurrentActivity = mySchedule.getActivityByName("absent");
		} else {
			if (myCurrentActivity ==  mySchedule.getActivityByName("absent")){
				//CIH 052019 change from absent to something else, otherwise leave it alone
				myCurrentActivity = mySchedule.getActivityByName("onboard");
			}
			
		}
	}
	
	/**
	 * CIH 052019 added to easily check to see if the crew member is present
	 */

	public boolean isOnBoard() {
		return (!myCurrentActivity.getName().equals("absent"));
	}
	
	/**
     * Looks at current activity to see if it's "meaningful" i.e., the activity
     * affects other modules. mission - productivity measure, used for metrics.
     * the longer the crew does this, the better evaluation (not implemented
     * yet) maitenance - prevents other modules from breaking down (not
     * implemented yet) repair - attempts to fix a module. may have to be called
     * several time depending on the severity of the malfunction
     * 
     * New Stuff: 1) Put new environment to join in activity (make it like a
     * repair activity) 2) sap 10% of air from old environment (look at air
     * input of crew) 3) hook up old environment at end of EVA task (keep a
     * record) 4) allow modules to be reconfigured using acutators (work on this
     * later)
     */
    protected void checkForMeaningfulActivity() {
		String activityName = getCurrentActivity().getName();
        if (activityName.equals("repair")) {
            RepairActivity repairActivity = (RepairActivity) (myCurrentActivity);
            repairModule(repairActivity.getModuleNameToRepair(), repairActivity
                    .getMalfunctionIDToRepair());
        } else if (activityName.equals("EVA")) {
            handleEVA((EVAActivity) (myCurrentActivity));
        } else if (activityName.equals("maitenance")) {
            MaitenanceActivity maitenanceActivity = (MaitenanceActivity) (myCurrentActivity);
            maintainModule(maitenanceActivity.getModuleNameToMaintain());
        }
    }

	/**
	 * When the CrewGroup ticks the crew member, the member: 1) increases the
	 * time the activity has been performed by 1. on the condition that the crew
	 * memeber isn't dead he/she then: (a) check to see if they are scheduled to arrive or depart. 
	 * (b) check to see if they are onboard. Then: 2) attempts to collect references to
	 * various server (if not already done). 3) possibly advances to the next
	 * activity. 4) consumes air/food/water, exhales and excretes. 5) takes
	 * afflictions from lack of any resources. 6) checks whether afflictions (if
	 * any) are fatal.
	 */
	public void tick() {
		myTimeActivityPerformed++;
		if (!isDead()) {
			arriveOrDepart();      //CIH 052019 - Add departure check
			if (isOnBoard()) {	   //CIH 052019 - Check to see if the member is present
				myLogger.debug("Crew member onboard, do activities, produce and consume resources");
				advanceActivity();
				consumeAndProduceResources();
				if (getCurrentCrewGroup().getDeathEnabled())
					afflictAndRecover();
			} else {               //CIH 200209 - Crew member is not on onboard, reset crew member values
				myLogger.debug("Crew member not onboard");
				crewDepart();
			}
		}
		myLogger.info(getName() + " is " + getCurrentActivityName());
		if (myLogger.isDebugEnabled())
			log();
	}

	protected abstract void consumeAndProduceResources();

	protected abstract void afflictAndRecover();
	
	protected abstract void crewDepart();

	public void insertActivityInSchedule(Activity pActivity, int pOrder) {
		mySchedule.insertActivityInSchedule(pActivity, pOrder);
	}

	public void insertActivityInScheduleNow(Activity pActivity) {
		mySchedule.insertActivityInSchedule(pActivity,
				myCurrentActivityIndex + 1);
	}

	public int getOrderOfScheduledActivity(String activityName) {
		return mySchedule.getOrderOfScheduledActivity(activityName);
	}

	public void kill() {
		myCurrentActivity = mySchedule.getActivityByName("dead");
		myCurrentActivityIndex = 0;
	}

	public void log() {
		myLogger.debug("name=" + myName);
		myLogger.debug("current_activity=" + myCurrentActivity.getName());
		myLogger.debug("current_activity_order=" + myCurrentActivityIndex);
		myLogger.debug("duration_of_activity=" + getTimeActivityPerformed());
		myLogger.debug("has_died=" + isDead());
		myLogger.debug("sick=" + isSick());
		myLogger.debug("age=" + getAge());
		myLogger.debug("weight" + getWeight());
		myLogger.debug("sex=" + getSex());
	}

	public CrewGroup getCurrentCrewGroup() {
		return myCurrentCrewGroup;
	}
	
	/**
     * attempts to fix a module. may have to be called several time depending on
     * the severity of the malfunction invoked when crew person performs
     * "repair" activity
     */
    private void repairModule(String moduleName, long id) {
        try {
            BioModule moduleToRepair = BioModuleHelper.narrow(OrbUtils
                    .getNamingContext(getCurrentCrewGroup().getID()).resolve_str(
                            moduleName));
            moduleToRepair.doSomeRepairWork(id);
        } catch (org.omg.CORBA.UserException e) {
            myLogger.warn("CrewPersonImp:" + getCurrentCrewGroup().getID()
                    + ": Couldn't locate " + moduleName
                    + " to repair, skipping...");
        }
    }

    /**
     * prevents other modules from breaking down invoked when crew person
     * performs "maitenance" activity
     */
    private void maintainModule(String moduleName) {
        try {
            BioModule module = BioModuleHelper.narrow(OrbUtils
                    .getNamingContext(getCurrentCrewGroup().getID()).resolve_str(
                            moduleName));
            module.maintain();
        } catch (org.omg.CORBA.UserException e) {
            myLogger.warn("CrewPersonImp:" + getCurrentCrewGroup().getID()
                    + ": Couldn't locate " + moduleName
                    + " to repair, skipping...");
        }
    }

	/**
	 * @return The amount of CO2 exhaled (in moles) by the crew member
	 */
	public float getCO2Produced() {
		return -1;
	}
	
	/**
	 * @return The amount of dirty water produced (in liters) by the crew member
	 */
	public float getDirtyWaterProduced() {
		return -1;
	}

	/**
	 * @return The amount of food consumed (in kilograms) by the crew member
	 */
	public float getFoodConsumed() {
		return -1;
	}

	/**
	 * @return The amount of grey water produced (in liters) by the crew member
	 */
	public float getGreyWaterProduced() {
		return -1;
	}

	/**
	 * @return The amount of oxygen consumed (in moles) by the crew member
	 */
	public float getO2Consumed() {
		return -1;
	}

	/**
	 * @return The amount of potable water consumed (in liters) by the crew member
	 */
	public float getPotableWaterConsumed() {
		return -1;
	}

	/**
	 * @return Return a metric on how well the crew member is doing on his or her's mission
	 */
	public float getProductivity() {
		return -1;
	}

	/**
	 * @return Return true if the crew is poisoned from CO2
	 */
	public boolean isPoisoned() {
		return false;
	}

	/**
	 * @return Return true if the crew is starving
	 */
	public boolean isStarving() {
		return false;
	}

	/**
	 * @return Return true if the crew is suffocating
	 */
	public boolean isSuffocating() {
		return false;
	}

	/**
	 * @return Return true if the crew is thirsty
	 */
	public boolean isThirsty() {
		return false;
	}

}
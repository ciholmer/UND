package com.traclabs.biosim.server.simulation.crew;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import com.traclabs.biosim.idl.framework.Malfunction;
import com.traclabs.biosim.idl.framework.MalfunctionIntensity;
import com.traclabs.biosim.idl.framework.MalfunctionLength;
import com.traclabs.biosim.idl.simulation.crew.CrewGroup;
import com.traclabs.biosim.idl.simulation.crew.CrewGroupOperations;
import com.traclabs.biosim.idl.simulation.crew.CrewPerson;
import com.traclabs.biosim.idl.simulation.crew.CrewPersonHelper;
import com.traclabs.biosim.idl.simulation.crew.RepairActivity;
import com.traclabs.biosim.idl.simulation.crew.RepairActivityHelper;
import com.traclabs.biosim.idl.simulation.crew.RepairActivityPOATie;
import com.traclabs.biosim.idl.simulation.crew.ScheduleType;
import com.traclabs.biosim.idl.simulation.crew.Sex;
import com.traclabs.biosim.idl.simulation.framework.AirConsumerDefinition;
import com.traclabs.biosim.idl.simulation.framework.AirConsumerOperations;
import com.traclabs.biosim.idl.simulation.framework.AirProducerDefinition;
import com.traclabs.biosim.idl.simulation.framework.AirProducerOperations;
import com.traclabs.biosim.idl.simulation.framework.DirtyWaterProducerDefinition;
import com.traclabs.biosim.idl.simulation.framework.DirtyWaterProducerOperations;
import com.traclabs.biosim.idl.simulation.framework.DryWasteProducerDefinition;
import com.traclabs.biosim.idl.simulation.framework.DryWasteProducerOperations;
import com.traclabs.biosim.idl.simulation.framework.FoodConsumerDefinition;
import com.traclabs.biosim.idl.simulation.framework.FoodConsumerOperations;
import com.traclabs.biosim.idl.simulation.framework.GreyWaterProducerDefinition;
import com.traclabs.biosim.idl.simulation.framework.GreyWaterProducerOperations;
import com.traclabs.biosim.idl.simulation.framework.PotableWaterConsumerDefinition;
import com.traclabs.biosim.idl.simulation.framework.PotableWaterConsumerOperations;
import com.traclabs.biosim.server.simulation.framework.AirConsumerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.AirProducerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.DirtyWaterProducerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.DryWasteProducerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.FoodConsumerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.GreyWaterProducerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.PotableWaterConsumerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.SimBioModuleImpl;
import com.traclabs.biosim.server.util.OrbUtils;

/**
 * The Crew Implementation. Holds multiple crew persons and their schedule.
 * 
 * @author Scott Bell
 */

public class CrewGroupImpl extends SimBioModuleImpl implements
        CrewGroupOperations, AirConsumerOperations,
        PotableWaterConsumerOperations, FoodConsumerOperations,
        AirProducerOperations, GreyWaterProducerOperations,
        DirtyWaterProducerOperations, DryWasteProducerOperations {

    //Consumers, Producers
    private FoodConsumerDefinitionImpl myFoodConsumerDefinitionImpl;

    private AirConsumerDefinitionImpl myAirConsumerDefinitionImpl;

    private PotableWaterConsumerDefinitionImpl myPotableWaterConsumerDefinitionImpl;

    private GreyWaterProducerDefinitionImpl myGreyWaterProducerDefinitionImpl;

    private DirtyWaterProducerDefinitionImpl myDirtyWaterProducerDefinitionImpl;

    private AirProducerDefinitionImpl myAirProducerDefinitionImpl;

    private DryWasteProducerDefinitionImpl myDryWasteProducerDefinitionImpl;

    //The crew persons that make up the crew.
    //They are the ones consuming air/food/water and producing air/water/waste
    // as they perform activities
    private Map crewPeople;

    private float healthyPercentage = 1f;

    private Random myRandom;

    private List crewScheduledForRemoval;

    private List crewScheduledForAddition;

    /**
     * Default constructor. Uses a default schedule.
     */
    public CrewGroupImpl(int pID, String pName) {
        super(pID, pName);
        crewScheduledForRemoval = new Vector();
        crewScheduledForAddition = new Vector();
        crewPeople = new Hashtable();
        myRandom = new Random();

        myFoodConsumerDefinitionImpl = new FoodConsumerDefinitionImpl();
        myAirConsumerDefinitionImpl = new AirConsumerDefinitionImpl();
        myPotableWaterConsumerDefinitionImpl = new PotableWaterConsumerDefinitionImpl();
        myGreyWaterProducerDefinitionImpl = new GreyWaterProducerDefinitionImpl();
        myDirtyWaterProducerDefinitionImpl = new DirtyWaterProducerDefinitionImpl();
        myAirProducerDefinitionImpl = new AirProducerDefinitionImpl();
        myDryWasteProducerDefinitionImpl = new DryWasteProducerDefinitionImpl();
    }

    public FoodConsumerDefinition getFoodConsumerDefinition() {
        return myFoodConsumerDefinitionImpl.getCorbaObject();
    }

    public AirConsumerDefinition getAirConsumerDefinition() {
        return myAirConsumerDefinitionImpl.getCorbaObject();
    }

    public PotableWaterConsumerDefinition getPotableWaterConsumerDefinition() {
        return myPotableWaterConsumerDefinitionImpl.getCorbaObject();
    }

    public GreyWaterProducerDefinition getGreyWaterProducerDefinition() {
        return myGreyWaterProducerDefinitionImpl.getCorbaObject();
    }

    public DirtyWaterProducerDefinition getDirtyWaterProducerDefinition() {
        return myDirtyWaterProducerDefinitionImpl.getCorbaObject();
    }

    public AirProducerDefinition getAirProducerDefinition() {
        return myAirProducerDefinitionImpl.getCorbaObject();
    }

    public DryWasteProducerDefinition getDryWasteProducerDefinition() {
        return myDryWasteProducerDefinitionImpl.getCorbaObject();
    }

    /**
     * Creates a crew person and adds them to the crew
     * 
     * @param pName
     *            the name of the new crew person
     * @param pAge
     *            the age of the new crew person
     * @param pWeight
     *            the weight of the new crew person
     * @param pSex
     *            the sex of the new crew person
     * @return the crew person just created
     */
    public CrewPerson createCrewPerson(String pName, float pAge, float pWeight,
            Sex pSex, int pArrivalTick, int pDepartureTick, CrewGroup crewGroup) {
        CrewPersonImpl newCrewPersonImpl = new CrewPersonImpl(pName, pAge,
                pWeight, pSex, pArrivalTick, pDepartureTick, this, crewGroup);
        CrewPerson newCrewPerson = CrewPersonHelper.narrow((OrbUtils
                .poaToCorbaObj(newCrewPersonImpl)));
        crewPeople.put(pName, newCrewPerson);
        return newCrewPerson;
    }

    public CrewPerson createCrewPerson(String pName, float pAge, float pWeight,
            Sex pSex, int pArrivalTick, int pDepartureTick, Schedule pSchedule,
            CrewGroup crewGroup) {
        CrewPersonImpl newCrewPersonImpl = new CrewPersonImpl(pName, pAge,
                pWeight, pSex, pArrivalTick, pDepartureTick, this, crewGroup,
                pSchedule);
        CrewPerson newCrewPerson = CrewPersonHelper.narrow((OrbUtils
                .poaToCorbaObj(newCrewPersonImpl)));
        crewPeople.put(pName, newCrewPerson);
        return newCrewPerson;
    }

    /**
     * Returns all the current crew persons who are in the crew
     * 
     * @return an array of the crew persons in the crew
     */
    public CrewPerson[] getCrewPeople() {
        CrewPerson[] theCrew = new CrewPerson[crewPeople.size()];
        int i = 0;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext(); i++) {
            theCrew[i] = (CrewPerson) (iter.next());
        }
        return theCrew;
    }

    public void scheduleRepair(String moduleName, long malfunctionID,
            int timeLength) {
        int randomCrewIndex = myRandom.nextInt(crewPeople.size());
        CrewPerson randomCrewPerson = (CrewPerson) ((crewPeople.values()
                .toArray())[randomCrewIndex]);
        RepairActivityImpl newRepairActivityImpl = new RepairActivityImpl(
                moduleName, malfunctionID, timeLength);
        RepairActivity newRepairActivity = RepairActivityHelper
                .narrow(OrbUtils.poaToCorbaObj(new RepairActivityPOATie(
                        newRepairActivityImpl)));
        randomCrewPerson.insertActivityInScheduleNow(newRepairActivity);
    }

    public void setSchedule(ScheduleType pSchedule) {
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            currentPerson.reset();
        }
    }

    /**
     * Returns a crew person given their name
     * 
     * @param crewPersonName
     *            the name of the crew person to fetch
     * @return the crew person asked for
     */
    public CrewPerson getCrewPerson(String crewPersonName) {
        CrewPerson foundPerson = (CrewPerson) (crewPeople.get(crewPersonName));
        return foundPerson;
    }

    protected String getMalfunctionName(MalfunctionIntensity pIntensity,
            MalfunctionLength pLength) {
        StringBuffer returnBuffer = new StringBuffer();
        if (pIntensity == MalfunctionIntensity.SEVERE_MALF)
            returnBuffer.append("Severe ");
        else if (pIntensity == MalfunctionIntensity.MEDIUM_MALF)
            returnBuffer.append("Medium ");
        else if (pIntensity == MalfunctionIntensity.LOW_MALF)
            returnBuffer.append("Low ");
        if (pLength == MalfunctionLength.TEMPORARY_MALF)
            returnBuffer.append("Sickness (Temporary)");
        else if (pLength == MalfunctionLength.PERMANENT_MALF)
            returnBuffer.append("Sickness (Permanent)");
        return returnBuffer.toString();
    }

    private void clearActualFlowRates() {
        Arrays.fill(getPotableWaterConsumerDefinition().getActualFlowRates(),
                0f);
        Arrays.fill(getGreyWaterProducerDefinition().getActualFlowRates(), 0f);
        Arrays.fill(getDirtyWaterProducerDefinition().getActualFlowRates(), 0f);
        Arrays.fill(getDryWasteProducerDefinition().getActualFlowRates(), 0f);
    }

    /**
     * Processes a tick by ticking each crew person it knows about.
     */
    public void tick() {
        super.tick();
        clearActualFlowRates();
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson tempPerson = (CrewPerson) (iter.next());
            tempPerson.tick();
        }
        //Add those scheduled
        for (Iterator iter = crewScheduledForAddition.iterator(); iter
                .hasNext();) {
            CrewPerson crewPersonToAdd = (CrewPerson) (iter.next());
            crewPeople.put(crewPersonToAdd.getName(), crewPersonToAdd);
        }
        crewScheduledForAddition.clear();
        //Remove those scheduled
        for (Iterator iter = crewScheduledForRemoval.iterator(); iter.hasNext();) {
            String crewPersonNameToRemove = (String) (iter.next());
            crewPeople.remove(crewPersonNameToRemove);
        }
        crewScheduledForRemoval.clear();
    }

    protected void performMalfunctions() {
        healthyPercentage = 1f;
        for (Iterator iter = myMalfunctions.values().iterator(); iter.hasNext();) {
            Malfunction currentMalfunction = (Malfunction) (iter.next());
            if (currentMalfunction.getLength() == MalfunctionLength.TEMPORARY_MALF) {
                if (currentMalfunction.getIntensity() == MalfunctionIntensity.SEVERE_MALF)
                    healthyPercentage *= 0.50;
                else if (currentMalfunction.getIntensity() == MalfunctionIntensity.MEDIUM_MALF)
                    healthyPercentage *= 0.25;
                else if (currentMalfunction.getIntensity() == MalfunctionIntensity.LOW_MALF)
                    healthyPercentage *= 0.10;
            } else if (currentMalfunction.getLength() == MalfunctionLength.PERMANENT_MALF) {
                if (currentMalfunction.getIntensity() == MalfunctionIntensity.SEVERE_MALF)
                    healthyPercentage *= 0.50;
                else if (currentMalfunction.getIntensity() == MalfunctionIntensity.MEDIUM_MALF)
                    healthyPercentage *= 0.25;
                else if (currentMalfunction.getIntensity() == MalfunctionIntensity.LOW_MALF)
                    healthyPercentage *= 0.10;
            }
        }

        int peopleAsleep = (new Float((1 - healthyPercentage)
                * crewPeople.size())).intValue();
        for (int i = 0; i < peopleAsleep; i++) {
            int randomIndex = myRandom.nextInt(crewPeople.size());
            CrewPerson tempPerson = (CrewPerson) ((crewPeople.values()
                    .toArray())[randomIndex]);
            tempPerson.sicken();
        }
    }

    /**
     * Resets the schedule and deletes all the crew persons
     */
    public void reset() {
        super.reset();
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            currentPerson.reset();
        }
    }

    /**
     * Gets the productivity of the crew
     */
    public float getProductivity() {
        float productivity = 0f;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            productivity += currentPerson.getProductivity();
        }
        return productivity;
    }

    public boolean anyDead() {
        if (crewPeople.size() < 1)
            return false;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            if (currentPerson.isDead())
                return true;
        }
        return false;
    }

    public boolean isDead() {
        if (crewPeople.size() < 1)
            return false;
        boolean areTheyDead = true;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            areTheyDead = areTheyDead && currentPerson.isDead();
        }
        return areTheyDead;
    }

    public int getCrewSize() {
        return crewPeople.size();
    }

    public float getGreyWaterProduced() {
        float totalGreyWaterProduced = 0f;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            totalGreyWaterProduced += currentPerson.getGreyWaterProduced();
        }
        return totalGreyWaterProduced;
    }

    public float getDirtyWaterProduced() {
        float totalDirtyWaterProduced = 0f;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            totalDirtyWaterProduced += currentPerson.getDirtyWaterProduced();
        }
        return totalDirtyWaterProduced;
    }

    public float getPotableWaterConsumed() {
        float totalPotableWaterConsumed = 0f;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            totalPotableWaterConsumed += currentPerson
                    .getPotableWaterConsumed();
        }
        return totalPotableWaterConsumed;
    }

    public float getFoodConsumed() {
        float totalFoodConsumed = 0f;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            totalFoodConsumed += currentPerson.getFoodConsumed();
        }
        return totalFoodConsumed;
    }

    public float getCO2Produced() {
        float totalCO2Produced = 0f;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            totalCO2Produced += currentPerson.getCO2Produced();
        }
        return totalCO2Produced;
    }

    public float getO2Consumed() {
        float totalO2Consumed = 0f;
        for (Iterator iter = crewPeople.values().iterator(); iter.hasNext();) {
            CrewPerson currentPerson = (CrewPerson) (iter.next());
            totalO2Consumed += currentPerson.getO2Consumed();
        }
        return totalO2Consumed;
    }

    public void detachCrewPerson(String name) {
        crewScheduledForRemoval.add(name);
    }

    public void attachCrewPerson(CrewPerson pCrewPerson) {
        crewScheduledForAddition.add(pCrewPerson);
    }

    /**
     * @return Returns the myAirConsumerDefinitionImpl.
     */
    protected AirConsumerDefinitionImpl getAirConsumerDefinitionImpl() {
        return myAirConsumerDefinitionImpl;
    }

    /**
     * @return Returns the myAirProducerDefinitionImpl.
     */
    protected AirProducerDefinitionImpl getAirProducerDefinitionImpl() {
        return myAirProducerDefinitionImpl;
    }

    /**
     * @return Returns the myDirtyWaterProducerDefinitionImpl.
     */
    protected DirtyWaterProducerDefinitionImpl getDirtyWaterProducerDefinitionImpl() {
        return myDirtyWaterProducerDefinitionImpl;
    }

    /**
     * @return Returns the myDryWasteProducerDefinitionImpl.
     */
    protected DryWasteProducerDefinitionImpl getDryWasteProducerDefinitionImpl() {
        return myDryWasteProducerDefinitionImpl;
    }

    /**
     * @return Returns the myFoodConsumerDefinitionImpl.
     */
    protected FoodConsumerDefinitionImpl getFoodConsumerDefinitionImpl() {
        return myFoodConsumerDefinitionImpl;
    }

    /**
     * @return Returns the myGreyWaterProducerDefinitionImpl.
     */
    protected GreyWaterProducerDefinitionImpl getGreyWaterProducerDefinitionImpl() {
        return myGreyWaterProducerDefinitionImpl;
    }

    /**
     * @return Returns the myPotableWaterConsumerDefinitionImpl.
     */
    protected PotableWaterConsumerDefinitionImpl getPotableWaterConsumerDefinitionImpl() {
        return myPotableWaterConsumerDefinitionImpl;
    }
}
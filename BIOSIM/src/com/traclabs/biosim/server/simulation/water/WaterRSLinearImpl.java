package com.traclabs.biosim.server.simulation.water;

import com.traclabs.biosim.idl.framework.MalfunctionIntensity;
import com.traclabs.biosim.idl.framework.MalfunctionLength;
import com.traclabs.biosim.idl.simulation.framework.DirtyWaterConsumerDefinition;
import com.traclabs.biosim.idl.simulation.framework.DirtyWaterConsumerOperations;
import com.traclabs.biosim.idl.simulation.framework.GreyWaterConsumerDefinition;
import com.traclabs.biosim.idl.simulation.framework.GreyWaterConsumerOperations;
import com.traclabs.biosim.idl.simulation.framework.PotableWaterProducerDefinition;
import com.traclabs.biosim.idl.simulation.framework.PotableWaterProducerOperations;
import com.traclabs.biosim.idl.simulation.framework.PowerConsumerDefinition;
import com.traclabs.biosim.idl.simulation.framework.PowerConsumerOperations;
import com.traclabs.biosim.idl.simulation.water.WaterRSOperations;
import com.traclabs.biosim.server.simulation.framework.DirtyWaterConsumerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.GreyWaterConsumerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.PotableWaterProducerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.PowerConsumerDefinitionImpl;
import com.traclabs.biosim.server.simulation.framework.SimBioModuleImpl;

//import java.lang.*;

/**
 * The Water Recovery System takes grey/dirty water and refines it to potable
 * water for the crew members and grey water for the crops.. Class modeled after
 * the paper:. "Intelligent Control of a Water Recovery System: Three Years in
 * the Trenches" by Bonasso, Kortenkamp, and Thronesbery
 * 
 * @author Scott Bell
 */

public class WaterRSLinearImpl extends SimBioModuleImpl implements
        WaterRSOperations, PowerConsumerOperations,
        DirtyWaterConsumerOperations, GreyWaterConsumerOperations,
        PotableWaterProducerOperations {
    //Consumers, Producers
    private PowerConsumerDefinitionImpl myPowerConsumerDefinitionImpl;

    private GreyWaterConsumerDefinitionImpl myGreyWaterConsumerDefinitionImpl;

    private DirtyWaterConsumerDefinitionImpl myDirtyWaterConsumerDefinitionImpl;

    private PotableWaterProducerDefinitionImpl myPotableWaterProducerDefinitionImpl;

    private float currentPowerConsumed = 0f;

    private float currentWaterConsumed = 0f;

    /**
     * Creates the Water RS and it's subsystems
     */
    public WaterRSLinearImpl(int pID, String pName) {
        super(pID, pName);
        myPowerConsumerDefinitionImpl = new PowerConsumerDefinitionImpl();
        myGreyWaterConsumerDefinitionImpl = new GreyWaterConsumerDefinitionImpl();
        myDirtyWaterConsumerDefinitionImpl = new DirtyWaterConsumerDefinitionImpl();
        myPotableWaterProducerDefinitionImpl = new PotableWaterProducerDefinitionImpl();
    }

    public PowerConsumerDefinition getPowerConsumerDefinition() {
        return myPowerConsumerDefinitionImpl.getCorbaObject();
    }

    public GreyWaterConsumerDefinition getGreyWaterConsumerDefinition() {
        return myGreyWaterConsumerDefinitionImpl.getCorbaObject();
    }

    public DirtyWaterConsumerDefinition getDirtyWaterConsumerDefinition() {
        return myDirtyWaterConsumerDefinitionImpl.getCorbaObject();
    }

    public PotableWaterProducerDefinition getPotableWaterProducerDefinition() {
        return myPotableWaterProducerDefinitionImpl.getCorbaObject();
    }

    /**
     * Resets production/consumption levels and resets all the subsystems
     */
    public void reset() {
        super.reset();
    }

    private void gatherPower() {
        currentPowerConsumed = myPowerConsumerDefinitionImpl
                .getMostResourceFromStore();
    }

    private void gatherWater() {
        //1540 Watts -> 4.26 liters of water
        float waterNeeded = (currentPowerConsumed / 1540f) * 4.26f;
        float currentDirtyWaterConsumed = myDirtyWaterConsumerDefinitionImpl
                .getResourceFromStore(waterNeeded);
        float currentGreyWaterConsumed = myGreyWaterConsumerDefinitionImpl
                .getResourceFromStore(waterNeeded - currentDirtyWaterConsumed);
        currentWaterConsumed = currentDirtyWaterConsumed
                + currentGreyWaterConsumed;
    }

    /**
     * Flushes the water from this subsystem (via the WaterRS) to the Potable
     * Water Store
     */
    private void pushWater() {
        float distributedWaterLeft = myPotableWaterProducerDefinitionImpl
                .pushResourceToStore(currentWaterConsumed);
    }

    /**
     * When ticked, the Water RS: 1) gets as much water as it can in relation to
     * power
     */
    public void tick() {
        super.tick();
        gatherPower();
        gatherWater();
        pushWater();
    }

    protected void performMalfunctions() {
    }

    protected String getMalfunctionName(MalfunctionIntensity pIntensity,
            MalfunctionLength pLength) {
        return "NoName";
    }

    /**
     * For fast reference to the log tree
     */
    private class LogIndex {
    }

}
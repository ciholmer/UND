package com.traclabs.biosim.server.simulation.air;

/**
 * OGS Subsystem
 * 
 * @author Scott Bell
 */

public class OGSSubsystem extends AirRSSubSystem {
    private final static float waterNeeded = 1.5f;

    private float currentH2OConsumed = 0;

    private float currentO2Produced = 0;

    private float currentH2Produced = 0;

    private float myProductionRate = 1f;

    public OGSSubsystem(AirRSImpl pAirRSImpl) {
        super(pAirRSImpl);
    }

    public void log() {
        super.log();
        myLogger.debug("currentH2OConsumed=" + currentH2OConsumed);
        myLogger.debug("currentO2Produced=" + currentO2Produced);
        myLogger.debug("currentH2Produced=" + currentH2Produced);
        myLogger.debug("myProductionRate=" + myProductionRate);
    }

    public float getO2Produced() {
        return currentO2Produced;
    }

    private void gatherWater() {
        currentH2OConsumed = myAirRS.getPotableWaterProducerDefinitionImpl()
                .getResourceFromStore(waterNeeded);
    }

    private void pushGasses() {
        //2H20 --> 2H2 + O2
        float molesOfWater = (currentH2OConsumed * 1000f) / 18.01524f; //1000g/liter,
        // 18.01524g/mole
        float molesOfReactant = molesOfWater / 2f;
        currentO2Produced = myAirRS.randomFilter(molesOfReactant)
                * myProductionRate;
        currentH2Produced = myAirRS.randomFilter(molesOfReactant * 2f)
                * myProductionRate;
        float O2ToDistrubute = myAirRS.randomFilter(currentO2Produced);
        float H2ToDistrubute = myAirRS.randomFilter(currentH2Produced);
        float distributedO2 = myAirRS.getO2ProducerDefinitionImpl()
                .pushResourceToStore(O2ToDistrubute);
        float distributedH2 = myAirRS.getH2ProducerDefinitionImpl()
                .pushResourceToStore(H2ToDistrubute);
    }

    public void setProductionRate(float percentage) {
        myProductionRate = percentage;
    }

    public float getProductionRate() {
        return myProductionRate;
    }

    public void reset() {
        currentPowerConsumed = 0;
        hasEnoughPower = false;
        currentH2OConsumed = 0;
        currentO2Produced = 0;
        currentH2Produced = 0;
    }

    public void tick() {
        super.tick();
        if (hasEnoughPower && enabled) {
            gatherWater();
            pushGasses();
        }
    }

}
package com.traclabs.biosim.server.simulation.food;

import org.apache.log4j.Logger;

import com.traclabs.biosim.idl.simulation.food.BioMatter;
import com.traclabs.biosim.idl.simulation.food.BiomassPS;
import com.traclabs.biosim.idl.simulation.food.BiomassPSHelper;
import com.traclabs.biosim.idl.simulation.food.Plant;
import com.traclabs.biosim.idl.simulation.food.PlantHelper;
import com.traclabs.biosim.idl.simulation.food.PlantType;
import com.traclabs.biosim.idl.simulation.food.ShelfPOA;
import com.traclabs.biosim.util.OrbUtils;

/**
 * Tray contains Plants
 * 
 * @author Scott Bell
 */

public class ShelfImpl extends ShelfPOA {
    private Logger myLogger;

    private PlantImpl myCrop;

    private float cropAreaTotal = 0f;

    private float cropAreaUsed = 0f;

    private BiomassPSImpl myBiomassPSImpl;

    private float waterLevel = 0f;

    /* grab up to 50 liters per meters squared of crops per hour (WAG) */
    private static final float waterNeededPerMeterSquared = 50f;

    private float waterNeeded = 0f;

    private float powerLevel = 0f;

    /* grab 1520 watts per square meter (from table 4.2.2 in BVAD) */
    private static final float POWER_PER_SQUARE_METER = 1520f;

    private int myStartTick;
    
    //CIH 200705 - Add for plant variable agrigates
    private float myO2Produced = 0f;
    
    private float myCO2Consumed = 0f;
    
    private float myWaterProduced = 0f;
    
    private float myWaterConsumed = 0f;
    
    private float myPotableWaterConsumed = 0f;
    
    private float myGrayWaterConsumed = 0f;
    //

    public ShelfImpl(PlantType pType, float pCropAreaTotal,
            BiomassPSImpl pBiomassImpl) {
        this(pType, pCropAreaTotal, pBiomassImpl, 0);
    }

    public ShelfImpl(PlantType pType, float pCropAreaTotal,
            BiomassPSImpl pBiomassImpl, int pStartTick) {
        myLogger = Logger.getLogger(this.getClass());
        myStartTick = pStartTick;
        cropAreaTotal = pCropAreaTotal;
        myBiomassPSImpl = pBiomassImpl;
        replant(pType, cropAreaTotal);
        waterNeeded = cropAreaUsed * waterNeededPerMeterSquared;
        myLogger.debug("ShelfImpl: power needed: " + powerLevel);
        myLogger.debug("ShelfImpl: WaterNeeded: " + waterNeeded);
    }

    public Plant getPlant() {
        return PlantHelper.narrow(OrbUtils.poaToCorbaObj(myCrop));
    }

    public PlantImpl getPlantImpl() {
        return myCrop;
    }

    public void reset() {
        waterLevel = 0f;
        powerLevel = 0f;
        myO2Produced = 0f;
        myCO2Consumed = 0f;
        myWaterConsumed = 0f;
        myWaterProduced = 0f;
        myPotableWaterConsumed = 0f;
        myGrayWaterConsumed = 0f;
        myCrop.reset();
    }
    
    public float getTimeTillCanopyClosure(){
    	return myCrop.getTimeTillCanopyClosure();
    }

    public PlantType getCropType() {
        return myCrop.getPlantType();
    }

    public String getCropTypeString() {
        return myCrop.getPlantTypeString();
    }

    public BiomassPS getBiomassPS() {
        return BiomassPSHelper.narrow(OrbUtils.poaToCorbaObj(myBiomassPSImpl));
    }

    public BiomassPSImpl getBiomassPSImpl() {
        return myBiomassPSImpl;
    }

    public float getCropAreaTotal() {
        return cropAreaTotal;
    }

    public float getCropAreaUsed() {
        return cropAreaUsed;
    }

    public void setStartTick(int tick) {
        myStartTick = tick;
    }
    
    /*
     * CIH 200704
     * @Return CO2 Consumed by shelf in moles
     */
    public float getCropCO2Consumed () {
    	return myCrop.getMolesOfCO2Consumed();
    }
    
    /*
     * CIH200704
     * @Return O2 produced by shelf in moles
     */
    public float getCropO2Produced() {
    	return myCrop.getMolesofO2Produced();
    }
    
    /*
     * CIH200920
     * @return Water Consumed by plants on the shelf in ltrs 
     */
    public float getCropWaterConsumed() {
        return myCrop.getLtrWaterConsumed();
    }
    
    /*
     * CIH200920
     * @return potable water consumed by shelf in ltrs (amount of water used to fill the water level on the shelf)
     * NOT the amount of Potable Water consumed by the plant on the shelf
     */
    public float getShelfPotableWaterConsumed(){
    	return myPotableWaterConsumed;
    }
    
    /*
     * CIH200920
     * @return gray water consumed by shelf in ltrs(amount of water used to fill the water level on the shelf)
     * NOT the amount of Gray Water consumed by the plant on the shelf
     */
    public float getShelfGrayWaterConsumed(){
    	return myGrayWaterConsumed;
    }
    
    /*
     * CIH200711
     * @return water produced by shelf in ltrs (transpired as vapor)
     */
    public float getCropWaterProduced(){
    	return myCrop.getLtrWaterProduced();
    }

    private void gatherWater() {
        float extraWaterNeeded = waterNeeded - waterLevel;
        if (extraWaterNeeded < 0) {
            extraWaterNeeded = 0;
        }
        float gatheredGreyWater = myBiomassPSImpl
                .getGreyWaterConsumerDefinitionImpl()
                .getFractionalResourceFromStores(extraWaterNeeded,
                        1f / myBiomassPSImpl.getNumberOfShelves());
        float gatheredPotableWater = myBiomassPSImpl
                .getPotableWaterConsumerDefinitionImpl()
                .getFractionalResourceFromStores(
                        extraWaterNeeded - gatheredGreyWater,
                        1f / myBiomassPSImpl.getNumberOfShelves());
        waterLevel += gatheredGreyWater + gatheredPotableWater;
        //set the amount of water consumed (Note water level != water consumed
        //myWaterConsumed += waterLevel;
        myWaterConsumed += myCrop.getLtrWaterConsumed();
        //Note tells how much was put into the shelf water level, not how much was consumed by the plants
        myPotableWaterConsumed = gatheredPotableWater;
        myGrayWaterConsumed = gatheredGreyWater;
        myLogger.debug("ShelfImpl: gatherWater - myWaterConsumed: " + myWaterConsumed);
        myLogger.debug("ShelfImpl: gatherWater - PotableWaterUsed: " + gatheredPotableWater);
        myLogger.debug("ShelfImpl: gatherWater - myPotableWaterConsumed: " + myPotableWaterConsumed);
        myLogger.debug("ShelfImpl: gatherWater - GrayWaterUsed: " + gatheredGreyWater);
        myLogger.debug("ShelfImpl: gatherWater - myGrayWaterConsumed: " + myGrayWaterConsumed);
    }

    private void gatherPower() {
        float powerNeeded = POWER_PER_SQUARE_METER * getCropAreaUsed();
        myLogger.debug("ShelfImpl: power needed: " + powerLevel);
        powerLevel = myBiomassPSImpl.getPowerConsumerDefinitionImpl()
                .getFractionalResourceFromStores(powerNeeded,
                        1f / myBiomassPSImpl.getNumberOfShelves());
        myLogger.debug("ShelfImpl: power pulled from stores: " + powerLevel);
    }

    public float takeWater(float pLiters) {
        if (waterLevel < pLiters) {
            float waterTaken = waterLevel;
            waterLevel = 0;
            return waterTaken;
        }
		waterLevel -= pLiters;
		return pLiters;
    }

    private void lightPlants() {
        myLogger.debug("ShelfImpl: powerLevel: " + powerLevel);
        myLogger.debug("ShelfImpl: getLampEfficiency:" + getLampEfficiency());
        myLogger.debug("ShelfImpl: getPSEfficiency: " + getPSEfficiency());
        float powerToDeliver = Math
                .min(powerLevel, myCrop.getPPFNeeded() * getCropAreaUsed()
                        / (getLampEfficiency() * getPSEfficiency()));
        myLogger.debug("ShelfImpl: pwerToDeliver: " + powerToDeliver);
        if (powerToDeliver <= 0)
            powerToDeliver = Float.MIN_VALUE;
        float thePPF = powerToDeliver * getLampEfficiency() * getPSEfficiency()
                / getCropAreaUsed();
        myLogger.debug("ShelfImpl: thePPF: " + thePPF);
        myCrop.shine(thePPF);
    }

    private float getLampEfficiency() {
        return 261f; //for high pressure sodium bulbs
    }

    private float getPSEfficiency() {
        return 4.68f; //for high pressure sodium bulbs
    }
    
    //CIH 201014 Add logging

    public void harvest() {
    	float age = myCrop.getDaysOfGrowth();
        BioMatter biomassProduced = myCrop.harvest();
    	float inedible = biomassProduced.inedibleFraction;
    	if (Float.isNaN(inedible))
    		inedible = 0;
        myLogger.info("ShelfImpl: Harvested " + biomassProduced.mass
                + "kg of " + myCrop.getPlantTypeString() + " ("+ inedible * 100 +"% inedible) after " +age+ " days of growth on tick " + myBiomassPSImpl.getMyTicks());
        myBiomassPSImpl.getBiomassProducerDefinitionImpl()
                .pushFractionalResourceToBiomassStore(biomassProduced,
                        1f / myBiomassPSImpl.getNumberOfShelves());
    }
   
    public boolean isReadyForHavest() {
        return myCrop.readyForHarvest();
    }

    public boolean isDead() {
        return myCrop.isDead();
    }

    public float getHarvestInterval() {
        return myCrop.getTimeAtCropMaturity();
    }
    
    private void tryHarvesting() {
    	float age = myCrop.getDaysOfGrowth();
    	if (myBiomassPSImpl.autoHarvestAndReplantEnabled()) {
            if (myCrop.readyForHarvest() || myCrop.isDead()) {
            	BioMatter biomassProduced = myCrop.harvest();
            	float inedible = biomassProduced.inedibleFraction;
            	if (Float.isNaN(inedible))
            		inedible = 0;
                myLogger.info("ShelfImpl: Harvested " + biomassProduced.mass
                        + "kg of " + myCrop.getPlantTypeString() + " ("+ inedible * 100 +"% inedible) after " +age+ " days of growth on tick " + myBiomassPSImpl.getMyTicks());
                myBiomassPSImpl
                        .getBiomassProducerDefinitionImpl()
                        .pushFractionalResourceToBiomassStore(biomassProduced,
                                1f / myBiomassPSImpl.getNumberOfShelves());
                myCrop.reset();
            }
        } 
    }

    public void tick() {
        if (cropAreaUsed > 0 && (myBiomassPSImpl.getMyTicks() >= myStartTick)) {
            tryHarvesting();
            gatherPower();
            gatherWater();
            lightPlants();
            myCrop.tick();
        }
    }

    public void replant(PlantType pType) {
        replant(pType, cropAreaTotal);
    }

    public void replant(PlantType pType, float pArea) {
        if (pArea > cropAreaTotal)
            cropAreaUsed = cropAreaTotal;
        else
            cropAreaUsed = pArea;
        
    	if ((myCrop != null) && (pType == myCrop.getPlantType()))
        	myCrop.reset();
    	else if (pType == PlantType.DRY_BEAN)
            myCrop = new DryBean(this);
        else if (pType == PlantType.LETTUCE)
            myCrop = new Lettuce(this);
        else if (pType == PlantType.PEANUT)
            myCrop = new Peanut(this);
        else if (pType == PlantType.SOYBEAN)
            myCrop = new Soybean(this);
        else if (pType == PlantType.RICE)
            myCrop = new Rice(this);
        else if (pType == PlantType.SWEET_POTATO)
            myCrop = new SweetPotato(this);
        else if (pType == PlantType.TOMATO)
            myCrop = new Tomato(this);
        else if (pType == PlantType.WHEAT)
            myCrop = new Wheat(this);
        else if (pType == PlantType.WHITE_POTATO)
            myCrop = new WhitePotato(this);
        waterNeeded = cropAreaUsed * waterNeededPerMeterSquared;
    }
    
    public void kill(){
    	myCrop.kill();
    }
    
}
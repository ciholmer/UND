package com.traclabs.biosim.client.control;

import java.util.Map;

import org.apache.log4j.Logger;

import com.traclabs.biosim.client.util.BioHolder;
import com.traclabs.biosim.client.util.BioHolderInitializer;
import com.traclabs.biosim.idl.actuator.framework.GenericActuator;
import com.traclabs.biosim.idl.simulation.air.OGS;
import com.traclabs.biosim.idl.simulation.water.WaterRS;

public class ActionMap {
    private BioHolder myBioHolder;

    private Logger myLogger;

    private boolean myWaterRSConsumeDirtyWater = false;

    private boolean myWaterRSConsumeGreyWater = false;

    private boolean myOGSProduceO2 = false;
    
    private static final Integer HIGH = new Integer(0);
    private static final Integer LOW = new Integer(1);

    private GenericActuator myOGSPotableWaterInFlowRateActuator;
    private GenericActuator myOGSPowerActuator;
    
    private GenericActuator myWaterRSDirtyWaterInFlowRateActuator;
    private GenericActuator myWaterRSGreyWaterInFlowRateActuator;
    private GenericActuator myWaterRSPowerActuator;

    public ActionMap() {
        myLogger = Logger.getLogger(this.getClass());
        
        myBioHolder = BioHolderInitializer.getBioHolder();

        WaterRS myWaterRS = (WaterRS) myBioHolder.theWaterRSModules.get(0);
        OGS myOGS = (OGS) myBioHolder.theOGSModules.get(0);
        
        myOGSPotableWaterInFlowRateActuator = myBioHolder.getActuatorAttachedTo(
                myBioHolder.thePotableWaterInFlowRateActuators, myOGS);
        myOGSPowerActuator = myBioHolder.getActuatorAttachedTo(
                myBioHolder.thePowerInFlowRateActuators, myOGS);
        
        myWaterRSDirtyWaterInFlowRateActuator = myBioHolder.getActuatorAttachedTo(
                myBioHolder.theDirtyWaterInFlowRateActuators, myWaterRS);
        myWaterRSGreyWaterInFlowRateActuator = myBioHolder.getActuatorAttachedTo(
                myBioHolder.theGreyWaterInFlowRateActuators, myWaterRS);
        myWaterRSPowerActuator = myBioHolder.getActuatorAttachedTo(
                myBioHolder.thePowerInFlowRateActuators, myWaterRS);

    }

    public void printMe() {
    }

    /**
     * @param classifiedState
     */
    public void performAction(Map classifiedState) {
        if (classifiedState.get("potablewater") == HandController.LOW) {
            myLogger.debug("potablewater is LOW! ");
            myWaterRSConsumeDirtyWater = true;
        } else if (classifiedState.get("dirtywater") == HandController.HIGH
                && classifiedState.get("potablewater") != HandController.HIGH) {
            myWaterRSConsumeDirtyWater = true;
        } else {
            myWaterRSConsumeDirtyWater = false;
        }
        if (classifiedState.get("potablewater") == HandController.LOW
                && classifiedState.get("greywater") != HandController.LOW) {
            myWaterRSConsumeGreyWater = true;
        } else if (classifiedState.get("greywater") == HandController.HIGH
                && classifiedState.get("potablewater") != HandController.HIGH) {
            myWaterRSConsumeGreyWater = true;
        } else {
            myWaterRSConsumeGreyWater = false;
        }
        if (classifiedState.get("oxygen") == HandController.LOW) {
            myLogger.debug("oxygen is LOW! ");
            myOGSProduceO2 = true;
        }
        if (classifiedState.get("oxygen") == HandController.HIGH) {
            myOGSProduceO2 = false;
        }
        
        myLogger.debug("myWaterRSConsumeDirtyWater "+myWaterRSConsumeDirtyWater);
        myLogger.debug("myWaterRSConsumeGreyWater "+myWaterRSConsumeGreyWater);
        myLogger.debug("myOGSProduceO2 "+myOGSProduceO2);
        
        //OGS
        if (myOGSProduceO2){
            myOGSPotableWaterInFlowRateActuator.setValue(myOGSPotableWaterInFlowRateActuator.getMax());
            myOGSPowerActuator.setValue(myOGSPowerActuator.getMax());
        }
        else{
            myOGSPotableWaterInFlowRateActuator.setValue(myOGSPotableWaterInFlowRateActuator.getMin());
            myOGSPowerActuator.setValue(myOGSPowerActuator.getMin());
        }
        
        //WaterRS
        if (myWaterRSConsumeDirtyWater || myWaterRSConsumeGreyWater)
            myWaterRSPowerActuator.setValue(myWaterRSPowerActuator.getMax());
        else
            myWaterRSPowerActuator.setValue(myWaterRSPowerActuator.getMin());
        if (myWaterRSConsumeDirtyWater)
            myWaterRSDirtyWaterInFlowRateActuator.setValue(myWaterRSDirtyWaterInFlowRateActuator.getMax());
        else
            myWaterRSDirtyWaterInFlowRateActuator.setValue(myWaterRSDirtyWaterInFlowRateActuator.getMin());
        if (myWaterRSConsumeGreyWater)
            myWaterRSGreyWaterInFlowRateActuator.setValue(myWaterRSGreyWaterInFlowRateActuator.getMax());
        else
            myWaterRSGreyWaterInFlowRateActuator.setValue(myWaterRSGreyWaterInFlowRateActuator.getMin());
    }


}
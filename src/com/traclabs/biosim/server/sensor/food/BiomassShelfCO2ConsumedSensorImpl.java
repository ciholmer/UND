package com.traclabs.biosim.server.sensor.food;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.food.BiomassShelfCO2ConsumedSensorOperations;

public class BiomassShelfCO2ConsumedSensorImpl extends ShelfSensorImpl implements
        BiomassShelfCO2ConsumedSensorOperations {
    public BiomassShelfCO2ConsumedSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getCropCO2Consumed();
        myValue = getStochasticFilter().randomFilter(preFilteredValue);
    }
    
    /** CIH 200809
     * getMax() - Required for GenericSenorImpl implementation.
     * No Max for this sensor, returns the largest float value possible
     */
    public float getMax() {
        return Float.MAX_VALUE;
    }

    protected void notifyListeners() {
        //does nothing now
    }


}
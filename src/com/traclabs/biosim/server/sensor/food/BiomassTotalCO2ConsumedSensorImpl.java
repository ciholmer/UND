package com.traclabs.biosim.server.sensor.food;

import com.traclabs.biosim.idl.sensor.food.BiomassTotalCO2ConsumedSensorOperations;

public class BiomassTotalCO2ConsumedSensorImpl extends BiomassSensorImpl implements
        BiomassTotalCO2ConsumedSensorOperations {
    public BiomassTotalCO2ConsumedSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getTotalCO2Consumed();
        myValue = getStochasticFilter().randomFilter(preFilteredValue);
    }

    public float getMax() {
        return Float.MAX_VALUE;
    }

    protected void notifyListeners() {
        //does nothing now
    }

}
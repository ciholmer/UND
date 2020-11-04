package com.traclabs.biosim.server.sensor.food;

import com.traclabs.biosim.idl.sensor.food.BiomassTotalWaterProducedSensorOperations;


public class BiomassTotalWaterProducedSensorImpl extends BiomassSensorImpl implements
BiomassTotalWaterProducedSensorOperations {
public BiomassTotalWaterProducedSensorImpl(int pID, String pName) {
super(pID, pName);
}

protected void gatherData() {
float preFilteredValue = getInput().getTotalWaterProduced();
myValue = getStochasticFilter().randomFilter(preFilteredValue);
}

public float getMax() {
return Float.MAX_VALUE;
}

protected void notifyListeners() {
//does nothing now
}

}
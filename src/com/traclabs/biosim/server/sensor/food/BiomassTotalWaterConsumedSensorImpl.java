package com.traclabs.biosim.server.sensor.food;

import com.traclabs.biosim.idl.sensor.food.BiomassTotalWaterConsumedSensorOperations;


public class BiomassTotalWaterConsumedSensorImpl extends BiomassSensorImpl implements
BiomassTotalWaterConsumedSensorOperations {
public BiomassTotalWaterConsumedSensorImpl(int pID, String pName) {
super(pID, pName);
}

protected void gatherData() {
float preFilteredValue = getInput().getTotalWaterConsumed();
myValue = getStochasticFilter().randomFilter(preFilteredValue);
}

public float getMax() {
return Float.MAX_VALUE;
}

protected void notifyListeners() {
//does nothing now
}

}
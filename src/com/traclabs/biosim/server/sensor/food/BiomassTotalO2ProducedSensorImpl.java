package com.traclabs.biosim.server.sensor.food;

import com.traclabs.biosim.idl.sensor.food.BiomassTotalO2ProducedSensorOperations;

public class BiomassTotalO2ProducedSensorImpl extends BiomassSensorImpl implements
BiomassTotalO2ProducedSensorOperations {
public BiomassTotalO2ProducedSensorImpl(int pID, String pName) {
super(pID, pName);
}

protected void gatherData() {
float preFilteredValue = getInput().getTotalO2Produced();
myValue = getStochasticFilter().randomFilter(preFilteredValue);
}

public float getMax() {
return Float.MAX_VALUE;
}

protected void notifyListeners() {
//does nothing now
}

}
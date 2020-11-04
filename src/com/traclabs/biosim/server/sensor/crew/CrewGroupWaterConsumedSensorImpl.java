package com.traclabs.biosim.server.sensor.crew;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.crew.CrewGroupWaterConsumedSensorOperations;

/*
 * Collects all of the Water consumed by an entire crew group during a tick for monitoring stability.
 * Based on CrewGroupProductivitySensorImpl
 * 
 * @author Curt Holmer May 2020
 */
public class CrewGroupWaterConsumedSensorImpl extends CrewGroupSensorImpl
        implements CrewGroupWaterConsumedSensorOperations {
    public CrewGroupWaterConsumedSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getPotableWaterConsumed();
        myValue = getStochasticFilter().randomFilter(preFilteredValue);
    }

    public float getMax() {
        return Float.MAX_VALUE;
    }

    protected void notifyListeners() {
    }

    public BioModule getInputModule() {
        return getInput();
    }
}
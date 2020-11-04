package com.traclabs.biosim.server.sensor.crew;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.crew.CrewGroupFoodConsumedSensorOperations;

/*
 * Collects all of the Food consumed by an entire crew group during a tick for monitoring stability.
 * Based on CrewGroupProductivitySensorImpl
 * 
 * @author Curt Holmer May 2020
 */
public class CrewGroupFoodConsumedSensorImpl extends CrewGroupSensorImpl
        implements CrewGroupFoodConsumedSensorOperations {
    public CrewGroupFoodConsumedSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getFoodConsumed();
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
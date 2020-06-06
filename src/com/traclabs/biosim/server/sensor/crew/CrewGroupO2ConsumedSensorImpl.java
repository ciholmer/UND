package com.traclabs.biosim.server.sensor.crew;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.crew.CrewGroupO2ConsumedSensorOperations;

/*
 * Collects all of the O2 consumed by an entire crew group during a tick for monitoring stability.
 * Based on CrewGroupProductivitySensorImpl
 * 
 * @author Curt Holmer May 2019
 */
public class CrewGroupO2ConsumedSensorImpl extends CrewGroupSensorImpl
        implements CrewGroupO2ConsumedSensorOperations {
    public CrewGroupO2ConsumedSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getO2Consumed();
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
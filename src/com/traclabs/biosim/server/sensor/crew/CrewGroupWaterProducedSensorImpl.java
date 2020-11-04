package com.traclabs.biosim.server.sensor.crew;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.crew.CrewGroupWaterProducedSensorOperations;

/*
 * Collects all of the Water Produced by an entire crew group during a tick for monitoring stability.
 * Includes both 'Dirty' and Grey
 * Based on CrewGroupProductivitySensorImpl
 * 
 * @author Curt Holmer May 2020
 */

public class CrewGroupWaterProducedSensorImpl extends CrewGroupSensorImpl
        implements CrewGroupWaterProducedSensorOperations {
    public CrewGroupWaterProducedSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getDirtyWaterProduced();
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
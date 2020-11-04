package com.traclabs.biosim.server.sensor.crew;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.crew.CrewGroupWasteProducedSensorOperations;

/*
 * Collects all of the Waste Produced by an entire crew group during a tick for monitoring stability.
 * Based on CrewGroupProductivitySensorImpl
 * 
 * @author Curt Holmer May 2020
 */
public class CrewGroupWasteProducedSensorImpl extends CrewGroupSensorImpl
        implements CrewGroupWasteProducedSensorOperations {
    public CrewGroupWasteProducedSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getDryWasteProduced();
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
package com.traclabs.biosim.server.sensor.crew;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.crew.CrewGroupCO2ProducedSensorOperations;

/*
 * Collects all of the CO2 Produced by an entire crew group during a tick for monitoring stability.
 * Based on CrewGroupProductivitySensorImpl
 * 
 * @author Curt Holmer May 2020
 */
public class CrewGroupCO2ProducedSensorImpl extends CrewGroupSensorImpl
        implements CrewGroupCO2ProducedSensorOperations {
    public CrewGroupCO2ProducedSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getCO2Produced();
        myValue = getStochasticFilter().randomFilter(preFilteredValue);
    }

    public float getMax() {
        return Float.MAX_VALUE;
    }

    protected void notifyListeners() {
    	//Does Nothing Now
    }

    public BioModule getInputModule() {
        return getInput();
    }
}
package com.traclabs.biosim.server.sensor.air;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.air.O2OutFlowRateSensorOperations;
import com.traclabs.biosim.idl.simulation.framework.O2Producer;
import com.traclabs.biosim.server.sensor.framework.GenericSensorImpl;

public class O2OutFlowRateSensorImpl extends GenericSensorImpl implements
        O2OutFlowRateSensorOperations {
    private O2Producer myProducer;

    private int myIndex;

    public O2OutFlowRateSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getO2ProducerDefinition().getActualFlowRate(myIndex);
        myValue = randomFilter(preFilteredValue);
    }

    protected void notifyListeners() {
        //does nothing right now
    }

    public void setInput(O2Producer pProducer, int pIndex) {
        myProducer = pProducer;
        myIndex = pIndex;
    }

    public float getMax() {
        return myProducer.getO2ProducerDefinition().getMaxFlowRate(myIndex);
    }

    public O2Producer getInput() {
        return myProducer;
    }

    public BioModule getInputModule() {
        return (BioModule) (myProducer);
    }

    public int getIndex() {
        return myIndex;
    }

}
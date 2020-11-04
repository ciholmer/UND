package com.traclabs.biosim.server.actuator.food;

import com.traclabs.biosim.idl.actuator.food.TankActuatorOperations;
import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.simulation.food.AquaculturePS;
import com.traclabs.biosim.idl.simulation.food.Tank;
import com.traclabs.biosim.server.actuator.framework.GenericActuatorImpl;

public abstract class TankActuatorImpl extends GenericActuatorImpl implements
        TankActuatorOperations {
    protected Tank myTank;

    private AquaculturePS myModule;

    public TankActuatorImpl(int pID, String pName) {
        super(pID, pName);
    }

    public void setOutput(AquaculturePS pAquaculturePS, int tankIndex) {
        myTank = pAquaculturePS.getTank(tankIndex);
        myModule = pAquaculturePS;
        myValue = tankIndex;
    }

    public Tank getOutput() {
        return myTank;
    }

    public BioModule getOutputModule() {
        return (myModule);
    }
}
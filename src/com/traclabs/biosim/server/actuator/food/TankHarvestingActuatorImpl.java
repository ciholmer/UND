package com.traclabs.biosim.server.actuator.food;

import com.traclabs.biosim.idl.actuator.food.TankHarvestingActuatorOperations;

public class TankHarvestingActuatorImpl extends TankActuatorImpl implements
        TankHarvestingActuatorOperations {
    public TankHarvestingActuatorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void processData() {
        //harvest crops
        myTank.harvest();
    }



    public float getMax() {
        return 1f;
    }
}
package com.traclabs.biosim.server.actuator.food;

import com.traclabs.biosim.idl.actuator.food.SpawningActuatorOperations;
import com.traclabs.biosim.idl.simulation.food.FishType;

public class SpawningActuatorImpl extends TankActuatorImpl implements
        SpawningActuatorOperations {
    private FishType myType = FishType.UNKNOWN_FISH;

    public SpawningActuatorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void processData() {
        float myFilteredValue = getStochasticFilter().randomFilter(myValue);
        //respawn fish
        myTank.respawn(myType, myFilteredValue);
    }



    public float getMax() {
        return myTank.getTankVolTotal();
    }

    public void setFishType(FishType pType) {
        myType = pType;
        newValueSet = true;
    }
}
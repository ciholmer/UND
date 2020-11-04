package com.traclabs.biosim.server.sensor.food;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.simulation.food.BiomassPS;
import com.traclabs.biosim.idl.simulation.food.BiomassPSOperations;
import com.traclabs.biosim.idl.sensor.food.BiomassSensorOperations;
import com.traclabs.biosim.server.sensor.framework.GenericSensorImpl;

public abstract class BiomassSensorImpl extends GenericSensorImpl implements
	BiomassSensorOperations {


    private BiomassPS myBiomassPS;

    public BiomassSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected abstract void gatherData();

    protected abstract void notifyListeners();

    public void setInput(BiomassPS pBiomassPS, int shelfIndex) {
        myBiomassPS = pBiomassPS;
    }
    
    public void setInput(BiomassPS pBiomassPS) {
        myBiomassPS = pBiomassPS;
    }

    public BiomassPS getInput() {
        return myBiomassPS;
    }

    public BioModule getInputModule() {
        return (myBiomassPS);
    }
}
package com.traclabs.biosim.server.sensor.environment;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.environment.GasMoleSensorOperations;
import com.traclabs.biosim.idl.simulation.environment.EnvironmentStore;
import com.traclabs.biosim.idl.simulation.environment.SimEnvironment;
import com.traclabs.biosim.server.sensor.framework.GenericSensorImpl;


/**
 * Returns the moles of gas 
 * 
 * @author CIHolmer
 */

public class GasMoleSensorImpl extends GenericSensorImpl implements GasMoleSensorOperations {
    protected SimEnvironment myEnvironment;
    private EnvironmentStore myEnvironmentStore;

    public GasMoleSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData(){
        float molesOfGas = getGas().getCurrentLevel();
        float preFilteredValue = molesOfGas;
        myValue = getStochasticFilter().randomFilter(preFilteredValue);
        
    }
    
	@Override
	public float getMax() {
		return 1;
	}

	public void setInput(SimEnvironment environment, EnvironmentStore gas) {
		myEnvironment = environment;
		myEnvironmentStore = gas;
	}

	public SimEnvironment getEnvironment() {
		return myEnvironment;
	}

	public EnvironmentStore getGas() {
		return myEnvironmentStore;
	}

	@Override
	public BioModule getInputModule() {
		return myEnvironmentStore;
	}
}
package com.traclabs.biosim.server.simulation.framework;

import com.traclabs.biosim.idl.simulation.air.CO2Store;
import com.traclabs.biosim.idl.simulation.framework.CO2ConsumerDefinition;
import com.traclabs.biosim.idl.simulation.framework.CO2ConsumerDefinitionHelper;
import com.traclabs.biosim.idl.simulation.framework.CO2ConsumerDefinitionOperations;
import com.traclabs.biosim.idl.simulation.framework.CO2ConsumerDefinitionPOATie;
import com.traclabs.biosim.server.util.OrbUtils;

/**
 * @author Scott Bell
 */

public class CO2ConsumerDefinitionImpl extends StoreFlowRateControllableImpl
        implements CO2ConsumerDefinitionOperations {
    private CO2ConsumerDefinition myCO2ConsumerDefinition;

    public CO2ConsumerDefinitionImpl() {
        myCO2ConsumerDefinition = CO2ConsumerDefinitionHelper.narrow(OrbUtils
                .poaToCorbaObj(new CO2ConsumerDefinitionPOATie(this)));
    }

    public CO2ConsumerDefinition getCorbaObject() {
        return myCO2ConsumerDefinition;
    }

    public void setCO2Inputs(CO2Store[] pStores, float[] pMaxFlowRates,
            float[] pDesiredFlowRates) {
        setStores(pStores);
        setMaxFlowRates(pMaxFlowRates);
        setDesiredFlowRates(pDesiredFlowRates);
    }
}
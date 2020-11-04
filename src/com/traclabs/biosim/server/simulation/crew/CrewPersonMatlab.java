package com.traclabs.biosim.server.simulation.crew;

import com.traclabs.biosim.idl.simulation.crew.CrewGroup;
import com.traclabs.biosim.idl.simulation.crew.Sex;

public class CrewPersonMatlab extends BaseCrewPersonImpl {
	public CrewPersonMatlab(String name, float age, float weight, Sex sex,
			int arrivalTick, int departureTick, CrewGroup crewGroup,
			Schedule schedule) {
		super(name, age, weight, sex, arrivalTick, departureTick, crewGroup, schedule);
	}

	/**
	 * Take and give resources from stores. See CrewPersonImpl's implementation as a guide.
	 */
	@Override
	protected void consumeAndProduceResources() {
		// TODO
		myLogger.info("Consuming and producing inside MatLab");
	}
	
    /**
	 * Hurt the crew if enough resources haven't been consumed
	 * Heal the crew if enough resources have been consumed
	 */
	@Override
	protected void afflictAndRecover() {
		// TODO
	}
	
	/**
	 * CIH 200718 Copied from CrewPersonImpl to resolve impmentation error
	 * CIH 200209 Add to zero crew member values when they depart before the end of the sim
	 */
	
	public void crewDepart(){
		myLogger.info(getName() + " is not in the simulation");
		reset();
		setCurrentActivity(getActivityByName("absent"));
	}
	
}

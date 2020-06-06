package com.traclabs.biosim.framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.traclabs.biosim.client.control.ActionMap;
import com.traclabs.biosim.client.control.StateMap;
import com.traclabs.biosim.client.framework.BiosimMain;
import com.traclabs.biosim.client.util.BioHolder;
import com.traclabs.biosim.client.util.BioHolderInitializer;
import com.traclabs.biosim.idl.actuator.framework.GenericActuator;
import com.traclabs.biosim.idl.actuator.air.O2InFlowRateActuator;
import com.traclabs.biosim.idl.actuator.air.O2OutFlowRateActuator;
import com.traclabs.biosim.idl.actuator.air.CO2InFlowRateActuator;
import com.traclabs.biosim.idl.actuator.air.CO2OutFlowRateActuator;
import com.traclabs.biosim.idl.framework.BioDriver;
import com.traclabs.biosim.idl.sensor.framework.GenericSensor;
import com.traclabs.biosim.idl.sensor.framework.StoreLevelSensor;
import com.traclabs.biosim.idl.simulation.environment.Dehumidifier;
import com.traclabs.biosim.idl.simulation.framework.Store;
import com.traclabs.biosim.idl.simulation.air.O2Store;
import com.traclabs.biosim.idl.simulation.air.CO2Store;
import com.traclabs.biosim.idl.simulation.air.H2Store;
import com.traclabs.biosim.idl.simulation.air.NitrogenStore;
import com.traclabs.biosim.idl.simulation.air.MethaneStore;
import com.traclabs.biosim.idl.simulation.air.VCCR;
import com.traclabs.biosim.idl.simulation.air.OGS;
import com.traclabs.biosim.idl.simulation.air.CRS;
import com.traclabs.biosim.idl.simulation.air.Pyrolizer;
import com.traclabs.biosim.idl.simulation.waste.DryWasteStore;
import com.traclabs.biosim.idl.simulation.waste.Incinerator;
import com.traclabs.biosim.idl.framework.LogLevel;
import com.traclabs.biosim.idl.framework.MalfunctionIntensity;
import com.traclabs.biosim.idl.framework.MalfunctionLength;
import com.traclabs.biosim.idl.simulation.environment.SimEnvironment;
import com.traclabs.biosim.idl.simulation.framework.Injector;
import com.traclabs.biosim.idl.simulation.framework.Accumulator;
import com.traclabs.biosim.idl.simulation.water.WaterRS;
import com.traclabs.biosim.idl.simulation.water.DirtyWaterStore;
import com.traclabs.biosim.idl.simulation.water.GreyWaterStore;
import com.traclabs.biosim.idl.simulation.water.PotableWaterStore;
import com.traclabs.biosim.server.framework.BiosimServer;
import com.traclabs.biosim.util.OrbUtils;

/**
 * A standalone BioSim instance (server, nameserver, client in one)
 * 
 * @author Scott Bell
 * @auther Curt Holmer - Modify standalone code to integrate HandController
 * Code from https://github.com/scottbell/biosim/blob/develop/src/com/traclabs/biosim/client/control/HandController.java
 * and Rice Controllers:
 * https://github.com/scottbell/reinforcement-learning-biosim/blob/master/src/edu/rice/biosim/RL/disaster/HandController.java
 * https://github.com/scottbell/reinforcement-learning-biosim/blob/master/src/edu/rice/biosim/RL/disaster/MixedController.java
 * 
 * Modified Code to mimic external simulation controls during actual LMLSTP Phase 3 experiment as documented in
 * Lunar-Mars Life Support Test Project: Phase III Final Report
 * Doc. Number JSC-39144 Date 2-23-2000
 * JSC# 39144
 * 
 * Test dration from wheat growth results (pg 58)
 * Human occupation from day 60 to day 150 (pg 58)
 *  - Air - regenerative biological and physicochemical
	        75% Physicochemical Atmosphereic Regeneration (OGS / VCCR / CRS)(section 2.1.4 users manaual)
	           GARDEN experiment in Crew Quarters
	           .22 m2 growing area (Barta 2016, pg 20, Isolation pg 264, LMLSTP III Final, pg 14 )
	           * set to 1 m of growth space, based on Phase I experience 
	           harvested every 10 days (Barta 2016, pg 20, Isolation, pg 264, LMLSTP III Final, pg 79)
	           Atmospheric parameter for Crew Quarters 
	             CO2 0.2% to 0.65% LMLSTP III Final, pg 93-94
	             O2 20.5% to 21.6% LMLSTP III Final, pg 101-102
	           4BMS ran day 0-47 and day 57 - 91 LMLSTP III Final, pg 94
	           WSCR ran day 39-40 and day 47-57 LMLSTP III Final, pg 94
	           CRS ran 75 days out of 91 LMLSTP III Final, pg 99
	           OGS ran the entire 91 day test, LMLSTP III Final, pg 102
	        25% VPGC (Variable Pressure Growth Chamber) (Barta 2016) 95% of O2 cycled back to Crew Compartment (LMLSTP III Final, pg 47)
	     	   11.2 m of wheat used for Atmospheric regeneration and crew consumption (5% of calories)
	     	   * set to 44.8 m of growth space per Phase 1 experience
	     	   25% harvest rotation starting on day 20 (LMLSTP III Final, pg 58)
	     	   Atmospheric parameters for VPGC 
	     	     CO2 1200 PPM
                 O2 21.5 to 21.6%
	     	set to 22.4 m of growth space per Phase 1 experience
	     900 m3 crew space + 27 m3 plant growth space (+ 9.5m3 airlock) = 936.5 m3 of environment volume
	     Water Recovery System 
	       8 days of water were cycled 10 times through crew chamber LMLSTP III Final, pg 47)
	     Waste Management System - Incineration and Biodegradation
	       Fecal process was done every 4 days starting on day 4 (overall day 54 of test) LMLSTP III Final, pg 87
	        - Average processing time (Burn) was 4 hours - LMLSTP III Final, pg 87
	       PGT not operational during first 3 weeks of test (LMLSTP III Final, pg 90)
 * 
 */

public class BiosimStandaloneLMLSTP3Controller {

	private Thread myServerThread;

	private Thread myClientThread;

	private ReadyListener myReadyListener;

	private JFrame myFrame;

	private JProgressBar myProgressBar;

	private String myXmlFilename;

	private int myDriverPause;
	
	//Hand Controller Values LMLSTP3
	
	private Dehumidifier crewDehumidifier;
	private Dehumidifier vpgcDehumidifier;
	
	private GenericSensor crewHumiditySensor;
	private GenericSensor vpgcHumiditySensor;
	
	private GenericSensor crewO2ConcentrationSensor;
	private GenericSensor vpgcO2ConcentrationSensor;
	
	private GenericSensor crewCO2ConcentrationSensor;
	private GenericSensor vpgcCO2ConcentrationSensor;
	
	private Accumulator crewO2Concentrator;
	private Accumulator vpgcO2Concentrator;

	private O2InFlowRateActuator crewO2InjectorActuator;
	private O2OutFlowRateActuator crewO2StorageActuator;
	private O2OutFlowRateActuator crewO2ConcentratorActuator;
	private O2InFlowRateActuator crewO2ConcentratorStoreActuator;
	
	private CO2InFlowRateActuator crewCO2StorageActuator;
	private CO2OutFlowRateActuator crewCO2InjectorActuator;
	
	private O2InFlowRateActuator vpgcO2InjectorActuator;
	private O2OutFlowRateActuator vpgcO2StorageActuator;
	private O2OutFlowRateActuator vpgcO2ConcentratorActuator;
	private O2InFlowRateActuator vpgcO2ConcentratorStoreActuator;
	
	private CO2InFlowRateActuator vpgcCO2StorageActuator;
	private CO2OutFlowRateActuator vpgcCO2InjectorActuator;
	
	//private GenericActuator myCO2InjectorActuator;
	
	private float myCO2PPMV;
	
	
    // hand controller stuff;
	
	/**
	 * Values to control for Crew Chamber
	 * 40% Humidity Isolation, pg 283
	 * CO2 0.2% to 0.65% LMLSTP III Final, pg 93-94
	 * O2 20.5% to 21.6% LMLSTP III Final, pg 101-102
	 */
	private double envCrewO2TargetPer = 0.210;
	private double envCrewO2HIGH = 0.216;
	private double envCrewO2LOW = 0.205;
	private float envCrewO2Inject = 100; //the amount to increase the injector by when adding O2 from stores

	private float envCO2TargetPPM = 1251;  //From Phase 1
	private float envCO2HIGH = 9000; //From Phase 1
	private float envCO2LOW = 1200; //rom Phase 1
	private double envCrewCO2TargetPer = 0.003;
	private double envCrewCO2HIGH = 0.0065;
	private double envCrewCO2LOW = 0.002;
	private float envVPGCCO2Inject = 20; // Amount to increment the injector by when adding CO2 to the VPGC env
	private float envCrewCO2Inject = 20; // Amount to increment the injector by when adding CO2 to the Crew env
	
	private double envCrewHumidTargetPer = 0.4;
	private double envCrewHumidHIGH = 0.41;
	private double envCrewHumidLOW = 0.39;
	
	/*Values to control for in VPGC
	 * 70% Humidity
	 * CO2 1200 PPM
	 * O2 21.5 to 21.6%
	 */
	private double envVPGCO2TargetPer = 0.215;
	private double envVPGCO2HIGH = 0.216;
	private double envVPGCO2LOW = 0.214;

	private double envVPGCCO2TargetPer = 0.125;
	private float envVPGCCO2TargetPPM = 1251;  
	private float envVPGCCO2HIGH = 1300;
	private float envVPGCCO2LOW = 1200;
	 
	private double envVPGCHumidTargetPer = 0.7;
	private double envVPGCHumidHIGH = 0.71;
	private double envVPGCHumidLOW = 0.69;
	
	//States and Storage
    private StateMap continuousState;
    private ActionMap myActionMap;
    private Map classifiedState;
    private Map<String, Map> thresholdMap = new TreeMap<String, Map>();

    //Top level containers
    private BioDriver myBioDriver;
    private BioHolder myBioHolder;
    
    //Environments
    private SimEnvironment crewEnvironment;
    private SimEnvironment vpgcEnvironment;

    //Stores
    private DirtyWaterStore myDirtyWaterStore;
    private GreyWaterStore myGreyWaterStore;
    private PotableWaterStore myPotableWaterStore;
    private O2Store myO2Store;
    private CO2Store myCO2Store;
    private H2Store myH2Store;
    private MethaneStore myCH4Store;
    private NitrogenStore myN2Store;
    private DryWasteStore myDryWasteStore;

    //Support equipment
    private VCCR myVCCR;
    private OGS myOGS;
    private CRS myCRS;
    private WaterRS myWSCR;
    private Pyrolizer myPyrolizer;
    private Incinerator myIncinerator;
    
    //Handconroller variables
    private static int ATMOSPHERIC_PERIOD = 2;

    private static int CORE_PERIOD_MULT = 5;

    public static String[] stateNames = { "dirtywater", "greywater",
            "potablewater", "oxygen" };

    public static String[] actuatorNames = { "OGSpotable", "waterRSdirty",
            "waterRSgrey" };

    private Logger myLogger;

    public static final Integer HIGH = new Integer(0);

    public static final Integer LOW = new Integer(1);

    public static final Integer NORMAL = new Integer(2);

   
	public static void main(String args[]) {
		String filename = "default.biosim";
		if (args.length > 0) {
			filename = BiosimMain.getArgumentValue(args[0]);
		}
    	System.out.println("Class path is: " + System.getProperty("java.class.path"));
    	System.out.println("Using  file: " + filename);

    	
		ImageIcon moon = new ImageIcon(BiosimStandaloneLMLSTP3Controller.class.getClassLoader()
				.getResource("com/traclabs/biosim/framework/moon.png"));
		BiosimStandaloneLMLSTP3Controller myBiosimStandalone = new BiosimStandaloneLMLSTP3Controller(moon,
				"BioSim: Advanced Life Support Simulation", filename, 500);
    	//Hand Controller merge insert code
		System.out.println("Main complete; starting simulation for BiosimStandaloneLMLSTPController");
		myBiosimStandalone.beginSimulation();
	}

	public BiosimStandaloneLMLSTP3Controller(String xmlFilename, int driverPause) {
		this.myDriverPause = driverPause;
		this.myXmlFilename = xmlFilename;
		this.myServerThread = new Thread(new ServerThread());
		this.myClientThread = new Thread(new ClientThread());
		
	}

	public BiosimStandaloneLMLSTP3Controller(ImageIcon splashIcon, String splashText,
			String xmlFilename, int driverPause) {
		this(xmlFilename, driverPause);
		startSpash(splashIcon, splashText);
	}
	
	private void startSpash(ImageIcon splashIcon, String splashText) {
		myProgressBar = new JProgressBar();
		myProgressBar.setIndeterminate(true);
		myFrame = new JFrame("BioSim Loader");
		myFrame.getContentPane().setLayout(new BorderLayout());
		ImageIcon biosimIcon = new ImageIcon(BiosimStandaloneLMLSTP3Controller.class
				.getClassLoader().getResource(
						"com/traclabs/biosim/client/framework/biosim.png"));

		JLabel waitLabel = new JLabel(splashText, splashIcon,
				SwingConstants.CENTER);
		waitLabel.setForeground(Color.WHITE);
		myFrame.setIconImage(biosimIcon.getImage());
		myFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		WindowCloseListener myWindowCloseListener = new WindowCloseListener();
		myFrame.addWindowListener(myWindowCloseListener);
		myFrame.getContentPane().add(waitLabel, BorderLayout.CENTER);
		myFrame.getContentPane().add(myProgressBar, BorderLayout.SOUTH);
		myFrame.pack();
		myFrame.setLocationRelativeTo(null);
		myFrame.getContentPane().setBackground(Color.BLACK);
		myReadyListener = new ReadyListener();
	}

	public void beginSimulation() {
		if (myFrame != null) {
			myFrame.getContentPane().setCursor(
					Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			myFrame.setVisible(true);
		}
		//standalone startup code
		OrbUtils.startStandaloneNameServer();
		OrbUtils.sleepAwhile(5000);
		OrbUtils.initializeServerForStandalone();
		myServerThread.start();
		OrbUtils.sleepAwhile(4000);
		OrbUtils.initializeClientForStandalone();
		myLogger = Logger.getLogger(this.getClass());
		myLogger.setLevel(Level.DEBUG);
		// HandController Code
		//Add collectReferences line to initialize BioDriver variable (was in main of handcontroller)
		collectReferences();
		myBioDriver.setPauseSimulation(true);
		myBioDriver.startSimulation();
		myLogger.info("Controller starting run - setting up enviroment ");

		
		stepSim();
		myLogger.info("Envorment setup complete - starting sim loop");
		//BioDriver contains check for max number of ticks set in config file
		while (!myBioDriver.isDone())
			stepSim();
		//if we get here, the end condition has been met
		myBioDriver.endSimulation();
		myLogger.info("Controller ended on tick " + myBioDriver.getTicks());
	        
	    // \HandController Code
	}
	
	protected void runClient() {
		String[] args = { "-xml=" + myXmlFilename };
		BiosimMain.main(args);
	}
	
	/**
	 * Hand Controller Code
	 * Executed every tick.  Looks at a sensor, looks at an actuator,
	 * then increments the actuator.
	 */
	public void stepSim() {
		
		myLogger.info("Hand Controller Sim Step for tick " +myBioDriver.getTicks());
		//check and log sensors for crew environment
		float crewO2sensorValue = crewO2ConcentrationSensor.getValue();
		float crewCO2sensorValue = crewCO2ConcentrationSensor.getValue();
		float crewHumiditySensorValue = (crewEnvironment.getRelativeHumidity())/100;
		myLogger.info("Enviroment Gas readings for Crew Compartment: O2: " + crewO2sensorValue +" CO2: "+ crewCO2sensorValue + " VAPOR:" + crewHumiditySensorValue);
		
		//check and log sensors for VPGC environment
		float vpgcO2sensorValue = vpgcO2ConcentrationSensor.getValue();
		float vpgcCO2sensorValue = vpgcCO2ConcentrationSensor.getValue();
		float vpgcHumiditySensorValue = (vpgcEnvironment.getRelativeHumidity())/100;
		myLogger.info("Enviroment Gas readings for VPGC Compartment: O2: " + vpgcO2sensorValue +" CO2: "+ vpgcCO2sensorValue + " VAPOR:" + vpgcHumiditySensorValue);
		
		//Check crew O2 Concentration Level
		adjustModuleO2Levels ("Crew Quarters", crewO2sensorValue, crewO2InjectorActuator, crewO2StorageActuator, crewO2ConcentratorActuator,
				crewO2ConcentratorStoreActuator, envCrewO2TargetPer,envCrewO2HIGH, envCrewO2LOW, envCrewO2Inject );
		
		//End Crew O2 Control
		
		//VPGC O2 Control
		adjustModuleO2Levels ("VPGC", vpgcO2sensorValue, vpgcO2InjectorActuator, vpgcO2StorageActuator, vpgcO2ConcentratorActuator,
				vpgcO2ConcentratorStoreActuator, envVPGCO2TargetPer,envVPGCO2HIGH, envVPGCO2LOW, envVPGCCO2Inject );
		//End VPGC O2 Control
		
		//Crew CO2 Control
		adjustModuleCO2Levels ("Crew Quarters", crewCO2sensorValue, crewCO2InjectorActuator, crewCO2StorageActuator, 
				envCrewCO2TargetPer,envCrewCO2HIGH, envCrewCO2LOW, envCrewCO2Inject );
		//END Crew CO2 Control
		
		//VPGC CO2 Control
		adjustModuleCO2Levels ("VPGC", vpgcCO2sensorValue, vpgcCO2InjectorActuator, vpgcCO2StorageActuator, 
				envVPGCCO2TargetPer,envVPGCCO2HIGH, envVPGCCO2LOW, envVPGCCO2Inject );
		//END VPGC CO2 Control
		
		//Crew Humidity Control
		adjustModuleHumidityLevels ("Crew Quarters", crewHumiditySensorValue, crewDehumidifier, envCrewHumidTargetPer, envCrewHumidHIGH, envCrewHumidLOW );
		
		//VPGC Humidity Control
		adjustModuleHumidityLevels ("VPGC", vpgcHumiditySensorValue, vpgcDehumidifier, envVPGCHumidTargetPer, envVPGCHumidHIGH, envVPGCHumidLOW );
		
		//OGS Control
		myLogger.debug("Begin OGS Check");
		//OGS creates O2 and H2 from portable water, puts into stores. Check O2 and H2 stores. If full, shut down, if less than half full, run at max, if over half full run at half
		float fMaxWater = myOGS.getPotableWaterConsumerDefinition().getMaxFlowRate(0);
		float fMaxPower = myOGS.getPowerConsumerDefinition().getMaxFlowRate(0);
		
		if (isStoreFull(myO2Store)&& isStoreFull(myH2Store)){
			//Stores are full, shutdown
			myOGS.getPowerConsumerDefinition().setDesiredFlowRate(0, 0);
			myOGS.getPotableWaterConsumerDefinition().setDesiredFlowRate(0, 0);
			myLogger.debug("O2 and H2 Stores are full, shutting down OGS");
		} else {
			//Check levels
			if(myO2Store.getPercentageFilled()<=.50 || myH2Store.getPercentageFilled()<=.50){
				//run at max
				myLogger.debug("Turn on OGS, O2Store is "+ myO2Store.getPercentageFilled()+ "% and H2Store is "+ myH2Store.getPercentageFilled() +"%");
				myOGS.getPowerConsumerDefinition().setDesiredFlowRate(fMaxPower, 0);
				myOGS.getPotableWaterConsumerDefinition().setDesiredFlowRate(fMaxWater, 0);
				myLogger.debug("OGS Set to run at Max");
			} else{
				//Check run at 50% 
					myLogger.debug("Turn on OGS at half, O2Store is "+ myO2Store.getPercentageFilled()+ "% and H2Store is "+ myH2Store.getPercentageFilled() +"%");
					myOGS.getPowerConsumerDefinition().setDesiredFlowRate((fMaxPower/2), 0);
					myOGS.getPotableWaterConsumerDefinition().setDesiredFlowRate((fMaxWater/2), 0);
					myLogger.debug("OGS Set to run at Half");
			}	
		}
		myLogger.debug("Finish OGS Check");
		
		//CRS Control
		myLogger.debug("Begin CRS Control Checks");
		//Takes in CO2 and H2 from Stores and Makes Potable Water and Methane (CH4). Run if CO2 levels in store are above 50%
		fMaxPower = myCRS.getPowerConsumerDefinition().getMaxFlowRate(0);
		float fMaxCO2Flow = myCRS.getCO2ConsumerDefinition().getMaxFlowRate(0);
		float fMaxH2Flow = myCRS.getH2ConsumerDefinition().getMaxFlowRate(0);
		myLogger.debug("My CO2Store is "+myCO2Store.getPercentageFilled()+"% full");
		if(myCO2Store.getPercentageFilled()>=.50){
			//Run CRS
			myCRS.getPowerConsumerDefinition().setDesiredFlowRate(fMaxPower, 0);
			myCRS.getCO2ConsumerDefinition().setDesiredFlowRate(fMaxCO2Flow, 0);
			myCRS.getH2ConsumerDefinition().setDesiredFlowRate(fMaxH2Flow, 0);
			myLogger.debug("CRS running (max level)");
		} else {
			//Turn Off CRS
			myCRS.getPowerConsumerDefinition().setDesiredFlowRate(0, 0);
			myCRS.getCO2ConsumerDefinition().setDesiredFlowRate(0, 0);
			myCRS.getH2ConsumerDefinition().setDesiredFlowRate(0, 0);
			myLogger.debug("Shutting down CRS");
		}
		myLogger.debug("Finish CRS Check");
		
		//Pyrolizer Control
		//Pyrolizer run to eliminate trace gases from crew atmosphere. Run every 24 hrs.
		myLogger.debug("Begin Pyrolizer Check");
		fMaxPower = myPyrolizer.getPowerConsumerDefinition().getMaxFlowRate(0);
		if ( myBioDriver.getTicks()%24 == 0){
			myLogger.debug("Running Pyrolizer on shedule tick number: "+ myBioDriver.getTicks());
			myPyrolizer.getPowerConsumerDefinition().setDesiredFlowRate(fMaxPower, 0);
			myLogger.debug("Pyrolizer power set to: "+ myPyrolizer.getPowerConsumerDefinition().getDesiredFlowRate(0));
		}else{
			myLogger.debug("Pyrolizer not scheduled to run");
			myPyrolizer.getPowerConsumerDefinition().setDesiredFlowRate(0, 0);
			myLogger.debug("Pyrolizer power set to: "+ myPyrolizer.getPowerConsumerDefinition().getDesiredFlowRate(0));
		}
		myLogger.debug("Finish Pyrolizer Check");
		
		//WSCR Control
		// WSCR ran day 39-40 and day 47-57 LMLSTP III Final, pg 94
		//Set WSCR to run when Potable Water is low or Gray/Dirty Water tank is getting full (above 75%)
		myLogger.debug("Begin WSCR Check");
		fMaxPower = myWSCR.getPowerConsumerDefinition().getMaxFlowRate(0);
		float fPotWaterLvl = myPotableWaterStore.getPercentageFilled();
		float fGreyWaterLvl = myGreyWaterStore.getPercentageFilled();
		float fDirtyWaterLvl = myDirtyWaterStore.getPercentageFilled();
		if (fPotWaterLvl<=.25||fGreyWaterLvl>=.75|fDirtyWaterLvl>=.75){
			myLogger.debug("Running WSCR - PotableWater Store is "+fPotWaterLvl +" GreyWater Store is "+ fGreyWaterLvl + " DirtyWater Store is "+fDirtyWaterLvl );
			myWSCR.getPowerConsumerDefinition().setDesiredFlowRate(fMaxPower, 0);
			myLogger.debug("WSCR power set to "+ myWSCR.getPowerConsumerDefinition().getActualFlowRate(0));
		} else {
			myLogger.debug("WSCR is not needed, shutting down - PotableWater Store is "+fPotWaterLvl +" GreyWater Store is "+ fGreyWaterLvl + " DirtyWater Store is "+fDirtyWaterLvl );
			myWSCR.getPowerConsumerDefinition().setDesiredFlowRate(0, 0);
			myLogger.debug("WSCR power set to "+ myWSCR.getPowerConsumerDefinition().getActualFlowRate(0));
		}
		myLogger.debug("Finish WSCR Check");
		
		//Incinerator Control
		// Run Incenerator when the DryWaste store is getting full (above 75%)
		myLogger.debug("Begin Incerator Check");
		fMaxPower = myIncinerator.getPowerConsumerDefinition().getMaxFlowRate(0);
		float fDryWasteLvl = myDryWasteStore.getPercentageFilled();
		if (fDryWasteLvl>=.75){
			myLogger.debug("Running Incinerator - DryWaste Store is "+fDryWasteLvl);
			myIncinerator.getPowerConsumerDefinition().setDesiredFlowRate(fMaxPower, 0);
			myLogger.debug("Incinerator power set to "+ myIncinerator.getPowerConsumerDefinition().getActualFlowRate(0));
		}else {
			myLogger.debug("Incinerator is not needed, shutting down");
			myIncinerator.getPowerConsumerDefinition().setDesiredFlowRate(0, 0);
			myLogger.debug("Incinerator power set to "+ myIncinerator.getPowerConsumerDefinition().getActualFlowRate(0));
		}
		myLogger.debug("Finish Incerator Check");
		
		// advancing the sim 1 tick
		myBioDriver.advanceOneTick();
		myLogger.info("Storage Levels are: 02:"+myO2Store.getCurrentLevel()+" CO2:"+myCO2Store.getCurrentLevel()+" H2: "+myH2Store.getCurrentLevel());    
	}

	private void adjustModuleCO2Levels(String strModuleName, float myCO2sensorValue,
			CO2OutFlowRateActuator myCO2InjectorActuator,
			CO2InFlowRateActuator myCO2StorageActuator,
			double envCO2TargetPer, double envCO2HIGH,
			double envCO2LOW, float envCO2Inject) {
		
				//Turn on O2 Concentrator if CO2 level is high
				//CO2 (PPM) 1251 +/- 448
				//Using 1 volume percent = 10,000 ppmv 
				myCO2PPMV = myCO2sensorValue * 1000000; //( 10000*100 )
				myLogger.info("CO2 Level (%): "+ myCO2sensorValue * 100 +" PPMV: "+ myCO2PPMV );
			
				//If CO2 level is above 1699, Remove CO2 from environment to stores
				if (myCO2PPMV > envCO2HIGH){
					myLogger.debug("CO2 is above HIGH threshold ("+ envCO2HIGH +") in "+ strModuleName + "- check actuator and turn on VCCR");
					//check actuator, turn off if running
					if (myCO2InjectorActuator.getValue()>0){
						myLogger.debug("Turning OFF CO2 injection from Stores in "+ strModuleName );
						myCO2InjectorActuator.setValue(0);
						myCO2StorageActuator.setValue(0);
					} else {
						myLogger.debug("CO2 is NOT being injected from stores in "+ strModuleName );
					}
					
				    //Check to see if VCCR is running
				    if (isVCCRRunning()){
				    	//If it is running then increase flow if not currently at MAX
				    	float myCurrentCO2Flow = myVCCR.getCO2ProducerDefinition().getActualFlowRate(0);
				    	float myCurrentInFlow = myVCCR.getAirConsumerDefinition().getActualFlowRate(0);
				    	float myMAXCO2Flow = myVCCR.getCO2ProducerDefinition().getMaxFlowRate(0);
				    	float myMAXInFlow = myVCCR.getAirConsumerDefinition().getMaxFlowRate(0);
				    	myLogger.debug("Current VCCR intake is :"+ myCurrentInFlow +" and Current VCCR output of CO2 to CO2Store is: " + myCurrentCO2Flow );
				    	if (myCurrentCO2Flow >= myMAXCO2Flow && myCurrentInFlow >= myMAXInFlow){
				    		myLogger.debug("VCCR is running at MAX - O2 Flow: "+myVCCR.getCO2ProducerDefinition().getMaxFlowRate(0)+ 
				    				" CO2 Flow: " + myVCCR.getAirConsumerDefinition().getMaxFlowRate(0));
				    		//regulate flows to max incase they are higher
				    		myVCCR.getCO2ProducerDefinition().setDesiredFlowRate(myMAXCO2Flow, 0);
				    		myVCCR.getAirConsumerDefinition().setDesiredFlowRate(myMAXInFlow, 0);
				    	} else {
				    		//VCCR not at MAX for either input or output
				    		if (myCurrentInFlow < myMAXInFlow){
				    			//Increase the input rate
				    			myLogger.debug("Increasing VCCR input rate from module "+ strModuleName );
				    			myVCCR.getAirConsumerDefinition().setDesiredFlowRate(myCurrentInFlow + 100, 0);
				    			myLogger.debug("Set VCCR input from module "+ strModuleName + " to : "+ myVCCR.getAirConsumerDefinition().getDesiredFlowRate(0));
				    		} else {
				    			myLogger.debug("VCCR input from module "+ strModuleName + " is at MAX ("+myVCCR.getAirConsumerDefinition().getMaxFlowRate(0)+")");
				    		}
				    		if (myCurrentCO2Flow < myMAXCO2Flow){
				    			//Increase the output rate
				    			myLogger.debug("Increasing VCCR output to CO2 Store");
				    			myVCCR.getCO2ProducerDefinition().setDesiredFlowRate(myCurrentCO2Flow + 100, 0);
				    			myLogger.debug("Set VCCR Flow rate to CO2Store: "+ myVCCR.getCO2ProducerDefinition().getDesiredFlowRate(0));
				    		} else {
				    			myLogger.debug("VCCR output to CO2 Store is at MAX ("+myVCCR.getCO2ProducerDefinition().getMaxFlowRate(0)+")");
				    		}
				    		
				    	}
				    	myLogger.debug("Finished Adjusting VCCR output");
				    } else {
				    	myLogger.debug("VCCR is OFF - turning ON");
				    	float myPowerLevel = myVCCR.getPowerConsumerDefinition().getMaxFlowRate(0);
				    	myVCCR.getPowerConsumerDefinition().setDesiredFlowRate(myPowerLevel, 0);
				    	myLogger.debug("Set VCCR Power to :"+ myVCCR.getPowerConsumerDefinition().getDesiredFlowRate(0));
				    	myLogger.debug("VCCR power flow rate is :" + myVCCR.getPowerConsumerDefinition().getActualFlowRate(0));
				    }
				} else {
					myLogger.debug("CO2 is below HIGH threshold - Check VCCR - and Check LOW threshold");
					//Check if SWAD is running
					if (isVCCRRunning()){
						//If it is running, turn it off
						myVCCR.getPowerConsumerDefinition().setDesiredFlowRate(0, 0);
						myLogger.debug("SWAD is turned OFF");
					} else {
						myLogger.debug("SVCCR is not running");
					}
					//If CO2 level is below threshold Add from stores
					//If CO2 is flowing from stores, increase injection rate (if below 803), otherwise turn off injection
					//Honor max injection value
					if (myCO2PPMV < envCO2LOW ){
						float myCO2ActuatorValue = myCO2InjectorActuator.getValue();
						float myCO2StorageValue = myCO2StorageActuator.getValue();
						
						myLogger.info("CO2 actuator currently set at " + myCO2InjectorActuator.getValue()+ " Actuator Max: " + myCO2InjectorActuator.getMax() + "; Sotrage Flow:"
								+ myCO2StorageActuator.getValue()+" Max:"+ myCO2StorageActuator.getMax());
						if (myCO2InjectorActuator.getValue()< myCO2InjectorActuator.getMax()){
							myLogger.info("CO2 LOW - incrementing CO2 actuator by "+ envCO2Inject);
							myCO2InjectorActuator.setValue(myCO2ActuatorValue + envCO2Inject);
							myCO2StorageActuator.setValue(myCO2StorageValue + envCO2Inject);
						}else{
							myLogger.info("CO2 actuator is at MAX ("+ myCO2InjectorActuator.getMax()+")");
							myCO2InjectorActuator.setValue(myCO2InjectorActuator.getMax());
							myCO2StorageActuator.setValue(myCO2StorageActuator.getMax());
						}
						myLogger.info("CO2 actuator is now currently set at " + myCO2InjectorActuator.getValue()+ " Actuator Max: " + myCO2InjectorActuator.getMax() + "; Sotrage Flow:"
								+ myCO2StorageActuator.getValue()+" Max:"+ myCO2StorageActuator.getMax());
					} else {
						//check actuator, turn off if running
						myLogger.debug("CO2 is above LOW threshold - check actuator");
						if (myCO2InjectorActuator.getValue()>0){
							myLogger.debug("Turning OFF CO2 injection from Stores");
							myCO2InjectorActuator.setValue(0);
							myCO2StorageActuator.setValue(0);
						} else {
							myLogger.debug("CO2 is NOT being injected from stores");
						}
						
					}
				}
		
	}
	
	private void adjustModuleHumidityLevels(String strModuleName, float myHumidSensorValue,
			Dehumidifier envDehumidifier,
			double envHumidTargetPer, double envHumidHIGH,
			double envHumidLOW) {
		
			myLogger.debug("Humidty is " + myHumidSensorValue + " in "+ strModuleName); 
			//If Humidity level is below LOW threshold turn off Dehumidifier 
				if (myHumidSensorValue == envHumidTargetPer || myHumidSensorValue <= envHumidLOW ){
					myLogger.debug("Humidty is below LOW threshold ("+ envHumidLOW +") in "+ strModuleName + " - turn off Dehumidifier");
					if (envDehumidifier.getAirConsumerDefinition().getDesiredFlowRate(0)>0){
						myLogger.debug("Dehumidifier is on, turn off");
						envDehumidifier.getAirConsumerDefinition().setDesiredFlowRate(0, 0);
						envDehumidifier.getDirtyWaterProducerDefinition().setDesiredFlowRate(0, 0);
					} else {
						myLogger.debug("Dehumidifier is off");
					}
				
					myLogger.info("Dehumidifier in "+ strModuleName + " - is set to: " + envDehumidifier.getAirConsumerDefinition().getDesiredFlowRate(0) + " Max: " + envDehumidifier.getAirConsumerDefinition().getMaxFlowRate(0));
				} else {
					myLogger.debug("Humidty is above HIGH threshold ("+ envHumidHIGH +") in "+ strModuleName + " - turn on Dehumidifier or increment");
					//Trun-on or increment Dehumidifier to move excess Water Vapor to stores, honor max if set (assumes dirty water rates are the same as airflow)
					myLogger.debug("Checking Dehumidifier, increment by 10");
					float fCurrentValue = envDehumidifier.getAirConsumerDefinition().getDesiredFlowRate(0);
					float fMaxValue = envDehumidifier.getAirConsumerDefinition().getMaxFlowRate(0);
					myLogger.info("Dehumidifier in "+ strModuleName + " - is set to: " + fCurrentValue + "  Max: " + fMaxValue);
					if (fCurrentValue< fMaxValue){
						//Increase both Air Flow and stores
						envDehumidifier.getAirConsumerDefinition().setDesiredFlowRate((fCurrentValue + 10f), 0);
						envDehumidifier.getDirtyWaterProducerDefinition().setDesiredFlowRate((fCurrentValue + 10f), 0);
					}else{
						myLogger.info("Dehumidfier is at max");
						envDehumidifier.getAirConsumerDefinition().setDesiredFlowRate(fMaxValue, 0);
						envDehumidifier.getDirtyWaterProducerDefinition().setDesiredFlowRate(fMaxValue, 0);
					}
					myLogger.info("Dehumidifier in "+ strModuleName + " - is set to: " + envDehumidifier.getAirConsumerDefinition().getDesiredFlowRate(0) + " Max: " + envDehumidifier.getAirConsumerDefinition().getMaxFlowRate(0));
				}
		
	}

	private void adjustModuleO2Levels(String strModuleName, float myO2sensorValue,
			O2InFlowRateActuator myO2InjectorActuator,
			O2OutFlowRateActuator myO2StorageActuator,
			O2OutFlowRateActuator myO2ConcentratorActuator,
			O2InFlowRateActuator myO2ConcentratorStoreActuator,
			double envO2TargetPer, double envO2HIGH,
			double envO2LOW, float envO2Inject) {
		
		//float oxygenPercentage = myO2ConcentrationSensor.getValue();  //CIH 200117 add conditional change in O2 actuator
				float myO2ActuatorValue = myO2InjectorActuator.getValue();
				float myO2StorageActuatorValue = myO2StorageActuator.getValue();
				float myO2ConcentratorValue = myO2ConcentratorActuator.getValue();
				float myO2ConcentratorStoreValue = myO2ConcentratorStoreActuator.getValue();
						
				//Take external action if O2 levels are 21.9 +/- .7
				//If O2 level is above 22.6. turn off O2 from tanks, turn on concentrator
				if (myO2sensorValue > envO2HIGH ){
					myLogger.debug("O2 is above HIGH threshold ("+ envO2HIGH +") in "+ strModuleName + " - check Actuator and Concentrator");
					if (myO2InjectorActuator.getValue()>0 || myO2StorageActuator.getValue()>0){
						myLogger.debug("Turning off O2 injection from stores");
						//myO2InjectorActuator.setValue(0);	CIH 0127 One side only, need to turn off both consumer and producer
						myO2InjectorActuator.setValue(0);
						myO2StorageActuator.setValue(0);
					} else {
						myLogger.debug("O2 is NOT being injected from stores");
					}
					//Trun-on or increment Concentrator to move excess O2 to stores, honor max if set
					myLogger.debug("Checking Concentrator, increment by 10");
					myLogger.info("O2 concentrator in "+ strModuleName + " - is set to: " + myO2ConcentratorActuator.getValue()+ " Actuator Max: " + myO2InjectorActuator.getMax());
					if (myO2ConcentratorActuator.getValue()< myO2ConcentratorActuator.getMax()){
						//Increase both Concentrator and stores
						myO2ConcentratorActuator.setValue(myO2ConcentratorValue + 10);
						myO2ConcentratorStoreActuator.setValue(myO2ConcentratorStoreValue + 10);
					}else{
						myLogger.info("O2 Concentrator is at max");
						myO2ConcentratorActuator.setValue(myO2ConcentratorActuator.getMax());
						myO2ConcentratorStoreActuator.setValue(myO2ConcentratorStoreActuator.getMax());
					}
					
					myLogger.info("O2 concentrator in "+ strModuleName + " - is set to: " + myO2ConcentratorActuator.getValue()+ " Actuator Max: " + myO2InjectorActuator.getMax());
				} else {
					//If O2 level is below 21.2, add O2 from stores
					if (myO2sensorValue < envO2LOW ){
						//Make Sure O2 Concentrator is off
						myLogger.info("02 in "+ strModuleName + " - is below LOW threshold, ensure O2 Concentrator is off ");
						myO2ConcentratorActuator.setValue(0);
						myO2ConcentratorStoreActuator.setValue(0);
						//If O2 is flowing from stores, increase injection rate.
						//Honor max injection value
						myLogger.info("O2 is below LOW threshold ("+ envO2LOW +")in "+ strModuleName + " - incrementing O2 actuator by " + envO2Inject);
						myLogger.info("O2 actuator currently set at " + myO2InjectorActuator.getValue()+ " in "+ strModuleName + " - Actuator Max: " + myO2InjectorActuator.getMax());
						if (myO2InjectorActuator.getValue()< myO2InjectorActuator.getMax()){
							//Increase both injector and stores
							myO2InjectorActuator.setValue(myO2ActuatorValue + envO2Inject);
							myO2StorageActuator.setValue(myO2ActuatorValue + envO2Inject);
						}else{
							myLogger.info("O2 actuator in "+ strModuleName + " - is at max");
							myO2InjectorActuator.setValue(myO2InjectorActuator.getMax());
							myO2StorageActuator.setValue(myO2StorageActuator.getMax());
						}
					} else {
						myLogger.info("O2 is within thresholds in "+ strModuleName + " - No O2 adjustments needed. Make sure Concentrator is turned off");
						//Make Sure O2 Concentrator is off
						myO2ConcentratorActuator.setValue(0);
						myO2ConcentratorStoreActuator.setValue(0);
					}
				}
		
	}

	private class ServerThread implements Runnable {
		public void run() {
			BiosimServer server = new BiosimServer(0, myDriverPause,
					myXmlFilename);
			if (myReadyListener != null)
				server.addReadyListener(myReadyListener);
			server.runServer("BiosimServer (id=0)");
		}
	}

	private class ClientThread implements Runnable {
		public void run() {
			runClient();
		}
	}

	public class ReadyListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (myFrame != null) {
				myFrame.getContentPane().setCursor(Cursor.getDefaultCursor());
				myFrame.dispose();
			}
		}
	}

	/**
	 * The Window Close listener for this Frame
	 */
	private class WindowCloseListener extends java.awt.event.WindowAdapter {
		public void windowClosing(java.awt.event.WindowEvent event) {
			System.exit(0);
		}
	}

	public String getXmlFilename() {
		return myXmlFilename;
	}
	
	//** Hand Controller Routines **//
	
	/**
	 * Collects references to BioModules we'll need
	 * to run/observer/poke the sim.  The BioHolder is 
	 * a utility for clients to easily access different parts 
	 * of BioSim.
	 *
	 */
	public void collectReferences() {
		BioHolderInitializer.setFile(myXmlFilename);
		Integer intModule = 99; 
		myBioHolder = BioHolderInitializer.getBioHolder();
		myBioDriver = myBioHolder.theBioDriver;
		crewEnvironment = myBioHolder.theSimEnvironments.get(0);
		vpgcEnvironment = myBioHolder.theSimEnvironments.get(1);
		
		
		Injector injCrewO2Injector;
		Injector injVPGCO2Injector;
		Injector injCrewCO2Injector;
		Injector injVPGCCO2Injector;
				
		injCrewO2Injector = myBioHolder.theInjectors.get(0);
		myLogger.debug("Found Injectors, Crew O2 Injector is set to: " + injCrewO2Injector.getModuleName());
		injVPGCO2Injector = myBioHolder.theInjectors.get(3);
		myLogger.debug("Found Injectors, VPGC O2 Injector is set to: " + injVPGCO2Injector.getModuleName());
		crewO2Concentrator = myBioHolder.theAccumulators.get(0);
		myLogger.debug("Found Accumulators, Crew O2 Accumulator is set to: " + crewO2Concentrator.getModuleName());
		injCrewCO2Injector = myBioHolder.theInjectors.get(1);
		myLogger.debug("Found Injectors, Crew CO2 Injector is set to: " + injCrewCO2Injector.getModuleName());
		injVPGCCO2Injector = myBioHolder.theInjectors.get(2);
		myLogger.debug("Found Injectors, VPGC CO2 Injector is set to: " + injVPGCCO2Injector.getModuleName());
		vpgcO2Concentrator = myBioHolder.theAccumulators.get(1);
		myLogger.debug("Found Accumulators, VPGC O2 Accumulator is set to: " + vpgcO2Concentrator.getModuleName());
		
		//Set dehumidifiers
		crewDehumidifier = myBioHolder.theDehumidifiers.get(0);
		myLogger.debug("Found Dehumidfiers, crewDehumidifier is set to "+ crewDehumidifier.getModuleName());
		vpgcDehumidifier = myBioHolder.theDehumidifiers.get(1);
		myLogger.debug("Found Dehumidfiers,vpgcDehumidifier is set to "+ vpgcDehumidifier.getModuleName());
		
		//Injector myO2Injector = myBioHolder.theInjectors.get(1); Replace with loop
		//loop for determing which injector were dealing with in the index.
		//CIH 200415 - okay, this is ugly but it appears that switch can only check string names in 1.7 or above. Not sure what forcing compailing to 1.7
		//would do to the older code, doing the integer workround.
		int intIndex = 0;
		for(Iterator<Injector> i =  myBioHolder.theInjectors.iterator(); i.hasNext();){
			String strModuleName = i.next().getModuleName();
			if (strModuleName.equals("Crew_O2_Injector")){
				intModule = 0;
			}else{
				if (strModuleName.equals("Crew_CO2_Injector")){
					intModule = 1;
				}else{
					if (strModuleName.equals("VPGC_CO2_Injector")){
					intModule = 2;
				}else{
					if (strModuleName.equals("VPGC_O2_Injector")){
						intModule = 3;
					}
				}
				}
			}
				
			switch(intModule){
				//case "Crew_O2_Injector":
			case 0:
					myLogger.debug("Configure Crew O2 Injectors");
					
					crewO2InjectorActuator = (O2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2InFlowRateActuators, injCrewO2Injector));
					
					myLogger.debug("crewO2InjectorActuator is currently set to: "+ crewO2InjectorActuator.getValue() + " Max:"+ crewO2InjectorActuator.getMax() + " Min: " + crewO2InjectorActuator.getMin() + 
								" for module "+	crewO2InjectorActuator.getOutputModule().getModuleName());
					crewO2StorageActuator = (O2OutFlowRateActuator)(myBioHolder.getActuatorAttachedTo(myBioHolder.theO2OutFlowRateActuators, injCrewO2Injector));
					crewO2ConcentratorStoreActuator = (O2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2InFlowRateActuators, crewO2Concentrator));
					
					myLogger.debug("crewO2StorageActuator is currently set to: "+ crewO2StorageActuator.getValue() + " Max:"+ crewO2StorageActuator.getMax() + " Min: " + crewO2StorageActuator.getMin() + 
							" for module "+	crewO2StorageActuator.getOutputModule().getModuleName());
					
					myLogger.debug("crewO2ConcentratorStoreActuator is currently set to: "+ crewO2ConcentratorStoreActuator.getValue() + " Max:"+ crewO2ConcentratorStoreActuator.getMax() + " Min: " + crewO2ConcentratorStoreActuator.getMin() + 
							" for module "+	crewO2ConcentratorStoreActuator.getOutputModule().getModuleName());
					 break;	
				//case "Crew_CO2_Injector":
			case 1:
										
					myLogger.debug("Configure Crew_CO2_Injector");
					crewCO2StorageActuator = (CO2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theCO2InFlowRateActuators, injCrewCO2Injector));
					myLogger.debug("crewCO2StorageActuator is currently set to : "+ crewCO2StorageActuator.getValue() + " Max:"+ crewCO2StorageActuator.getMax() + " Min: " + crewCO2StorageActuator.getMin() + 
							" for module "+	crewCO2StorageActuator.getOutputModule().getModuleName());
				    crewCO2InjectorActuator = (CO2OutFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theCO2OutFlowRateActuators, injCrewCO2Injector));
					
					myLogger.debug("crewCO2InjectorActuator is currently set to : "+ crewCO2InjectorActuator.getValue() + " Max:"+ crewCO2InjectorActuator.getMax() + " Min: " + crewCO2InjectorActuator.getMin() + 
								" for module "+	crewCO2InjectorActuator.getOutputModule().getModuleName());
					 break;
				//case "VPGC_CO2_Injector" :
			case 2:
					myLogger.debug("Configure VPGC_CO2_Injector" );
					vpgcCO2StorageActuator = (CO2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theCO2InFlowRateActuators, injVPGCCO2Injector));
					myLogger.debug("vpgcCO2StorageActuator is currently set to : "+ vpgcCO2StorageActuator.getValue() + " Max:"+ vpgcCO2StorageActuator.getMax() + " Min: " + vpgcCO2StorageActuator.getMin() + 
							" for module "+	vpgcCO2StorageActuator.getOutputModule().getModuleName());
					vpgcCO2InjectorActuator = (CO2OutFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theCO2OutFlowRateActuators, injVPGCCO2Injector));
						
						myLogger.debug("vpgcCO2InjectorActuator is currently set to : "+ vpgcCO2InjectorActuator.getValue() + " Max:"+ vpgcCO2InjectorActuator.getMax() + " Min: " + vpgcCO2InjectorActuator.getMin() + 
									" for module "+	vpgcCO2InjectorActuator.getOutputModule().getModuleName());

						 break;
				//case "VPGC_O2_Injector":
			case 3:
					myLogger.debug("Configure VPGC_O2_Injector");
					vpgcO2InjectorActuator = (O2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2InFlowRateActuators, injVPGCO2Injector));
					
					myLogger.debug("vpgcO2InjectorActuator is currently set to: "+ vpgcO2InjectorActuator.getValue() + " Max:"+ vpgcO2InjectorActuator.getMax() + " Min: " + vpgcO2InjectorActuator.getMin() + 
								" for module "+	vpgcO2InjectorActuator.getOutputModule().getModuleName());
					
					vpgcO2StorageActuator = (O2OutFlowRateActuator)(myBioHolder.getActuatorAttachedTo(myBioHolder.theO2OutFlowRateActuators, injVPGCO2Injector));
					vpgcO2ConcentratorStoreActuator = (O2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2InFlowRateActuators, vpgcO2Concentrator));
					
					myLogger.debug("vpgcO2StorageActuator is currently set to: "+ vpgcO2StorageActuator.getValue() + " Max:"+ vpgcO2StorageActuator.getMax() + " Min: " + vpgcO2StorageActuator.getMin() + 
							" for module "+	vpgcO2StorageActuator.getOutputModule().getModuleName());
					
					myLogger.debug("vpgcO2ConcentratorStoreActuator is currently set to: "+ vpgcO2ConcentratorStoreActuator.getValue() + " Max:"+ vpgcO2ConcentratorStoreActuator.getMax() + " Min: " + vpgcO2ConcentratorStoreActuator.getMin() + 
							" for module "+	vpgcO2ConcentratorStoreActuator.getOutputModule().getModuleName());
					
					 break;
				default:
					myLogger.warn("Unkown Injector encountered - " + strModuleName +" - in collectReferences of handcontroller, not assigned any control. intIndex is -" + intIndex);
			}
			intIndex++;
		}
		
		//Accumulator myO2Concentrator = myBioHolder.theAccumulators.get(0); Replace with loop
		intIndex = 0;
		intModule = 99;
		for(Iterator<Accumulator> i1 =  myBioHolder.theAccumulators.iterator(); i1.hasNext();){
			String strModuleName = i1.next().getModuleName();
			if (strModuleName.equals("Crew_O2_Concentrator")){
				intModule = 0;
			}else{
				if (strModuleName.equals("VPGC_O2_Concentrator")){
					intModule = 1;
				}
			}
			switch(intModule){
				//case "Crew_O2_Concentrator":
			case 0:
					myLogger.debug("Crew_O2_Concentrator");
					crewO2ConcentratorActuator = (O2OutFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2OutFlowRateActuators, crewO2Concentrator));
					myLogger.debug("crewO2ConcentratorActuator is currently set to: "+ crewO2ConcentratorActuator.getValue() + " Max:"+ crewO2ConcentratorActuator.getMax() + " Min: " + crewO2ConcentratorActuator.getMin() + 
							" for module "+	crewO2ConcentratorActuator.getOutputModule().getModuleName());
				break;
				//case "VPGC_O2_Concentrator":
			case 1:
					myLogger.debug("VPGC_O2_Concentrator");
					vpgcO2ConcentratorActuator = (O2OutFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2OutFlowRateActuators, vpgcO2Concentrator));
					myLogger.debug("vpgcO2ConcentratorActuator is currently set to: "+ vpgcO2ConcentratorActuator.getValue() + " Max:"+ vpgcO2ConcentratorActuator.getMax() + " Min: " + vpgcO2ConcentratorActuator.getMin() + 
							" for module "+	vpgcO2ConcentratorActuator.getOutputModule().getModuleName());
				break;
					
			default:
					myLogger.warn("Unkown Accumulator encountered - " + strModuleName +" - in collectReferences of handcontroller, not assigned any control. intIndex is - " + intIndex);
			}
			intIndex++;
		}
    
		// Crew sensor readings in proper variables
		crewO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(myBioHolder.theGasConcentrationSensors, crewEnvironment.getO2Store()));
		myLogger.debug("crewO2ConcentrationSensor (crewEnv.O2Store)value is: " + crewO2ConcentrationSensor.getValue());
		
		myO2Store = myBioHolder.theO2Stores.get(0);
		myLogger.debug("O2Store is :" +myO2Store.getCurrentLevel()+" IsFull: "+ isStoreFull(myO2Store));
		
		crewCO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(myBioHolder.theGasConcentrationSensors, crewEnvironment.getCO2Store()));
		myLogger.debug("crewCO2ConcentrationSensor (crewEnv.CO2Store)value is: " +crewCO2ConcentrationSensor.getValue());
		
		// VPGC concentrator sensor setting
		vpgcO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(myBioHolder.theGasConcentrationSensors, vpgcEnvironment.getO2Store()));
		myLogger.debug("vpgcO2ConcentrationSensor (vpgcEnv)value is: " + vpgcO2ConcentrationSensor.getValue());
		
		vpgcCO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(myBioHolder.theGasConcentrationSensors, vpgcEnvironment.getCO2Store()));
		myLogger.debug("vpgcCO2ConcentrationSensor (vpgcEnv)value is: " + vpgcCO2ConcentrationSensor.getValue());
		
		//Set OGS for the sim, only one in use
		myOGS = myBioHolder.theOGSModules.get(0);
		myLogger.debug("myOGS is set to: "+ myOGS.getModuleName());
				
		//Set VCCR for the sim, only one in ues;
		myVCCR = myBioHolder.theVCCRModules.get(0);
		myLogger.debug("myVCCR is set to: "+ myVCCR.getModuleName());
		
		//Set CRS, only one in use;
		myCRS = myBioHolder.theCRSModules.get(0);
		myLogger.debug("myCRS is set to: "+ myCRS.getModuleName());
		
		//Set Pyrolizer, only one in ues;
		myPyrolizer = myBioHolder.thePyrolizerModules.get(0);
		myLogger.debug("myPyrolizer is set to: "+ myPyrolizer.getModuleName());
		
		//Set WSCR, only one in ues;
		myWSCR = myBioHolder.theWaterRSModules.get(0);
		myLogger.debug("myWSCR is set to: "+ myWSCR.getModuleName());
		
		//Set Incinerator, only one in ues;
		myIncinerator = myBioHolder.theIncinerators.get(0);
		myLogger.debug("myIncinerator is set to: "+ myIncinerator.getModuleName());
		
		//Set the Dry Waste Store, only one in use;
		myDryWasteStore = myBioHolder.theDryWasteStores.get(0);
		myLogger.debug("myDryWasteStore is set to: "+ myDryWasteStore.getModuleName());
		
	    //Set the CO2 Store for the sim, only one in use
		myCO2Store = myBioHolder.theCO2Stores.get(0);
		myLogger.debug("CO2Store is :" +myCO2Store.getCurrentLevel()+" IsFull: "+ isStoreFull(myCO2Store));
		
		//Set the H2 Store for the sim, only one in use
		myH2Store = myBioHolder.theH2Stores.get(0);
		myLogger.debug("H2Store is :" +myH2Store.getCurrentLevel()+" IsFull: "+ isStoreFull(myH2Store));
		
		//Set the N2 Store for the sim, only one in use
		myN2Store = myBioHolder.theNitrogenStores.get(0);
		myLogger.debug("N2Store is : " +myN2Store.getCurrentLevel() +" IsFull: "+ isStoreFull(myN2Store));
		
		//Set the CH4 Store for the sim, only one in use
		myCH4Store = myBioHolder.theMethaneStores.get(0);
		myLogger.debug("CH4Store is : " +myCH4Store.getCurrentLevel() +" IsFull: "+ isStoreFull(myCH4Store));
		
		//Set the GreyWater Store for the sim, only one in use
		myGreyWaterStore = myBioHolder.theGreyWaterStores.get(0);
		myLogger.debug("GreyWaterStore is : " +myGreyWaterStore.getCurrentLevel() +" IsFull: "+ isStoreFull(myGreyWaterStore));
		
		//Set theDirtyWater Store for the sim, only one in use
	    myDirtyWaterStore = myBioHolder.theDirtyWaterStores.get(0);
	    myLogger.debug("DirtyWaterStore is : " +myDirtyWaterStore.getCurrentLevel() +" IsFull: "+ isStoreFull(myDirtyWaterStore));
		
	    //Set thePotableWaterStore for the sim, only one in use
	    myPotableWaterStore = myBioHolder.thePotableWaterStores.get(0);
	    myLogger.debug("PotableWaterStore is : " +myPotableWaterStore.getCurrentLevel() +" IsFull: "+ isStoreFull(myPotableWaterStore));
		
	}
	
	/**
	 * Checks to see if the VCCR is running or not
	 * Checks myVCCR (an instance of the VCCR object from com.traclabs.biosim.server.simulation.air)
	 * @return true if VCCR is running
	 * @return false if VCCR is not running
	 */
	private boolean isVCCRRunning(){
		float myVCCRPower = myVCCR.getPowerConsumerDefinition().getActualFlowRate(0);
		myLogger.debug("SWAD/VCCR power level is: "+ myVCCRPower);
		if (myVCCRPower >0 ){
			myLogger.debug("SWAD/VCCR is ON");
			return true;
		} else {
			myLogger.debug("SWAD/VCCR is OFF");
			return false;
		}
	}
	
	private boolean isStoreFull(Store myStore) {
		if (myStore.getPercentageFilled()==100){
			myLogger.debug("The "+ myStore.getModuleName()+" is full:" + myStore.getPercentageFilled());
		return true;
		}else{
			return false;
		}
	}
	/*}
	private boolean isO2StoreFull(){
		if (myO2Store.getPercentageFilled()==100){
			myLogger.debug("O2Store is full:" + myO2Store.getPercentageFilled());
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isH2StoreFull() {
		if (myH2Store.getPercentageFilled()==100){
			myLogger.debug("H2Store is full:" + myH2Store.getPercentageFilled());
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isCO2StoreFull(){
		if (myCO2Store.getPercentageFilled()==100){
			myLogger.debug("CO2Store is full:" + myCO2Store.getPercentageFilled());
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isN2StoreFull(){
		if (myN2Store.getPercentageFilled()==100){
			myLogger.debug("N2Store is full:" + myN2Store.getPercentageFilled());
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isCH4StoreFull(){
		if (myCH4Store.getPercentageFilled()==100){
			myLogger.debug("CH4Store is full:" + myCH4Store.getPercentageFilled());
			return true;
		}else{
			return false;
		}
	
	}*/
	
}

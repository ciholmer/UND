package com.traclabs.biosim.framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

//import com.traclabs.biosim.client.control.ActionMap;
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
import com.traclabs.biosim.idl.simulation.air.O2Store;
import com.traclabs.biosim.idl.simulation.air.CO2Store;
import com.traclabs.biosim.idl.simulation.air.H2Store;
import com.traclabs.biosim.idl.simulation.air.VCCR;
import com.traclabs.biosim.idl.framework.LogLevel;
import com.traclabs.biosim.idl.framework.MalfunctionIntensity;
import com.traclabs.biosim.idl.framework.MalfunctionLength;
import com.traclabs.biosim.idl.simulation.environment.SimEnvironment;
import com.traclabs.biosim.idl.simulation.framework.Injector;
import com.traclabs.biosim.idl.simulation.framework.Accumulator;
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
 * Modified Code to mimic external simulation controls during actual LMLSTP Phase 1 experiment as documented in
 * Early Human Testing Initiative Phase I Final Report
 * Doc. Number ADV-208 Date 3-25-96
 * JSC# 33636
 * Prepared by M. Edeen and D. Barta
 * 
 * Test Time-line table 2 from Phase 1 final report (pg 15)
 * O2 level injection from stores
 * O2 level removal via O2 concentrator to stores 
 * CO2 level injection from stores (starting day 3) (see fig 12 pg 17)
 * VCCR/SWAD not used during Phase 1 sim
 * light and temperature adjustments
 *
 * Table 3 Environmental Conditions
 * Rel Hum - 70.9 +/- 6.1
 * CO2 (PPM) 1251 +/- 448
 * O2 % 21.9 +/- 0.7
 */

public class BiosimStandaloneLMLSTPController {

	private Thread myServerThread;

	private Thread myClientThread;

	private ReadyListener myReadyListener;

	private JFrame myFrame;

	private JProgressBar myProgressBar;

	private String myXmlFilename;

	private int myDriverPause;
	
	//Hand Controller Values 090819 edit 200123 for LMLSTP
	
	private GenericSensor myO2ConcentrationSensor;
	
	private GenericSensor myCO2ConcentrationSensor;
	
	private Accumulator myO2Concentrator;

	private O2InFlowRateActuator myO2InjectorActuator;
	private O2OutFlowRateActuator myO2StorageActuator;
	private O2OutFlowRateActuator myO2ConcentratorActuator;
	private O2InFlowRateActuator myO2ConcentratorStoreActuator;
	
	private CO2InFlowRateActuator myCO2StorageActuator;
	private CO2OutFlowRateActuator myCO2InjectorActuator;
	//private GenericActuator myCO2InjectorActuator;
	
	private float myCO2PPMV;
	
	
    // hand controller stuff;
	
	//Values to control for
	/**
	 * CIH 200209 - Examining phase 1 final report. Reported range was 21.9 % +/- 0.7%
	 * Examining O2% concentration during human occupation shows flux around 21% with a high of approx 21.7
	 * and a low of 19.9.
	 */
	private double envO2TargetPer = 0.210;
	//private double envO2RangePer = 	0.007;
	//private double envO2HIGH = envO2TargetPer + envO2RangePer;
	private double envO2HIGH = 0.219;
	//private double envO2LOW = envO2TargetPer - envO2RangePer;
	private double envO2LOW = 0.199;
	private float envO2Inject = 100; //the amount to increase the injector by when adding O2 from stores


	/** 
	 * Looked at the Phase 1 final report for what injection rate was used in the actual test,
	 *  unfortunately shows that chambers were kept separate. Not sure how of figure this in at
	 *   this point in time. Crewed side of the system started at 1251 and reached a peak with 
	 *   Bob Roberts around 7800PPM while the plant chambers self regulated at 1251 +/- 448.. 
	 *   
	 *    Total of 59.8kg of CO2 was injected during the experiment (pg18)
	 *    and was started on day 5.08 (pg 19). Low plant chamber was 1200 PPM upper was 3900 
	 *    (graph pg 19). CO2 in chamber with Bob as low of 1251 and high of 7500 PPM. Since SWAD
	 *     was not used during phase 1, will set upper limited to 8000 PPM and put 
	 *     the low (start injection) at 1200 PPM.
	 */
	//private float envCO2RangePPM = 448;
	private float envCO2TargetPPM = 1251;
	private float envCO2HIGH = 9000; //envCO2TargetPPM + envCO2RangePPM;
	private float envCO2LOW = 1200; //envCO2TargetPPM - envCO2RangePPM;
	private float envCO2Inject = 20; // Amount to increment the injector by when adding CO2 to the env

    private StateMap continuousState;

    //private ActionMap myActionMap;  CIH 200718 never used

    private Map classifiedState;

    private Map<String, Map> thresholdMap = new TreeMap<String, Map>();

    private BioDriver myBioDriver;

    private BioHolder myBioHolder;

    private SimEnvironment myCrewEnvironment;

    private DirtyWaterStore myDirtyWaterStore;

    private GreyWaterStore myGreyWaterStore;

    private PotableWaterStore myPotableWaterStore;
 
    private O2Store myO2Store;
    
    private CO2Store myCO2Store;
    
    private H2Store myH2Store;
    
    private VCCR mySWAD;
    
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

    	
		ImageIcon moon = new ImageIcon(BiosimStandaloneLMLSTPController.class.getClassLoader()
				.getResource("com/traclabs/biosim/framework/moon.png"));
		BiosimStandaloneLMLSTPController myBiosimStandalone = new BiosimStandaloneLMLSTPController(moon,
				"BioSim: Advanced Life Support Simulation", filename, 500);
    	//Hand Controller merge insert code
		//myBiosimStandalone.collectReferences(); Cant do here, server starts in beginSimulation, move to after server start
		System.out.println("Main complete; starting simulation for BiosimStandaloneLMLSTPController");
		myBiosimStandalone.beginSimulation();
	}

	public BiosimStandaloneLMLSTPController(String xmlFilename, int driverPause) {
		this.myDriverPause = driverPause;
		this.myXmlFilename = xmlFilename;
		this.myServerThread = new Thread(new ServerThread());
		this.myClientThread = new Thread(new ClientThread());
		
	}

	public BiosimStandaloneLMLSTPController(ImageIcon splashIcon, String splashText,
			String xmlFilename, int driverPause) {
		this(xmlFilename, driverPause);
		startSpash(splashIcon, splashText);
	}
	
	private void startSpash(ImageIcon splashIcon, String splashText) {
		myProgressBar = new JProgressBar();
		myProgressBar.setIndeterminate(true);
		myFrame = new JFrame("BioSim Loader");
		myFrame.getContentPane().setLayout(new BorderLayout());
		ImageIcon biosimIcon = new ImageIcon(BiosimStandaloneLMLSTPController.class
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
		//myClientThread.start(); Replace with handcontroller code myController.runSim();
		//Add collectReferences line to init BioDriver (was in main of handcontroller) 
		collectReferences();
		// HandController Code
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
	
	/** From SimpleController
	 * If the oxygen in the cabin drifts below 10%, stop the sim.
	 */
	private boolean endConditionMet() {
		float oxygenPercentage = myO2ConcentrationSensor.getValue();
		myLogger.info("Sim End Condition Check - current O2 percent is "+ oxygenPercentage);
		return (oxygenPercentage < 0.1);
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
		//check sensor
		float O2sensorValue = myO2ConcentrationSensor.getValue();
		float CO2sensorValue = myCO2ConcentrationSensor.getValue();
		myLogger.info("Enviroment Gas readings: O2: " + O2sensorValue +" CO2: "+ CO2sensorValue );
		//Check O2 Concentration Level
		float oxygenPercentage = myO2ConcentrationSensor.getValue();  //CIH 200117 add conditional change in O2 actuator
		float myO2ActuatorValue = myO2InjectorActuator.getValue();
		float myO2StorageActuatorValue = myO2StorageActuator.getValue();
		float myO2ConcentratorValue = myO2ConcentratorActuator.getValue();
		float myO2ConcentratorStoreValue = myO2ConcentratorStoreActuator.getValue();
				
		//Take external action if O2 levels are 21.9 +/- .7
		//If O2 level is above 22.6. turn off O2 from tanks, turn on concentrator
		if (oxygenPercentage > envO2HIGH ){
			myLogger.debug("O2 is above HIGH threshold ("+ envO2HIGH +")- check Actuator and Concentrator");
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
			myLogger.info("O2 concentrator is set to: " + myO2ConcentratorActuator.getValue()+ " Actuator Max: " + myO2InjectorActuator.getMax());
			if (myO2ConcentratorActuator.getValue()< myO2ConcentratorActuator.getMax()){
				//Increase both Concentrator and stores
				myO2ConcentratorActuator.setValue(myO2ConcentratorValue + 10);
				myO2ConcentratorStoreActuator.setValue(myO2ConcentratorStoreValue + 10);
			}else{
				myLogger.info("O2 Concentrator is at max");
				myO2ConcentratorActuator.setValue(myO2ConcentratorActuator.getMax());
				myO2ConcentratorStoreActuator.setValue(myO2ConcentratorStoreActuator.getMax());
			}
			
			myLogger.info("O2 concentrator is set to: " + myO2ConcentratorActuator.getValue()+ " Actuator Max: " + myO2InjectorActuator.getMax());
		} else {
			//If O2 level is below 21.2, add O2 from stores
			if (oxygenPercentage < envO2LOW ){
				//Make Sure O2 Concentrator is off
				myLogger.info("02 is below LOW threshold, ensure O2 Concentrator is off ");
				myO2ConcentratorActuator.setValue(0);
				myO2ConcentratorStoreActuator.setValue(0);
				//If O2 is flowing from stores, increase injection rate.
				//Honor max injection value
				myLogger.info("O2 is below LOW threshold ("+ envO2LOW +")- incrementing O2 actuator by " + envO2Inject);
				myLogger.info("O2 actuator currently set at " + myO2InjectorActuator.getValue()+ " Actuator Max: " + myO2InjectorActuator.getMax());
				if (myO2InjectorActuator.getValue()< myO2InjectorActuator.getMax()){
					//Increase both injector and stores
					myO2InjectorActuator.setValue(myO2ActuatorValue + envO2Inject);
					myO2StorageActuator.setValue(myO2ActuatorValue + envO2Inject);
				}else{
					myLogger.info("O2 actuator is at max");
					myO2InjectorActuator.setValue(myO2InjectorActuator.getMax());
					myO2StorageActuator.setValue(myO2StorageActuator.getMax());
				}
			} else {
				myLogger.info("O2 is within thresholds - No O2 adjustments needed. Make sure Concentrator is turned off");
				//Make Sure O2 Concentrator is off
				myO2ConcentratorActuator.setValue(0);
				myO2ConcentratorStoreActuator.setValue(0);
			}
		}
		
		//Turn on O2 Concentrator if CO2 level is high
		//CO2 (PPM) 1251 +/- 448
		//Using 1 volume percent = 10,000 ppmv 
		myCO2PPMV = CO2sensorValue * 1000000; //( 10000*100 )
		myLogger.info("CO2 Level (%): "+ CO2sensorValue * 100 +" PPMV: "+ myCO2PPMV );
	
		//If CO2 level is above 1699, Remove CO2 from environment to stores
		if (myCO2PPMV > envCO2HIGH){
			//TO-DO test with acumulator setup in config file
			myLogger.debug("CO2 is above HIGH threshold ("+ envCO2HIGH +") - check actuator and turn on O2 Concentrator (SWAD)");
			//check actuator, turn off if running
			if (myCO2InjectorActuator.getValue()>0){
				myLogger.debug("Turning OFF CO2 injection from Stores");
				myCO2InjectorActuator.setValue(0);
				myCO2StorageActuator.setValue(0);
			} else {
				myLogger.debug("CO2 is NOT being injected from stores");
			}
			
		    //Check to see if SWAD is running
		    if (isSWADRunning()){
		    	//If it is running then increase flow if not currently at MAX
		    	float myCurrentCO2Flow = mySWAD.getCO2ProducerDefinition().getActualFlowRate(0);
		    	float myCurrentInFlow = mySWAD.getAirConsumerDefinition().getActualFlowRate(0);
		    	float myMAXCO2Flow = mySWAD.getCO2ProducerDefinition().getMaxFlowRate(0);
		    	float myMAXInFlow = mySWAD.getAirConsumerDefinition().getMaxFlowRate(0);
		    	myLogger.debug("Current SWAD intake is :"+ myCurrentInFlow +" and Current SWAD output of CO2 to CO2Store is: " + myCurrentCO2Flow );
		    	if (myCurrentCO2Flow >= myMAXCO2Flow && myCurrentInFlow >= myMAXInFlow){
		    		myLogger.debug("SWAD is running at MAX - O2 Flow: "+mySWAD.getCO2ProducerDefinition().getMaxFlowRate(0)+ 
		    				" CO2 Flow: " + mySWAD.getAirConsumerDefinition().getMaxFlowRate(0));
		    		//regulate flows to max incase they are higher
		    		mySWAD.getCO2ProducerDefinition().setDesiredFlowRate(myMAXCO2Flow, 0);
		    		mySWAD.getAirConsumerDefinition().setDesiredFlowRate(myMAXInFlow, 0);
		    	} else {
		    		//SWAD not at MAX for either input or output
		    		if (myCurrentInFlow < myMAXInFlow){
		    			//Increase the input rate
		    			myLogger.debug("Increasing SWAD input rate from module");
		    			mySWAD.getAirConsumerDefinition().setDesiredFlowRate(myCurrentInFlow + 100, 0);
		    			myLogger.debug("Set SWAD input from module to : "+ mySWAD.getAirConsumerDefinition().getDesiredFlowRate(0));
		    		} else {
		    			myLogger.debug("SWAD input from module is at MAX ("+mySWAD.getAirConsumerDefinition().getMaxFlowRate(0)+")");
		    		}
		    		if (myCurrentCO2Flow < myMAXCO2Flow){
		    			//Increase the output rate
		    			myLogger.debug("Increasing SWAD output to CO2 Store");
		    			mySWAD.getCO2ProducerDefinition().setDesiredFlowRate(myCurrentCO2Flow + 100, 0);
		    			myLogger.debug("Set SWAD Flow rate to CO2Store: "+ mySWAD.getCO2ProducerDefinition().getDesiredFlowRate(0));
		    		} else {
		    			myLogger.debug("SWAD output to CO2 Store is at MAX ("+mySWAD.getCO2ProducerDefinition().getMaxFlowRate(0)+")");
		    		}
		    		
		    	}
		    	myLogger.debug("Finished Adjusting SWAD output");
		    } else {
		    	myLogger.debug("SWAD is OFF - turning ON");
		    	float myPowerLevel = mySWAD.getPowerConsumerDefinition().getMaxFlowRate(0);
		    	mySWAD.getPowerConsumerDefinition().setDesiredFlowRate(myPowerLevel, 0);
		    	myLogger.debug("Set SWAD Power to :"+ mySWAD.getPowerConsumerDefinition().getDesiredFlowRate(0));
		    	myLogger.debug("SWAD power flow rate is :" + mySWAD.getPowerConsumerDefinition().getActualFlowRate(0));
		    }
		} else {
			myLogger.debug("CO2 is below HIGH threshold - Check SWAD - and Check LOW threshold");
			//Check if SWAD is running
			if (isSWADRunning()){
				//If it is running, turn it off
				mySWAD.getPowerConsumerDefinition().setDesiredFlowRate(0, 0);
				myLogger.debug("SWAD is turned OFF");
			} else {
				myLogger.debug("SWAD/VCCR is not running");
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
		//TO-DO Adjust light levels in growth chamber
		
		// advancing the sim 1 tick
		myBioDriver.advanceOneTick();
		myLogger.info("Storage Levels are: 02:"+myO2Store.getCurrentLevel()+" CO2:"+myCO2Store.getCurrentLevel()+" H2: "+myH2Store.getCurrentLevel());    
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
		myBioHolder = BioHolderInitializer.getBioHolder();
		myBioDriver = myBioHolder.theBioDriver;
		
		Injector myO2Injector = myBioHolder.theInjectors.get(1);
		Accumulator myO2Concentrator = myBioHolder.theAccumulators.get(0);

		//For Crew Quarters
		myO2InjectorActuator = (O2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2InFlowRateActuators, myO2Injector));
		
		myLogger.debug("myO2InjectorActuator is currently set to: "+ myO2InjectorActuator.getValue() + " Max:"+ myO2InjectorActuator.getMax() + " Min: " + myO2InjectorActuator.getMin() + 
					" for module "+	myO2InjectorActuator.getOutputModule().getModuleName());
		
		myO2ConcentratorActuator = (O2OutFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2OutFlowRateActuators, myO2Concentrator));
		
		myLogger.debug("myO2ConcentratorActuator is currently set to: "+ myO2ConcentratorActuator.getValue() + " Max:"+ myO2ConcentratorActuator.getMax() + " Min: " + myO2ConcentratorActuator.getMin() + 
				" for module "+	myO2ConcentratorActuator.getOutputModule().getModuleName());
		
		//FOr Storage Control
		myO2StorageActuator = (O2OutFlowRateActuator)(myBioHolder.getActuatorAttachedTo(myBioHolder.theO2OutFlowRateActuators, myO2Injector));
		myO2ConcentratorStoreActuator = (O2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theO2InFlowRateActuators, myO2Concentrator));
		
		myLogger.debug("myO2StorageActuator is currently set to: "+ myO2StorageActuator.getValue() + " Max:"+ myO2StorageActuator.getMax() + " Min: " + myO2StorageActuator.getMin() + 
				" for module "+	myO2StorageActuator.getOutputModule().getModuleName());
		
		myLogger.debug("myO2ConcentratorStoreActuator is currently set to: "+ myO2ConcentratorStoreActuator.getValue() + " Max:"+ myO2ConcentratorStoreActuator.getMax() + " Min: " + myO2ConcentratorStoreActuator.getMin() + 
				" for module "+	myO2ConcentratorStoreActuator.getOutputModule().getModuleName());
		
		Injector myCO2Injector = myBioHolder.theInjectors.get(0);
		
		//For Storage Control
		myCO2StorageActuator = (CO2InFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theCO2InFlowRateActuators, myCO2Injector));
		
		myLogger.debug("myCO2StorageActuator is currently set to : "+ myCO2StorageActuator.getValue() + " Max:"+ myCO2StorageActuator.getMax() + " Min: " + myCO2StorageActuator.getMin() + 
					" for module "+	myCO2StorageActuator.getOutputModule().getModuleName());
		//For Crew Quarters
       myCO2InjectorActuator = (CO2OutFlowRateActuator) (myBioHolder.getActuatorAttachedTo(myBioHolder.theCO2OutFlowRateActuators, myCO2Injector));
		
		myLogger.debug("myCO2InjectorActuator is currently set to : "+ myCO2InjectorActuator.getValue() + " Max:"+ myCO2InjectorActuator.getMax() + " Min: " + myCO2InjectorActuator.getMin() + 
					" for module "+	myCO2InjectorActuator.getOutputModule().getModuleName());
	

		SimEnvironment crewEnvironment = myBioHolder.theSimEnvironments.get(0);

		myO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(myBioHolder.theGasConcentrationSensors, crewEnvironment.getO2Store()));
		myLogger.debug("myO2ConcentrationSensor (crewEnv.O2Store)value is: " + myO2ConcentrationSensor.getValue());
		
		myO2Store = myBioHolder.theO2Stores.get(0);
		myLogger.debug("O2Store is :" +myO2Store.getCurrentLevel());
		
		myCO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(myBioHolder.theGasConcentrationSensors, crewEnvironment.getCO2Store()));
		myLogger.debug("myCO2ConcentrationSensor (crewEnv.CO2Store)value is: " + myCO2ConcentrationSensor.getValue());
		
		mySWAD = myBioHolder.theVCCRModules.get(0);
		myLogger.debug("mySWAD is set to: "+ mySWAD.getModuleName());
		myCO2Store = myBioHolder.theCO2Stores.get(0);
		myH2Store = myBioHolder.theH2Stores.get(0);
		
	}
	
	/**
	 * Checks to see if the SWAD is running or not
	 * @param an instance of the VCCR object from com.traclabs.biosim.server.simulation.air
	 * @return true if SWAD is running
	 * @return false if SWAD is not running
	 */
	private boolean isSWADRunning(){
		float mySWADPower = mySWAD.getPowerConsumerDefinition().getActualFlowRate(0);
		myLogger.debug("SWAD/VCCR power level is: "+ mySWADPower);
		if (mySWADPower >0 ){
			myLogger.debug("SWAD/VCCR is ON");
			return true;
		} else {
			myLogger.debug("SWAD/VCCR is OFF");
			return false;
		}
	}
	
}

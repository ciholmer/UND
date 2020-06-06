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

import com.traclabs.biosim.client.control.ActionMap;
import com.traclabs.biosim.client.control.StateMap;
import com.traclabs.biosim.client.framework.BiosimMain;
import com.traclabs.biosim.client.util.BioHolder;
import com.traclabs.biosim.client.util.BioHolderInitializer;
import com.traclabs.biosim.idl.actuator.framework.GenericActuator;
import com.traclabs.biosim.idl.framework.BioDriver;
import com.traclabs.biosim.idl.sensor.framework.GenericSensor;
import com.traclabs.biosim.idl.sensor.framework.StoreLevelSensor;
import com.traclabs.biosim.idl.simulation.air.O2Store;
import com.traclabs.biosim.idl.simulation.air.CO2Store;
import com.traclabs.biosim.idl.simulation.air.H2Store;
import com.traclabs.biosim.idl.simulation.air.OGS;
import com.traclabs.biosim.idl.framework.LogLevel;
import com.traclabs.biosim.idl.framework.MalfunctionIntensity;
import com.traclabs.biosim.idl.framework.MalfunctionLength;
import com.traclabs.biosim.idl.simulation.environment.SimEnvironment;
import com.traclabs.biosim.idl.simulation.framework.Injector;
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
 * 
 * Modified code to include with com.traclabs.biosim.client.control.SimpleController code and calls to observe simple controller execution.
 */

public class BiosimStandaloneSimpleController {

	private Thread myServerThread;

	private Thread myClientThread;

	private ReadyListener myReadyListener;

	private JFrame myFrame;

	private JProgressBar myProgressBar;

	private String myXmlFilename;

	private int myDriverPause;
	
	//Hand Controller Values 090819
	
	private GenericSensor myO2ConcentrationSensor;
	
	private GenericSensor myCO2ConcentrationSensor;

	private GenericActuator myO2InjectorAcutator;
	
	//malfunction stuff
	//private MalfunctionIntensity myMalfIntensity = MalfunctionIntensity.SEVERE_MALF;
	
	//private MalfunctionLength myMalfLength = MalfunctionLength.TEMPORARY_MALF;
	
	//feedback loop stuff
    private float levelToKeepO2At = 0.20f;

    private float levelToKeepCO2At = 0.00111f;

    private float crewO2integral = 0f;

    private float crewCO2integral = 0f;

    private final static String TAB = "\t";

    // hand controller stuff;

    private StateMap continuousState;

    private ActionMap myActionMap;

    private Map classifiedState;

    private Map<String, Map> thresholdMap = new TreeMap<String, Map>();

    private BioDriver myBioDriver;

    private BioHolder myBioHolder;

    private SimEnvironment myCrewEnvironment;

    private DirtyWaterStore myDirtyWaterStore;

    private GreyWaterStore myGreyWaterStore;

    private PotableWaterStore myPotableWaterStore;
    
    private OGS myOGS;

    private O2Store myO2Store;
    
    private CO2Store myCO2Store;
    
    private H2Store myH2Store;

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

    private GenericSensor myO2AirConcentrationSensor;

    private GenericSensor myCO2AirConcentrationSensor;

    private Injector myO2Injector;

    private Injector myCO2Injector;

    private GenericActuator myO2InInjectorAcutator;

    private GenericActuator myO2AirEnvironmentOutInjectorAcutator;

    private GenericActuator myCO2AirStoreInInjectorAcutator;

    private GenericActuator myCO2AirEnvironmentOutInjectorAcutator;

    private float myO2AirStoreInInjectorMax;

    private float myCO2AirStoreInInjectorMax;
    
	public static void main(String args[]) {
		String filename = "default.biosim";
		if (args.length > 0) {
			filename = BiosimMain.getArgumentValue(args[0]);
		}
    	System.out.println("Class path is: " + System.getProperty("java.class.path"));
    	System.out.println("Using  file: " + filename);

    	
		ImageIcon moon = new ImageIcon(BiosimStandaloneSimpleController.class.getClassLoader()
				.getResource("com/traclabs/biosim/framework/moon.png"));
		BiosimStandaloneSimpleController myBiosimStandalone = new BiosimStandaloneSimpleController(moon,
				"BioSim: Advanced Life Support Simulation", filename, 500);
    	//Hand Controller merge insert code
		//myBiosimStandalone.collectReferences(); Cant do here, server starts in beginSimulation, move to after server start
		myBiosimStandalone.beginSimulation();
	}

	public BiosimStandaloneSimpleController(String xmlFilename, int driverPause) {
		this.myDriverPause = driverPause;
		this.myXmlFilename = xmlFilename;
		this.myServerThread = new Thread(new ServerThread());
		this.myClientThread = new Thread(new ClientThread());
		
	}

	public BiosimStandaloneSimpleController(ImageIcon splashIcon, String splashText,
			String xmlFilename, int driverPause) {
		this(xmlFilename, driverPause);
		startSpash(splashIcon, splashText);
	}
	
	private void startSpash(ImageIcon splashIcon, String splashText) {
		myProgressBar = new JProgressBar();
		myProgressBar.setIndeterminate(true);
		myFrame = new JFrame("BioSim Loader");
		myFrame.getContentPane().setLayout(new BorderLayout());
		ImageIcon biosimIcon = new ImageIcon(BiosimStandalone.class
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
		myLogger.info("Controller starting run, setting up enviroment ");
		stepSim();
		myLogger.info("Envorment setup complete, starting sim loop");
		while (!endConditionMet())
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
		myLogger.info("Sim End Condition Check, current O2 percent is "+ oxygenPercentage);
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
		//set the actuator if O2 level is low otherwise set to 0
		float oxygenPercentage = myO2ConcentrationSensor.getValue();  //CIH 200117 add conditional change in O2 actuator
		float actuatorValue = myO2InjectorAcutator.getValue();
				
		//Turn off OGS when O2 is above 18% and the OGS is running,
		if (oxygenPercentage > 0.18 && isOGSRunning()){
			myLogger.debug("Turning off OGS");
			//myOGS.setEnableFailure(true);
			myOGS.startMalfunction(MalfunctionIntensity.SEVERE_MALF, MalfunctionLength.TEMPORARY_MALF);
			myLogger.debug("OGS is currently running: " + isOGSRunning());
		} else {
			myLogger.debug("OGS is currently running: " + isOGSRunning());
		}
		//Turn on OGS when O2 is below 12% and the OGS is NOT running
		if (oxygenPercentage <= 0.12 && !isOGSRunning()){
			myLogger.debug("Turning on OGS");
			myOGS.clearAllMalfunctions();
			myLogger.debug("OGS is currently running: " + isOGSRunning());
		}
		
		//Add O2 from stores when O2 is below 17%
		if (oxygenPercentage < 0.17){
			myLogger.info("incrementing O2 actuator by 10");
			myLogger.info("O2 actuator currently set at " + myO2InjectorAcutator.getValue()+ " Actuator Max: " + myO2InjectorAcutator.getMax());
			if (myO2InjectorAcutator.getValue()< myO2InjectorAcutator.getMax()){
				myO2InjectorAcutator.setValue(actuatorValue + 10);
			}else{
				myLogger.info("O2 actuator is at max");
				myO2InjectorAcutator.setValue(myO2InjectorAcutator.getMax());
			}
			myLogger.info("O2 actuator now at " + myO2InjectorAcutator.getValue());
		} else {
			myLogger.info("setting actuator to 0");
			myO2InjectorAcutator.setValue(0);
			myLogger.info("O2 actuator set at " + myO2InjectorAcutator.getValue());
		}
		
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
		
		myOGS = myBioHolder.theOGSModules.get(0);
		myLogger.debug("OGS is currently running: " + isOGSRunning());

		Injector O2Injector = myBioHolder.theInjectors.get(0);

		myO2InjectorAcutator = (myBioHolder.getActuatorAttachedTo(
				myBioHolder.theO2InFlowRateActuators, O2Injector));
		
		myLogger.debug("myO2InjectorActuator is currently set to: "+ myO2InjectorAcutator.getValue() + " Max:"+ myO2InjectorAcutator.getMax() + " Min: " + myO2InjectorAcutator.getMin() + 
					" for module "+	myO2InjectorAcutator.getOutputModule().getModuleName());

		SimEnvironment crewEnvironment = myBioHolder.theSimEnvironments.get(0);

		myO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(myBioHolder.theGasConcentrationSensors, crewEnvironment.getO2Store()));
		myLogger.debug("myO2ConcentrationSensor (crewEnv.O2Store)value is: " + myO2ConcentrationSensor.getValue());
		
		myO2Store = myBioHolder.theO2Stores.get(0);
		myLogger.debug("O2Store is :" +myO2Store.getCurrentLevel());
		
		myCO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(myBioHolder.theGasConcentrationSensors, crewEnvironment.getCO2Store()));
		myLogger.debug("myCO2ConcentrationSensor (crewEnv.CO2Store)value is: " + myCO2ConcentrationSensor.getValue());
		
		myCO2Store = myBioHolder.theCO2Stores.get(0);
		myH2Store = myBioHolder.theH2Stores.get(0);
		
	}
	
	/**
	 * Checks to see if the OGS is running. OGS is controlled by malfunctions in this instance
	 * @return true if the OGS does not have a malfunction
	 * @return false if the OGS has a malfunction
	 */
	private Boolean isOGSRunning(){
		boolean OGSstate;
		if (myOGS.isMalfunctioning()){
			//OGS is currently malfunction, it is not running
			OGSstate = false;
		} else {
			//OGS is currently NOT malfunctioning, it is running
			OGSstate = true;
		}
		return OGSstate;
	}

}

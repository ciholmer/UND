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

import org.apache.log4j.Logger;

import com.traclabs.biosim.client.control.ActionMap;
import com.traclabs.biosim.client.control.StateMap;
import com.traclabs.biosim.client.framework.BiosimMain;
import com.traclabs.biosim.client.util.BioHolder;
import com.traclabs.biosim.client.util.BioHolderInitializer;
import com.traclabs.biosim.idl.actuator.framework.GenericActuator;
import com.traclabs.biosim.idl.framework.BioDriver;
import com.traclabs.biosim.idl.sensor.framework.GenericSensor;
import com.traclabs.biosim.idl.simulation.air.O2Store;
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
 * Modified code for integration into thesis simulations
 * BioPlex Phase 1 and II, Thesis early warning indicators test
 */

public class BiosimStandaloneController {

	private Thread myServerThread;

	private Thread myClientThread;

	private ReadyListener myReadyListener;

	private JFrame myFrame;

	private JProgressBar myProgressBar;

	private String myXmlFilename;

	private int myDriverPause;
	
	//Hand Controller Values 090819
	
	private GenericSensor myO2ConcentrationSensor;

	private GenericActuator myO2InjectorAcutator;
	
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

    private O2Store myO2Store;

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

    	
		ImageIcon moon = new ImageIcon(BiosimStandaloneController.class.getClassLoader()
				.getResource("com/traclabs/biosim/framework/moon.png"));
		BiosimStandaloneController myBiosimStandalone = new BiosimStandaloneController(moon,
				"BioSim: Advanced Life Support Simulation", filename, 500);
    	//Hand Controller merge insert code
		//myBiosimStandalone.collectReferences(); CIH - not needed, done again in begin.Sim code
		myBiosimStandalone.beginSimulation();
	}

	public BiosimStandaloneController(String xmlFilename, int driverPause) {
		this.myDriverPause = driverPause;
		this.myXmlFilename = xmlFilename;
		this.myServerThread = new Thread(new ServerThread());
		this.myClientThread = new Thread(new ClientThread());
		
	}

	public BiosimStandaloneController(ImageIcon splashIcon, String splashText,
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
		//myClientThread.start(); Replace with handcontroller code
		// HandController Code
		collectReferences();
		myLogger = Logger.getLogger(this.getClass());
        setThresholds();
        continuousState = new StateMap();
        myActionMap = new ActionMap(myXmlFilename);
        
        if (myO2Injector != null) {
            myO2AirStoreInInjectorMax = myO2InInjectorAcutator.getMax();
        }

        if (myCO2Injector != null) {
            myCO2AirStoreInInjectorMax = myCO2AirStoreInInjectorAcutator
                    .getMax();
        }
		 myBioDriver.setPauseSimulation(true);
	        myBioDriver.startSimulation();
	        myLogger.info("Hand Controller starting run");
	        while (!myBioDriver.isDone())
	            stepSim();
	        myLogger.info("Controller ended on tick "+myBioDriver.getTicks());
	        
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
		myLogger.info("Hand Controller Sim Step for tick" +myBioDriver.getTicks());
		//check sensor
		float sensorValue = myO2ConcentrationSensor.getValue();
		myLogger.info("02 Concentrration sensor reading is " + sensorValue + " in " + myO2ConcentrationSensor.getModuleName());
		//set actuators
		doO2Injector();
		doCO2Injector();
		// advancing the sim 1 tick
		myBioDriver.advanceOneTick();
		myLogger.info("Hand Controller finished Sim Step for tick" +myBioDriver.getTicks());
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
		String XMLConfigFile = getXmlFilename();
				
		if(XMLConfigFile != null && !XMLConfigFile.isEmpty()) {
			BioHolderInitializer.setFile(XMLConfigFile);
		}
        myBioHolder = BioHolderInitializer.getBioHolder();
        myBioDriver = myBioHolder.theBioDriver;

        /*orig hand controller code 'CIH 1908928
        myO2Injector = myBioHolder.theInjectors.get(1);

        if (myBioHolder.theInjectors.size() >= 3) {
            myCO2Injector = myBioHolder.theInjectors.get(2);
        }
       
        myCO2Injector = myBioHolder.theInjectors.get(0);// 'CIH 1908928 position in LMLSTP Config when using O2 and CO2 seperate Injectors
        myO2Injector = myBioHolder.theInjectors.get(1);// 'CIH 1908928 position in LMLSTP Config when using O2 and CO2 seperate Injectors
        */
        myO2Injector = myBioHolder.theInjectors.get(0); //'CIH 191110 refactor configfile for 1 injector instantiation
        myCO2Injector = myBioHolder.theInjectors.get(0);//'CIH 191110 refactor configfile for 1 injector instantiation

        myDirtyWaterStore = myBioHolder.theDirtyWaterStores
                .get(0);
        myPotableWaterStore = myBioHolder.thePotableWaterStores
                .get(0);
        myGreyWaterStore = myBioHolder.theGreyWaterStores
                .get(0);

        myO2Store = myBioHolder.theO2Stores.get(0);

        myCrewEnvironment = myBioHolder.theSimEnvironments
                .get(0);

        myO2InInjectorAcutator = (myBioHolder
                .getActuatorAttachedTo(
                		myBioHolder.theO2InFlowRateActuators,
                		myO2Injector));

        myO2AirEnvironmentOutInjectorAcutator = (myBioHolder
                .getActuatorAttachedTo(
                        myBioHolder.theO2OutFlowRateActuators,
                        myO2Injector));

        myO2AirConcentrationSensor = (myBioHolder
                .getSensorAttachedTo(myBioHolder.theGasConcentrationSensors,
                        myCrewEnvironment.getO2Store()));
        
        /*Original Code  'CIH 1908928
        Injector O2Injector = myBioHolder.theInjectors.get(1);
 		*/
        //Injector O2Injector = myO2Injector;  //'CIH 190829 set to value determined earlier
        
		myO2InjectorAcutator = (myBioHolder.getActuatorAttachedTo(
				myBioHolder.theO2InFlowRateActuators, myO2Injector));

		SimEnvironment crewEnvironment = myBioHolder.theSimEnvironments.get(0);

		myO2ConcentrationSensor = (myBioHolder.getSensorAttachedTo(
				myBioHolder.theGasConcentrationSensors, crewEnvironment
						.getO2Store()));

        if (myCO2Injector != null) {
            myCO2AirStoreInInjectorAcutator = (myBioHolder
                    .getActuatorAttachedTo(
                            myBioHolder.theCO2InFlowRateActuators,
                            myCO2Injector));

            myCO2AirEnvironmentOutInjectorAcutator = (myBioHolder
                    .getActuatorAttachedTo(
                            myBioHolder.theCO2OutFlowRateActuators,
                            myCO2Injector));

            myCO2AirConcentrationSensor = (myBioHolder
                    .getSensorAttachedTo(
                            myBioHolder.theGasConcentrationSensors,
                            myCrewEnvironment.getCO2Store()));
        }
    }
	
	public void setThresholds() {
        // sets up the threshold map variable
        int dirtyWaterHighLevel = (int) myDirtyWaterStore.getCurrentCapacity();
        int dirtyWaterLowLevel = dirtyWaterHighLevel / 3;
        int greyWaterHighLevel = (int) myGreyWaterStore.getCurrentCapacity();
        int greyWaterLowLevel = greyWaterHighLevel / 3;
        int potableWaterHighLevel = (int) myPotableWaterStore
                .getCurrentCapacity();
        int potableWaterLowLevel = potableWaterHighLevel / 3;
        int O2StoreHighLevel = (int) myO2Store.getCurrentCapacity();
        int O2StoreLowLevel = O2StoreHighLevel / 3;

        Map<Integer, Integer> dirtyWaterSubMap = new TreeMap<Integer, Integer>();
        dirtyWaterSubMap.put(LOW, new Integer(dirtyWaterLowLevel));
        dirtyWaterSubMap.put(HIGH, new Integer(dirtyWaterHighLevel));
        thresholdMap.put("dirtywater", dirtyWaterSubMap);

        Map<Integer, Integer> greyWaterSubMap = new TreeMap<Integer, Integer>();
        greyWaterSubMap.put(LOW, new Integer(greyWaterLowLevel));
        greyWaterSubMap.put(HIGH, new Integer(greyWaterHighLevel));
        thresholdMap.put("greywater", greyWaterSubMap);

        Map<Integer, Integer> oxygenSubMap = new TreeMap<Integer, Integer>();
        oxygenSubMap.put(LOW, new Integer(O2StoreLowLevel));
        oxygenSubMap.put(HIGH, new Integer(O2StoreHighLevel));
        thresholdMap.put("oxygen", oxygenSubMap);

        Map<Integer, Integer> potableWaterSubMap = new TreeMap<Integer, Integer>();
        potableWaterSubMap.put(LOW, new Integer(potableWaterLowLevel));
        potableWaterSubMap.put(HIGH, new Integer(potableWaterHighLevel));
        thresholdMap.put("potablewater", potableWaterSubMap);
    }

    public Map classifyState(StateMap instate) {
        Map<String, Integer> state = new TreeMap<String, Integer>();

        Map thisSet;
        StringBuffer fileoutput;

        fileoutput = new StringBuffer(myBioDriver.getTicks());
        fileoutput.append(TAB);

        for (int i = 0; i < stateNames.length; i++) {

            thisSet = thresholdMap.get(stateNames[i]);
            fileoutput.append(instate.getStateValue(stateNames[i]));
            fileoutput.append(TAB);
            if (instate.getStateValue(stateNames[i]) < ((Integer) thisSet
                    .get(LOW)).intValue())
                state.put(stateNames[i], LOW);
            else if (instate.getStateValue(stateNames[i]) > ((Integer) thisSet
                    .get(HIGH)).intValue())
                state.put(stateNames[i], HIGH);
            else
                state.put(stateNames[i], NORMAL);
        }
        return state;
    }

    private void doO2Injector() {
        //crew O2 feedback control
        float crewO2p = 100f;
        float crewO2i = 5f;
        float crewO2 = myO2AirConcentrationSensor.getValue();
        float delta = levelToKeepO2At - crewO2;
        crewO2integral += delta;
        float signal = (delta * crewO2p + crewO2i * crewO2integral);
        float valueToSet = Math.min(myO2AirStoreInInjectorMax, signal);
        myLogger.debug("Level to keep O2 at is " + levelToKeepO2At +" ... setting O2 injector to " + valueToSet);
        valueToSet = Math.min(myO2AirStoreInInjectorMax, signal);
        myO2InInjectorAcutator.setValue(valueToSet);
        myO2AirEnvironmentOutInjectorAcutator.setValue(valueToSet);
    }

    private void doCO2Injector() {
        //crew O2 feedback control
        float crewCO2p = 100f;
        float crewCO2i = 5f;
        float crewCO2 = myCO2AirConcentrationSensor.getValue();
        float delta = levelToKeepCO2At - crewCO2;
        crewCO2integral += delta;
        float signal = (delta * crewCO2p + crewCO2i * crewCO2integral);
        float valueToSet = Math.min(myCO2AirStoreInInjectorMax, signal);
        myLogger.debug("setting CO2 injector to " + valueToSet);
        valueToSet = Math.min(myCO2AirStoreInInjectorMax, signal);
        myCO2AirStoreInInjectorAcutator.setValue(valueToSet);
        myCO2AirEnvironmentOutInjectorAcutator.setValue(valueToSet);
    }

    /**
     * @param pO2AirStoreInInjectorMax
     *            The myO2AirStoreInInjectorMax to set.
     */
    public void setO2AirStoreInInjectorMax(float pO2AirStoreInInjectorMax) {
        myO2AirStoreInInjectorMax = pO2AirStoreInInjectorMax;
    }

    /**
     * @param pO2AirStoreInInjectorMax
     *            The myO2AirStoreInInjectorMax to set.
     */
    public void setCO2AirStoreInInjectorMax(float pCO2AirStoreInInjectorMax) {
        myCO2AirStoreInInjectorMax = pCO2AirStoreInInjectorMax;
    }

}

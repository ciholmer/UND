package com.traclabs.biosim.server.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.tigris.gef.graph.presentation.JGraph;

/**
 * @author scott
 */
public class BiosimEditor {
    JFrame myMainFrame;
    JGraph myGraph;
    JTabbedPane myTabbedPane;
    Logger myLogger;
    
    public BiosimEditor(){
        initLogger();
        buildGui();
    }
    
    
    /**
     * 
     */
    private void buildGui() {
        //init graph
        myGraph = new JGraph();
        myGraph.setDrawingSize(640,480);
        
        //init tabbed pane tool bar
        myTabbedPane = new JTabbedPane();
        JComponent airPanel = makeTextPanel("Air Panel");
        myTabbedPane.addTab("Air", createImageIcon("com/traclabs/biosim/client/water/gui/water.jpg"), airPanel);
        JComponent crewPanel = makeTextPanel("Crew Panel");
        myTabbedPane.addTab("Crew", createImageIcon("com/traclabs/biosim/client/crew/gui/crew.jpg"), crewPanel);
        JComponent environmentPanel = makeTextPanel("Environment Panel");
        myTabbedPane.addTab("Environment", createImageIcon("com/traclabs/biosim/client/environment/gui/environment.jpg"), environmentPanel);
        JComponent frameworkPanel = makeTextPanel("Framework Panel");
        myTabbedPane.addTab("Framework", createImageIcon("com/traclabs/biosim/client/framework/gui/all.jpg"), frameworkPanel);
        JComponent powerPanel = makeTextPanel("Power Panel");
        myTabbedPane.addTab("Power", createImageIcon("com/traclabs/biosim/client/power/gui/power.jpg"), powerPanel);
        JComponent wastePanel = makeTextPanel("Waste Panel");
        myTabbedPane.addTab("Waste", createImageIcon("com/traclabs/biosim/client/framework/gui/gear.gif"), wastePanel);
        JComponent waterPanel = makeTextPanel("Water Panel");
        myTabbedPane.addTab("Water", createImageIcon("com/traclabs/biosim/client/water/gui/water.jpg"), waterPanel);
        //myTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        myMainFrame = new JFrame();
        //do Gridbag
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        myMainFrame.getContentPane().setLayout(gridbag);
        
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipadx = 45;
        gridbag.setConstraints(myTabbedPane, constraints);
        myMainFrame.getContentPane().add(myTabbedPane);
        
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        gridbag.setConstraints(myGraph, constraints);
        myMainFrame.getContentPane().add(myGraph);
        
        //set size, pack, show
        myMainFrame.setSize(640, 480);
        myMainFrame.pack();
        myMainFrame.setVisible(true);
    }
    
    private JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    private static ImageIcon createImageIcon(String path) {
        URL imgURL = ClassLoader.getSystemClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            Logger.getLogger(BiosimEditor.class.toString()).error("Couldn't find file for icon: " + path);
            return null;
        }
    }

    /**
     * 
     */
    private void initLogger() {
        Properties logProps = new Properties();
        logProps.setProperty("log4j.appender.editorAppender",
                "org.apache.log4j.ConsoleAppender");
        logProps.setProperty("log4j.appender.editorAppender.layout",
                "org.apache.log4j.PatternLayout");
        logProps.setProperty(
                "log4j.appender.editorAppender.layout.ConversionPattern",
                "%5p [%c] - %m%n");
        logProps.setProperty("log4j.logger.org.tigris.gef","INFO, editorAppender");
        logProps.setProperty("log4j.logger.com.traclabs.biosim","INFO, editorAppender");
        PropertyConfigurator.configure(logProps);
        myLogger = Logger.getLogger(BiosimEditor.class.toString());
    }


    public static void main(String args[]){
        BiosimEditor editor = new BiosimEditor();
    }
}

package biosim.client.environment.gui;

import biosim.client.framework.*;
import biosim.idl.environment.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.text.*;

public class EnvironmentPanel extends JPanel implements BioSimulatorListener
{
	private JLabel tickLabel;
	private JPanel tickPanel;
	private JLabel O2Label;
	private JLabel CO2Label;
	private JLabel otherLabel;
	private JPanel airPanel;
	private SimEnvironment mySimEnvironment;
	private BioSimulator myBioSimulator;
	private DecimalFormat numFormat;

	public EnvironmentPanel(BioSimulator pBioSimulator){
		myBioSimulator = pBioSimulator;
		mySimEnvironment = (SimEnvironment)(myBioSimulator.getBioModule(BioSimulator.simEnvironmentName));
		buildGui();
		myBioSimulator.registerListener(this);
	}

	private void buildGui(){
		numFormat = new DecimalFormat("#,###.00;(#)");
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		
		long ticksExpired = mySimEnvironment.getTicks();
		tickLabel = new JLabel(ticksExpired + " hours ("+(ticksExpired/24)+" days)");
		tickPanel = new JPanel();
		tickPanel.setBorder(BorderFactory.createTitledBorder("Time"));
		tickPanel.add(tickLabel, BorderLayout.CENTER);

		airPanel = new JPanel();
		airPanel.setLayout(new GridLayout(3,1));
		airPanel.setBorder(BorderFactory.createTitledBorder("Air"));
		O2Label =    new JLabel("O2:     "+numFormat.format(mySimEnvironment.getO2Level()) +" L");
		CO2Label =  new JLabel("CO2:   "+numFormat.format(mySimEnvironment.getCO2Level()) + " L");
		otherLabel = new JLabel("other:  "+numFormat.format(mySimEnvironment.getOtherLevel()) + " L");
		airPanel.add(O2Label);
		airPanel.add(CO2Label);
		airPanel.add(otherLabel);

		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.gridwidth = 1;
		c.weighty = 1.0;
		gridbag.setConstraints(tickPanel, c);
		add(tickPanel);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 2.0;
		c.weighty = 1.0;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(airPanel, c);
		add(airPanel);
	}

	public void processTick(){
		long ticksExpired = mySimEnvironment.getTicks();
		tickLabel.setText(ticksExpired + " hours ("+(ticksExpired/24)+" days)");
		O2Label.setText("O2:     "+numFormat.format(mySimEnvironment.getO2Level()) +" L");
		CO2Label.setText("CO2:   "+numFormat.format(mySimEnvironment.getCO2Level()) + " L");
		otherLabel.setText("other:  "+numFormat.format(mySimEnvironment.getOtherLevel()) + " L");
	}
}

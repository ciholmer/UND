/*
 * Created on Apr 28, 2005
 *
 */
package com.traclabs.biosim.client.simulation.power.schematic.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.traclabs.biosim.client.simulation.power.schematic.graph.FigActiveNode;
import com.traclabs.biosim.client.simulation.power.schematic.graph.ModuleNode;
import com.traclabs.biosim.idl.simulation.framework.SimBioModule;


public class ActivePropertiesFrame extends JFrame {
    private JButton myOKButton;

    private SimBioModule mySimBioModule;
    
    private FigActiveNode myFigActiveNode;

    public ActivePropertiesFrame(FigActiveNode pNode) {
        myFigActiveNode = pNode;
        ModuleNode owner = (ModuleNode) pNode.getOwner();
        mySimBioModule = owner.getSimBioModule();
        myOKButton = new JButton(new OKAction());

        setLayout(new GridLayout(2, 2));
        add(new JLabel("Name: "+mySimBioModule.getModuleName()));
        add(myOKButton);
        setTitle(pNode.getText() + " Properties");
    }

    public JPanel getModuleSpecificPanel(){
        return new JPanel();
    }
    
    /**
     * Action that auto-arranges internal menus
     */
    private class OKAction extends AbstractAction {
        public OKAction() {
            super("OK");
        }

        public void actionPerformed(ActionEvent ae) {
            myFigActiveNode.update();
            dispose();
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.jacorb.naming.*;
import biosim.server.framework.*;
import biosim.client.framework.*;

/**
 * A standalone BioSim instance (server, nameserver, client in one)
 *
 * @author    Scott Bell
 */	

public class BiosimStandalone
{
	private Thread myNamingServiceThread;
	private Thread myServerThread;
	private Thread myClientThread;
	private Thread waitThread;
	private ReadyListener myReadyListener;
	private static final int PORT_NUMBER=11200;
	private static final String XML_INIT_FILENAME="biosim/server/framework/DefaultInitialization.xml";
	
	private JFrame myFrame;
	private JProgressBar myProgressBar;

	public static void main(String args[]){
		System.getProperties().setProperty("ORBInitRef.NameService", "corbaloc::127.0.0.1:11200/StandardNS/NameServer-POA/_root");
		System.getProperties().setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
		System.getProperties().setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
		BiosimStandalone myBiosimStandalone = new BiosimStandalone();
		myBiosimStandalone.beginSimulation();
	}
	
	public BiosimStandalone(){
		myNamingServiceThread = new Thread(new NamingServiceThread());
		myServerThread = new Thread(new ServerThread());
		myClientThread = new Thread(new ClientThread());
		myProgressBar = new JProgressBar();
		myProgressBar.setIndeterminate(true);
		myFrame = new JFrame("BioSim Loader");
		myFrame.getContentPane().setLayout(new BorderLayout());
		ImageIcon marsIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("mars.gif"));
		JLabel waitLabel = new JLabel("BioSim: Advanced Life Support Simulation", marsIcon, SwingConstants.CENTER);
		myFrame.setUndecorated(true);
		myFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		myFrame.getContentPane().add(waitLabel, BorderLayout.CENTER);
		myFrame.getContentPane().add(myProgressBar, BorderLayout.SOUTH);
		Dimension winsize = myFrame.getSize(), screensize = Toolkit.getDefaultToolkit().getScreenSize();
		myFrame.pack();
		myFrame.setLocation((screensize.width - winsize.width - 320) / 2,(screensize.height - winsize.height - 200) / 2);
		myReadyListener = new ReadyListener();
	}
	
	public void beginSimulation(){
		myFrame.setCursor(Cursor.WAIT_CURSOR);
		myFrame.setVisible(true);
		myNamingServiceThread.start();
		//myServerThread.start();
		//myClientThread.start();
	}

	private class NamingServiceThread implements Runnable{
	        public void run(){
			NameServer myNameserver = new NameServer();
			String[] nameArgs = {"-p", ""+PORT_NUMBER};
			myNameserver.main(nameArgs);
	        }
	}
	
	private class ServerThread implements Runnable{
	        public void run(){
			BiosimServer myBiosimServer = new BiosimServer(0, 500, XML_INIT_FILENAME);
			myBiosimServer.addReadyListener(myReadyListener);
			myBiosimServer.runServer("BiosimServer (id="+0+")");
	        }
	}
	
	private class ClientThread implements Runnable{
	        public void run(){
			BiosimMain myBiosimClient = new BiosimMain();
			String[] emptyArgs = new String[0];
			myBiosimClient.main(emptyArgs);
	        }
	}
	
	public class ReadyListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			myFrame.setCursor(Cursor.DEFAULT_CURSOR);
			myFrame.dispose();
		}
	}
}


/*
 * Created on Oct 27, 2004
 *
 */
package com.traclabs.biosim.server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Scott Bell
 */
public class MatlabTestBackend {
	private ServerSocket myIncomingSocket;

	private Logger myLogger;

	private double[] myCurrentInputData;

	private DataOutputStream mySocketDataOutputStream;

	private BufferedWriter mySocketTextWriter;

	private DataInputStream mySocketDataInputStream;

	private BufferedReader mySocketTextReader;

	public MatlabTestBackend(int pPortToListen) {
		myLogger = Logger.getLogger(this.getClass().toString());
		try {
			myIncomingSocket = new ServerSocket(pPortToListen);
		} catch (IOException e) {
			myLogger.error("Couldn't create server socket!");
			myLogger.error(e);
		}
	}

	public static void main(String args[]) {
		Properties logProps = new Properties();
		logProps.setProperty("log4j.appender.matlabAppender",
				"org.apache.log4j.ConsoleAppender");
		logProps.setProperty("log4j.appender.matlabAppender.layout",
				"org.apache.log4j.PatternLayout");
		logProps.setProperty(
				"log4j.appender.matlabAppender.layout.ConversionPattern",
				"%5p [%c] - %m%n");
		logProps.setProperty("log4j.logger." + MatlabTestBackend.class,
				"DEBUG, matlabAppender");
		PropertyConfigurator.configure(logProps);

		int serverPort = MatlabAceEngine.DEFAULT_PORT;
		if (args.length > 1) {
			Logger
					.getLogger(MatlabTestBackend.class)
					.error(
							"usage: java com.traclabs.biosim.server.util.MatlabTestBackend [server port]");
			return;
		} else if (args.length == 1) {
			try {
				serverPort = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				Logger.getLogger(MatlabTestBackend.class).error(e);
				Logger.getLogger(MatlabTestBackend.class).error(
						"Had problems parsing server port you specified: "
								+ args[0]);
				return;
			}
		}
		MatlabTestBackend newMatlabTestBackend = new MatlabTestBackend(
				serverPort);
		newMatlabTestBackend.run();
	}

	/**
	 *  
	 */
	private void run() {
		myLogger.debug("Starting server on port "+myIncomingSocket.getLocalPort());
		while (true) {
			try {
				Socket clientSocket = myIncomingSocket.accept();
				myLogger.debug("Socket connection");
				handleSocketConnection(clientSocket);

			} catch (IOException e) {
				myLogger.error(e);
				myLogger.error("Problem with server socket");
			}
		}
	}

	/**
	 * @param clientSocket
	 */
	private void handleSocketConnection(Socket clientSocket) {
		try {
			initializeStreams(clientSocket);
			if (isBiosimClient()) {
				myLogger.debug("Client connected!");
				boolean clientWantsToLeave = false;
				while (!clientWantsToLeave) {
					String operationRequested = determineOperationRequested();
					if (operationRequested.equals(MatlabAceEngine.PUT_REQUEST))
						handlePutRequest();
					else if (operationRequested
							.equals(MatlabAceEngine.GET_REQUEST))
						handleGetRequest();
					else if (operationRequested
							.equals(MatlabAceEngine.CLIENT_BYE))
						clientWantsToLeave = true;
					else
						myLogger.warn("Unknown operation requested: "
								+ operationRequested);
				}
			} else {
				myLogger
						.debug("Non-biosim client attempted to connect");
			}
			myLogger.debug("closing socket");
			clientSocket.close();

		} catch (IOException e) {
			myLogger.error(e);
			myLogger.error("Problem handling socket request");
		}
	}

	/**
	 *  
	 */
	private void handlePutRequest() throws IOException {
		myLogger.debug("handling put request");
		int vectorLength = mySocketDataInputStream.readInt();
    	myLogger.debug("vector length was: "+vectorLength);
    	myCurrentInputData = new double[vectorLength];
        for (int i = 0; i < myCurrentInputData.length; i++){
        	myCurrentInputData[i] = mySocketDataInputStream.readDouble();
        	myLogger.debug("getting double: "+myCurrentInputData[i]);
        }
	}

	/**
	 * @return
	 */
	private String determineOperationRequested() throws IOException{
		return mySocketTextReader.readLine();
	}

	/**
	 *  
	 */
	private void handleGetRequest() throws IOException {
		double[] outputVector = {6d, 7.54d, Math.PI * 2d, 5.9d, 8d, 9.23322d};
		myLogger.debug("handling get request");
		myLogger.debug("sending vector length: "+outputVector.length);
		mySocketDataOutputStream.writeInt(outputVector.length);
		mySocketDataOutputStream.flush();
    	for (int i = 0; i < outputVector.length; i++){
    		myLogger.debug("sending double: "+outputVector[i]);
    		mySocketDataOutputStream.writeDouble(outputVector[i]);
    		mySocketDataOutputStream.flush();
    	}
	}

	/**
	 * @return
	 */
	private boolean isBiosimClient() throws IOException {
		//Say Hello
		mySocketTextWriter.write(MatlabAceEngine.SERVER_HELLO + "\n");
		mySocketTextWriter.flush();
		return mySocketTextReader.readLine().equals(MatlabAceEngine.CLIENT_HELLO);
	}

	/**
	 * @param clientSocket
	 */
	private void initializeStreams(Socket clientSocket) throws IOException {
		mySocketTextReader = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		mySocketDataInputStream = new DataInputStream(clientSocket
				.getInputStream());
		mySocketTextWriter = new BufferedWriter(new OutputStreamWriter(
				clientSocket.getOutputStream()));
		mySocketDataOutputStream = new DataOutputStream(clientSocket
				.getOutputStream());

	}

}
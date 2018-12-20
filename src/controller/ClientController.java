package controller;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javafx.scene.control.IndexRange;
import model.TextFile;
import rmi.Service;

public class ClientController {
	
	/*
	 * Variables for application configuration
	 */
	private static ClientController instance;
	private static Service service;
	private static String user;	
	private static String address;
	private static int port;
	
	/*
	 * Variables for opened text file management
	 */
	public static TextFile openedTextFile;
	
	/**
	 * Constructs the class
	 */
	public ClientController() { }
	
	/**
	 * Gets a synchronized instance of current class
	 * @return Instance of current class
	 */
	public static synchronized ClientController getInstance() {
		if(instance == null) {
			instance = new ClientController();
		}
		return instance;
	}
	
	/**
	 * Gets user name
	 * @return User name
	 */
	public static String getUser() {
		return user;
	}

	/**
	 * Sets user name
	 * @param user User name
	 */
	public static void setUser(String user) {
		ClientController.user = user;
	}
	
	/**
	 * Gets server address
	 * @return Server address
	 */
	public static String getAddress() {
		return address;
	}

	/**
	 * Gets server port
	 * @return Server port
	 */
	public static int getPort() {
		return port;
	}

	/**
	 * Turns service on
	 * @param serverIP Server ip
	 * @param serverPort Server port
	 * @param userID User name
	 * @throws IOException
	 * @throws NotBoundException
	 */
	public static void setUpService(String serverIP, int serverPort, String userID) throws IOException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(serverIP, serverPort);		
		service = (Service) registry.lookup("TextEditor");
		address = serverIP;
		port = serverPort;
		user = userID;
	}	

	/**
	 * Creates a new file
	 * @param fileName New file name
	 * @throws RemoteException
	 */
	public static void newFile(String fileName) throws RemoteException {
		openedTextFile = service.newFile(fileName, user);
	}
	
	/**
	 * Deletes a file
	 * @param id Id of file that will be removed
	 * @throws RemoteException
	 */
	public static void removeFile(int id) throws RemoteException {
		service.removeFile(id);
	}
	
	/**
	 * Change opened file
	 * @param id New file opened
	 * @throws RemoteException
	 */
	public static void changeOpened(int id) throws RemoteException {
		openedTextFile = service.getFile(id);
	}
	
	/**
	 * Insert text on file
	 * @param range Range to insert
	 * @param text Text to be inserted
	 * @throws RemoteException
	 */
	public static void addText(IndexRange range, String text) throws RemoteException {
		service.addText(range.getStart(), range.getEnd(), text, openedTextFile.getId());
	}
	
	/**
	 * Deletes a range with backspace key
	 * @param range Range to delete
	 * @throws RemoteException
	 */
	public static void backspaceDelete(IndexRange range) throws RemoteException {
		if(range.getStart() > 0 && range.getStart() - range.getEnd() == 0) {
			service.deleteText(range.getStart() - 1, range.getEnd(), openedTextFile.getId());
		} else {
			service.deleteText(range.getStart(), range.getEnd(), openedTextFile.getId());
		}
	}
	
	/**
	 * Deletes a range with del key
	 * @param range Range to delete
	 * @throws RemoteException
	 */
	public static void delDelete(IndexRange range) throws RemoteException {
		if(range.getEnd() + 1 <= openedTextFile.getText().length() && range.getStart() - range.getEnd() == 0) {
			service.deleteText(range.getStart(), range.getEnd() + 1, openedTextFile.getId());
		} else {
			service.deleteText(range.getStart(), range.getEnd(), openedTextFile.getId());
		}
	}
	
	/**
	 * Add collaborators to file
	 * @param users Users array
	 * @throws RemoteException
	 */
	public static void addUser(ArrayList<String> users) throws RemoteException {
		service.addUsers(users, openedTextFile.getId());
	}
	
	/**
	 * Gets user files
	 * @return User files
	 * @throws RemoteException
	 */
	public static ArrayList<TextFile> getUserFiles() throws RemoteException {
		return service.getUserFiles(user);
	}	
	
}

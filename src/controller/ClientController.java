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
	
	public ClientController() { }
	
	public static synchronized ClientController getInstance() {
		if(instance == null) {
			instance = new ClientController();
		}
		return instance;
	}
	
	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		ClientController.user = user;
	}
	
	public static String getAddress() {
		return address;
	}

	public static int getPort() {
		return port;
	}

	public static void setUpService(String serverIP, int serverPort, String userID) throws IOException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(serverIP, serverPort);		
		service = (Service) registry.lookup("TextEditor");
		address = serverIP;
		port = serverPort;
		user = userID;
	}	

	public static void newFile(String fileName) throws RemoteException {
		openedTextFile = service.newFile(fileName, user);
	}
	
	public static void removeFile(int id) throws RemoteException {
		service.removeFile(id);
	}
	
	public static void changeOpened(int id) throws RemoteException {
		openedTextFile = service.getFile(id);
	}
	
	public static void addText(IndexRange range, String text) throws RemoteException {
		service.addText(range.getStart(), range.getEnd(), text, openedTextFile.getId());
	}
	
	public static void backspaceDelete(IndexRange range) throws RemoteException {
		if(range.getStart() > 0 && range.getStart() - range.getEnd() == 0) {
			service.deleteText(range.getStart() - 1, range.getEnd(), openedTextFile.getId());
		} else {
			service.deleteText(range.getStart(), range.getEnd(), openedTextFile.getId());
		}
	}
	
	public static void delDelete(IndexRange range) throws RemoteException {
		if(range.getEnd() + 1 <= openedTextFile.getText().length() && range.getStart() - range.getEnd() == 0) {
			service.deleteText(range.getStart(), range.getEnd() + 1, openedTextFile.getId());
		} else {
			service.deleteText(range.getStart(), range.getEnd(), openedTextFile.getId());
		}
	}
	
	public static void addUser(ArrayList<String> users) throws RemoteException {
		service.addUsers(users, openedTextFile.getId());
	}
	
	public static ArrayList<TextFile> getUserFiles() throws RemoteException {
		return service.getUserFiles(user);
	}	
	
}

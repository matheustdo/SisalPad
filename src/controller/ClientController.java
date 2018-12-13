package controller;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.Service;

public class ClientController {
	
	private Service service;
	private String user;
	
	public ClientController(String serverIp, int serverPort) throws IOException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(serverPort);
		this.service = (Service) registry.lookup("TextEditor");
	}
	
}

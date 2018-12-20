package view;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.Servant;


public class Server {
	public static final int port = 1050;

	public static void main(String[] args) throws Exception {
		Registry registry = LocateRegistry.createRegistry(port);
		registry.bind("TextEditor", new Servant());
		
		System.out.println("> TextEditor service is running at " + port + " port");
	}

}

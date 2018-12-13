package view;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.Servant;


public class Server {

	public static void main(String[] args) throws Exception {
		Registry registry = LocateRegistry.createRegistry(1050);
		registry.bind("TextEditor", new Servant());
		
		System.out.println("> TextEditor service is running");
	}

}

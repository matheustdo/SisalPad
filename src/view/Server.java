package view;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmi.Servant;


public class Server {
	public static int port;

	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("> Insert port: ");
			port = Integer.parseInt(scanner.nextLine());
			scanner.close();
		} else {
			port = Integer.parseInt(args[0]);
		}		
		
		Registry registry = LocateRegistry.createRegistry(port);
		registry.bind("TextEditor", new Servant());		
		
		System.out.println("> TextEditor service is running at " + port + " port");
	}

}

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	public static void main(String[] args) throws Exception {
		Registry registry = LocateRegistry.createRegistry(1050);
		registry.rebind("TextEditor", new Servant());
	}

}

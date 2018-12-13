import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Servant extends UnicastRemoteObject implements Service {
	
	private static final long serialVersionUID = -3011273052818005271L;

	public Servant() throws RemoteException {
		super();
	}

	@Override
	public String echo(String input) throws RemoteException {
		return input;
	}

}

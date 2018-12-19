package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.TextFile;

public class Servant extends UnicastRemoteObject implements Service {
	
	private static final long serialVersionUID = -3011273052818005271L;

	private Map<Integer, TextFile> textFiles;
	
	public Servant() throws RemoteException {
		super();
		this.textFiles = new HashMap<Integer, TextFile>();
	}

	@Override
	public TextFile newFile(String name, String owner) throws RemoteException {
		TextFile textFile = new TextFile(name, owner);
		this.textFiles.put(textFile.getId(), textFile);
		return textFile;
	}

	@Override
	public ArrayList<TextFile> getUserFiles(String user) throws RemoteException {
		ArrayList<TextFile> userFiles = new ArrayList<TextFile>();
		for(Integer key: this.textFiles.keySet()) {
			TextFile textFile = this.textFiles.get(key);
			if(textFile.getOwner().equals(user) || textFile.getUsers().contains(user)) {
				userFiles.add(textFile);
			}
		}
		return userFiles;
	}

	@Override
	public TextFile removeFile(int id) throws RemoteException {
		TextFile textFile = textFiles.get(id);
		textFiles.remove(id);
		return textFile;
	}

	@Override
	public TextFile getFile(int id) throws RemoteException {
		return textFiles.get(id);
	}

	@Override
	public void addText(int start, int end,  String text, int id) throws RemoteException {
		TextFile textFile = textFiles.get(id);
		textFile.insertTextAtRange(start, end, text);
	}

	@Override
	public void deleteText(int start, int end, int id) throws RemoteException {
		TextFile textFile = textFiles.get(id);
		textFile.removeTextAtRange(start, end);
	}

	@Override
	public void addUsers(ArrayList<String> users, int id) throws RemoteException {
		TextFile textFile = textFiles.get(id);
		textFile.setUsers(users);
	}

}

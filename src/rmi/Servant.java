package rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import model.TextFile;

public class Servant extends UnicastRemoteObject implements Service {
	
	private static final long serialVersionUID = -3011273052818005271L;

	private Map<Integer, TextFile> textFiles;
	
	public Servant() throws ClassNotFoundException, IOException {
		super();
		this.textFiles = new HashMap<Integer, TextFile>();
		loadData();
		persistence();
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

	@SuppressWarnings("null")
	@Override
	public void addText(int start, int end, String text, int id) throws RemoteException {
		TextFile textFile = textFiles.get(id);
		
		if(textFile != null) {
			textFile.insertTextAtRange(start, end, text);
		}
	}

	@Override
	public void deleteText(int start, int end, int id) throws RemoteException {
		TextFile textFile = textFiles.get(id);
		
		if(textFile != null && start <= textFile.getText().length() && end <= textFile.getText().length()) {
			textFile.removeTextAtRange(start, end);
		}
	}

	@Override
	public void addUsers(ArrayList<String> users, int id) throws RemoteException {
		TextFile textFile = textFiles.get(id);
		
		if(textFile != null) {
			textFile.setUsers(users);
		}
	}
	
	/**
	 * Loads server data
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	private void loadData() throws IOException, ClassNotFoundException {
		File file = new File("system.data");
		
		if(file.exists()) {
			FileInputStream fis = new FileInputStream(file);	 
			ObjectInputStream ois = new ObjectInputStream(fis);	 
			Object obj = ois.readObject();
			
			if(obj instanceof Map<?, ?>) {
				this.textFiles = (Map<Integer, TextFile>) obj;
			}
			
			for(Integer key: this.textFiles.keySet()) {
				TextFile.counter = key+1;
			}
			
			ois.close(); 
			fis.close();
		}
	}
	
	/**
	 * Turns on system persistence
	 */
	private void persistence() {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				File file = new File("system.data");
				
				try {					
					if(!file.exists()) {					
						file.createNewFile();					
					}		
					
					FileOutputStream fos = new FileOutputStream(file);			 
					ObjectOutputStream oos = new ObjectOutputStream(fos); 
					oos.writeObject(textFiles); 
					oos.flush(); 
					oos.close(); 
					fos.flush(); 
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}, 2000, 2000);
	}

}

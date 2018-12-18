package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javafx.scene.control.IndexRange;
import model.TextFile;

public interface Service extends Remote {

	/**
	 * Creates a new file
	 * @param name File name
	 * @param owner File owner name
	 * @return Created file
	 * @throws RemoteException
	 */
	public TextFile newFile(String name, String owner) throws RemoteException;
	
	/**
	 * Returns to user their files
	 * @param user User id
	 * @return User files
	 * @throws RemoteException
	 */
	public ArrayList<TextFile> getUserFiles(String user) throws RemoteException;
	
	/**
	 * Removes file that have informed id
	 * @param id File id
	 * @return Removed file
	 */
	public TextFile removeFile(int id) throws RemoteException;
	
	/**
	 * Gets a file by id
	 * @param id File id
	 * @return TextFile
	 * @throws RemoteException
	 */
	public TextFile getFile(int id) throws RemoteException;
	
	/**
	 * Inserts a text into a text file at index range
	 * @param range Index range
	 * @param id Text id
	 * @return Text Text to insert
	 * @throws RemoteException
	 */
	public String addChar(IndexRange range, String text, int id) throws RemoteException;
	
}

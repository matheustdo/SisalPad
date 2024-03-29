package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Matheus Teles
 */
public class TextFile implements Serializable {
	
	private static final long serialVersionUID = 5275929308261089338L;
	public static int counter;
	
	private int id;	
	private String name;
	private String owner;
	private String text;
	private ArrayList<String> users;
	
	public TextFile(String name, String owner) {
		this.id = counter++;
		this.name = name;
		this.owner = owner;
		this.text = "";
		this.users = new ArrayList<String>();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<String> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}
	
	/**
	 * Insert text at range
	 * @param start Initial range
	 * @param end Final range
	 * @param text Text to be inserted
	 */
	public void insertTextAtRange(int start, int end, String text) {
		if(this.text == null) {
			this.text = text;
		} else if (start <= this.text.length() && end <= this.text.length()) {
			String startString = this.text.substring(0, start);
			String endString = this.text.substring(end, this.text.length());
			this.text = startString + text + endString;
		}
	}

	/**
	 * Removes text at range
	 * @param start Initial range
	 * @param end End range
	 */
	public void removeTextAtRange(int start, int end) {
		if(this.text != null && start <= this.text.length() && end <= this.text.length()) {
			String startString = this.text.substring(0, start);
			String endString = this.text.substring(end, this.text.length());
			this.text = startString + endString;
		}
	}

}

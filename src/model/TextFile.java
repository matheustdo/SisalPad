package model;

import java.io.Serializable;
import java.util.ArrayList;

public class TextFile implements Serializable {
	
	private static final long serialVersionUID = 5275929308261089338L;
	private static int counter;
	
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
	
	public void insertTextAtRange(int start, int end, String text) {
		if(this.text == null) {
			this.text = text;
		} else {
			String startString = this.text.substring(0, start);
			String endString = this.text.substring(end, this.text.length());
			this.text = startString + text + endString;
		}
	}

	public void removeTextAtRange(int start, int end) {
		if(this.text != null) {
			String startString = this.text.substring(0, start);
			String endString = this.text.substring(end, this.text.length());
			this.text = startString + endString;
		}
	}

}

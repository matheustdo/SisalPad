package controller.fxml;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import controller.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CollaboratorsDialogController extends Observable implements Initializable {

	@FXML
    private TextArea usersTextArea;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		usersTextArea.setStyle("-fx-text-fill: #ab0000;");
		
		String formatedUsers = new String();
		for(String user: ClientController.openedTextFile.getUsers()) {
			formatedUsers += user + System.getProperty("line.separator");
		}
		
		if(formatedUsers.length() != 0) {
			usersTextArea.setText(formatedUsers);
		}		
	}	

    @FXML
    void updateOnAction(ActionEvent event) throws RemoteException {
    	StringTokenizer usersST = new StringTokenizer(usersTextArea.getText(), System.getProperty("line.separator"));
		
    	ArrayList<String> userList = new ArrayList<String>();
    	while(usersST.hasMoreElements()) {
    		String user = usersST.nextToken();
    		if(user.length() != 0 && !userList.contains(user)) {
    			userList.add(user);
    		}    		
    	}
    	
    	ClientController.openedTextFile.setUsers(userList);
    	ClientController.addUser(userList);
    	
    	Stage dialog = (Stage) usersTextArea.getScene().getWindow();
		dialog.close();
    }
}

package controller.fxml;

import java.rmi.RemoteException;
import java.util.Observable;

import controller.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewTextDialogController extends Observable {
	
	@FXML
    private TextField fileNameTextField;

    @FXML
    private Button createButton;
    
    private Stage dialog;

    @FXML
    void createOnAction(ActionEvent event) throws RemoteException {
    	ClientController.newFile(fileNameTextField.getText());
    	setChanged();
		notifyObservers();
    	dialog.close();
    }
    
    public void setDialog(Stage dialog) {
    	this.dialog = dialog;
    }
    
}

package controller.fxml;

import java.rmi.RemoteException;
import java.util.Observable;

import controller.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Matheus Teles
 */
public class NewTextDialogController extends Observable {
	
	@FXML
    private TextField fileNameTextField;

    @FXML
    private Button createButton;

    @FXML
    void createOnAction(ActionEvent event) throws RemoteException {
    	ClientController.newFile(fileNameTextField.getText());
    	setChanged();
		notifyObservers();
		Stage dialog = (Stage) createButton.getScene().getWindow();
		dialog.close();
    }
    
}

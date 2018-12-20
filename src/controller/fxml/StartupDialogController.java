package controller.fxml;

import java.io.IOException;
import java.rmi.NotBoundException;

import controller.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Matheus Teles
 */
public class StartupDialogController {
	
	@FXML
    private TextField serverIpTextField;

    @FXML
    private TextField serverPortTextField;

    @FXML
    private Button beginButton;
    
    @FXML
    private TextField userIdTextField;

    @FXML
    void beginButtonOnAction(ActionEvent event) throws NotBoundException {    	
	    try {
	    	String user = userIdTextField.getText();
		    String ip = serverIpTextField.getText();
		    int port = Integer.parseInt(serverPortTextField.getText());
			ClientController.setUpService(ip, port, user);
			Stage dialog = (Stage) beginButton.getScene().getWindow();
			dialog.close();
		} catch (NumberFormatException | IOException e) {
			serverIpTextField.setText("BINDING ERROR");
			serverPortTextField.setText("BINDING ERROR");
		}
    }
	
}

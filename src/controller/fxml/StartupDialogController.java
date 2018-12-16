package controller.fxml;

import java.io.IOException;
import java.rmi.NotBoundException;

import controller.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartupDialogController {
	
	@FXML
    private TextField serverIpTextField;

    @FXML
    private TextField serverPortTextField;

    @FXML
    private Button beginButton;
    
    @FXML
    private TextField userIdTextField;
    
    private Stage dialog;

    @FXML
    void beginButtonOnAction(ActionEvent event) throws IOException, NotBoundException {
    	String user = userIdTextField.getText();
	    String ip = serverIpTextField.getText();
	    int port = Integer.parseInt(serverPortTextField.getText());
	    ClientController.setUpService(ip, port, user);
	    this.dialog.close();
    }
    
    public void setDialog(Stage dialog) {
    	this.dialog = dialog;
    }
	
}

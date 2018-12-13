package controller.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetServerDialogController implements Initializable {
	
	@FXML
    private TextField serverIpTextField;

    @FXML
    private TextField serverPortTextField;

    @FXML
    private Button beginButton;
    
    private String ip;
    private int port;
    private Stage dialog;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) { }	

    @FXML
    void beginButtonOnAction(ActionEvent event) {
	    this.ip = serverIpTextField.getText();
	    this.port = Integer.parseInt(serverPortTextField.getText());
	    this.dialog.close();
    }
    
    public void setDialog(Stage dialog) {
    	this.dialog = dialog;
    }

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
}

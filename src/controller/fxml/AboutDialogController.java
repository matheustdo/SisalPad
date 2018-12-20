package controller.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import controller.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * @author Matheus Teles
 */
public class AboutDialogController implements Initializable {

	@FXML
    private Label userLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label portLabel;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userLabel.setText(ClientController.getUser());
		addressLabel.setText(ClientController.getAddress());
		portLabel.setText(ClientController.getPort() + "");
	}

}

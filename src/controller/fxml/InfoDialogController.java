package controller.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import controller.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class InfoDialogController implements Initializable {
	
	@FXML
    private TextArea usersTextArea;

    @FXML
    private Label ownerLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idLabel.setText(ClientController.openedTextFile.getId() + "");
		nameLabel.setText(ClientController.openedTextFile.getName());
		ownerLabel.setText(ClientController.openedTextFile.getOwner());
	}
    
}

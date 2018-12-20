package controller.fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlertDialogController {
	@FXML
    private Label warningTextLabel;

    @FXML
    void continueOnAction(ActionEvent event) {
    	Stage dialog = (Stage) warningTextLabel.getScene().getWindow();
    	dialog.close();
    }
}

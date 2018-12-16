package controller.fxml;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import controller.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.Client;

public class HomePageController implements Observer, Initializable {
	
	@FXML
    private MenuItem infoMenuItem;

    @FXML
    private MenuItem colaboratorsMenuItem;

    @FXML
    private MenuItem deleteMenuItem;
    
	@FXML
    private TabPane tabPane;
    
    @FXML
    private TextArea textArea;
    
    @FXML
    private CheckMenuItem checkWrapMenuItem;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		tabPane.setTabMinWidth(70);
		tabPane.setTabMaxWidth(70);		
		infoMenuItem.setDisable(true);
		removeOwner();
	}
    
    @FXML
    void wrapOnAction(ActionEvent event) {
    	textArea.setWrapText(checkWrapMenuItem.isSelected());
    }
    
    @FXML
    void newMenuOnAction(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("fxml/NewTextDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();		
				
		Scene scene = new Scene(page, 200, 100);
		Stage dialog = new Stage();
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.setTitle("New text file");
		dialog.setScene(scene);
		dialog.setResizable(false);
		
		NewTextDialogController newTextDialog = loader.getController();
		newTextDialog.addObserver(this);
		newTextDialog.setDialog(dialog);
		
		dialog.showAndWait();
		setOwner();
    }
    
    @FXML
    void infoMenuOnAction(ActionEvent event) {

    }

    @FXML
    void colaboratorsMenuOnAction(ActionEvent event) {

    }

    @FXML
    void deleteMenuOnAction(ActionEvent event) throws RemoteException {
    	int id = ClientController.openedTextFile.getId();
    	
    	for(Tab tab: tabPane.getTabs()) {
    		if(tab.getId().equals(id + "")) {
    			tabPane.getTabs().remove(tab);
    			break;
    		}    		
    	}
    	
    	ClientController.changeOpened(Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId()));
    	ClientController.removeFile(id);
    }
    
    
    @FXML
    void closeMenuOnAction(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void tabPaneOnAction(MouseEvent event) throws NumberFormatException, RemoteException {
    	if(ClientController.openedTextFile.getOwner().equals(ClientController.user)) {
    		setOwner();
    	} else {
    		removeOwner();
    	}
    	
    	textArea.setText(ClientController.openedTextFile.getText());
    	ClientController.changeOpened(Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId()));
    }    

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof NewTextDialogController) {
			textArea.setText(ClientController.openedTextFile.getText());
			Tab tab = new Tab("(" + ClientController.openedTextFile.getId() + ") " + ClientController.openedTextFile.getName());
			tab.setId(ClientController.openedTextFile.getId() + "");
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);
		}
	}
	
	private void setOwner() {
		deleteMenuItem.setDisable(false);
		colaboratorsMenuItem.setDisable(false);
	}
	
	private void removeOwner() {
		deleteMenuItem.setDisable(true);
		colaboratorsMenuItem.setDisable(true);
	}
    
}

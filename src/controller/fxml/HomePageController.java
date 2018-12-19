package controller.fxml;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import controller.ClientController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.Client;

public class HomePageController implements Observer, Initializable {
	
	@FXML
    private AnchorPane anchorPane;
	
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
		
		loadFile();
		
		textArea = new TextArea() {
			@Override
            public void paste() {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                if (clipboard.hasString()) {
                    replaceSelection(clipboard.getString());
                }
            }
		};
		
		textArea.setVisible(false);
		anchorPane.getChildren().add(textArea);
		AnchorPane.setLeftAnchor(textArea, 0.0);
		AnchorPane.setRightAnchor(textArea, 0.0);
		AnchorPane.setBottomAnchor(textArea, 0.0);
		AnchorPane.setTopAnchor(textArea, 56.0);
		
		textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	IndexRange range = textArea.getSelection();
            	if (event.getCode() == KeyCode.BACK_SPACE) {
            		try {
						ClientController.backspaceDelete(range);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
            	} else if (event.getCode() == KeyCode.DELETE) {
            		try {
						ClientController.delDelete(range);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
            	} else if (event.getCode() == KeyCode.ENTER) {
            		try {                    	
						ClientController.addText(range, System.getProperty("line.separator"));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
            	} else {            		
                    try {                    	
						ClientController.addText(range, event.getText());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
            	}             	
            }
        });
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
    	
    	if(tabPane.getSelectionModel().getSelectedItem() != null ) {
    		ClientController.changeOpened(Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId()));
    	} else {
    		ClientController.openedTextFile = null;
    	}
    	
    	ClientController.removeFile(id);
    	loadFile();
    }
    
    @FXML
    void closeMenuOnAction(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void tabPaneOnAction(MouseEvent event) throws NumberFormatException, RemoteException {	
    	if(!tabPane.getSelectionModel().isEmpty()) {
    		textArea.setText(ClientController.openedTextFile.getText());
        	ClientController.changeOpened(Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId()));
        	loadFile();
    	}    	
    }    

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof NewTextDialogController) {
			textArea.setText(ClientController.openedTextFile.getText());
			Tab tab = new Tab("(" + ClientController.openedTextFile.getId() + ") " + ClientController.openedTextFile.getName());
			tab.setId(ClientController.openedTextFile.getId() + "");
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);
			loadFile();
		}
	}
	
	@FXML
    void textOnKeyTyped(KeyEvent event) {
		IndexRange i = textArea.getSelection();
		System.out.println(event.getCharacter());
		ClientController.openedTextFile.insertTextAtRange(i.getStart(), i.getStart(), "c");
    }

	
	private void loadFile() {		
		if(ClientController.openedTextFile != null) {
			infoMenuItem.setDisable(false);
			textArea.setVisible(true);
			textArea.setText(ClientController.openedTextFile.getText());
			if(ClientController.openedTextFile.getOwner().equals(ClientController.user)) {
				setOwner();
			} else {
				removeOwner();
			}
		} else {
			textArea.setVisible(false);
			infoMenuItem.setDisable(true);
			removeOwner();
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

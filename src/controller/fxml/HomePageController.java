package controller.fxml;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import controller.ClientController;
import javafx.application.Platform;
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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.TextFile;
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
		tabPane.setStyle("-fx-open-tab-animation: NONE; -fx-close-tab-animation: NONE;");		
		
		openFile();
		
		textArea = new TextArea() {
			@Override
            public void paste() {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                if (clipboard.hasString()) {
                	try {	
                		IndexRange range = textArea.getSelection();
						ClientController.addText(range, clipboard.getString());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
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

		// Controls input
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
            	} else if (!event.getCode().isArrowKey() && 
	            		   !event.getCode().isFunctionKey() &&
	            		   !event.getCode().isMediaKey() &&
	            		   !event.getCode().isNavigationKey() &&
	            		   !event.isControlDown()) {            		
                    try {
						ClientController.addText(range, event.getText());						
					} catch (RemoteException e) {
						e.printStackTrace();
					}
            	}             	
            }
        });
		
		// Solves accentuation input
		textArea.setOnInputMethodTextChanged(new EventHandler<InputMethodEvent>() {
			@Override
			public void handle(InputMethodEvent event) {
				try {
					ClientController.addText(textArea.getSelection(), event.getCommitted());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				textArea.insertText(textArea.getCaretPosition(), event.getCommitted());
			}
		});
		
		try {
			loadFiles();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		openedFileUpdater();
		userFilesUpdater();
		
		if(!System.getProperty("os.name").contains("Linux")) {
			try {
				showSystemWarning();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
				
		Scene scene = new Scene(page, 200, 120);
		Stage dialog = new Stage();
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(anchorPane.getScene().getWindow());
		dialog.setTitle("New Text File");
		dialog.setScene(scene);
		dialog.setResizable(false);
		
		NewTextDialogController newTextDialog = loader.getController();
		newTextDialog.addObserver(this);
		
		dialog.showAndWait();		
    }
    
    @FXML
    void infoMenuOnAction(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("fxml/InfoDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();		
				
		Scene scene = new Scene(page, 297, 410);
		Stage dialog = new Stage();
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(anchorPane.getScene().getWindow());
		dialog.setTitle("File Info");
		dialog.setScene(scene);
		dialog.setResizable(false);
		
		dialog.showAndWait();	
    }

    @FXML
    void colaboratorsMenuOnAction(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("fxml/ColaboratorsDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();		
				
		Scene scene = new Scene(page, 296, 323);
		Stage dialog = new Stage();
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(anchorPane.getScene().getWindow());
		dialog.setTitle("Colaborators");
		dialog.setScene(scene);		
		dialog.setResizable(false);
		
		dialog.showAndWait();	
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
    	openFile();
    }
    
    @FXML
    void closeMenuOnAction(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void aboutMenuOnAction(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("fxml/AboutDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();		
				
		Scene scene = new Scene(page, 422, 245);
		Stage dialog = new Stage();
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(anchorPane.getScene().getWindow());
		dialog.setTitle("About SisalPad");
		dialog.setScene(scene);
		dialog.setResizable(false);
		
		dialog.showAndWait();	
    }
    
    @FXML
    void tabPaneOnAction(MouseEvent event) throws NumberFormatException, RemoteException {	
    	if(!tabPane.getSelectionModel().isEmpty()) {    		
    		textArea.setText(ClientController.openedTextFile.getText());
        	ClientController.changeOpened(Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId()));
        	openFile();
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
			openFile();
		}
	}
	
	private void loadFiles() throws RemoteException {
		for(TextFile textFile: ClientController.getUserFiles()) {
			Tab tab = new Tab("(" + textFile.getId() + ") " + textFile.getName());
			tab.setId(textFile.getId() + "");
			tabPane.getTabs().add(tab);
		}
		
		if(!tabPane.getTabs().isEmpty()) {
			tabPane.getSelectionModel().select(0);
			ClientController.changeOpened(Integer.parseInt(tabPane.getTabs().get(0).getId()));
			openFile();
		}
	}
	
	private void openFile() {		
		if(ClientController.openedTextFile != null) {
			infoMenuItem.setDisable(false);
			textArea.setVisible(true);
			if(ClientController.openedTextFile.getOwner().equals(ClientController.getUser())) {
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
	
	private void openedFileUpdater() {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {				
				try {
					if(ClientController.openedTextFile != null) {
						IndexRange range = textArea.getSelection();
						Double scrollTopValue = textArea.scrollTopProperty().get();
						Double scrollLeftValue = textArea.scrollLeftProperty().get();
						ClientController.changeOpened(ClientController.openedTextFile.getId());
						if(ClientController.openedTextFile != null) {
							textArea.setText(ClientController.openedTextFile.getText());		
							textArea.selectRange(range.getStart(), range.getEnd());
							textArea.setScrollTop(scrollTopValue);
							textArea.setScrollLeft(scrollLeftValue);
						}
						openFile();	
					}										
				} catch (RemoteException e) {
					e.printStackTrace();
				}	
				
				openFile();
			}
		}, 200, 200);		
	}
	
	private void userFilesUpdater() {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					if(ClientController.openedTextFile != null) {
						try {
							String selected = tabPane.getSelectionModel().getSelectedItem().getId();
							
							TabPane tabPaneAux = tabPane;
							tabPaneAux.getTabs().clear();
							
							for(TextFile textFile: ClientController.getUserFiles()) {
								Tab tab = new Tab("(" + textFile.getId() + ") " + textFile.getName());
								tab.setId(textFile.getId() + "");
								tabPaneAux.getTabs().add(tab);
							}

							tabPane = tabPaneAux;
							
							for(Tab tab: tabPane.getTabs()) {
								if(tab.getId().equals(selected)) {
									tabPane.getSelectionModel().select(tab);
									break;
								}
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						}					
					} else {						
						try {
							textArea.setText("");
							TabPane tabPaneAux = tabPane;
							tabPaneAux.getTabs().clear();
							
							for(TextFile textFile: ClientController.getUserFiles()) {
								Tab tab = new Tab("(" + textFile.getId() + ") " + textFile.getName());
								tab.setId(textFile.getId() + "");
								tabPaneAux.getTabs().add(tab);
							}
							
							tabPane = tabPaneAux;
							
							if(!tabPane.getTabs().isEmpty() && ClientController.openedTextFile == null) {
								ClientController.changeOpened(Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId()));
							}
							
						} catch (RemoteException e) {
							e.printStackTrace();
						}					
					}
					
					openFile();
				});				
			}
				
		}, 200, 200);
	}
	
	/**
	 * Shows system warning
	 * @throws IOException
	 */
	private void showSystemWarning() throws IOException {
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					FXMLLoader loader = new FXMLLoader();
			        loader.setLocation(Client.class.getResource("fxml/AlertDialog.fxml"));
					AnchorPane page = new AnchorPane();
					try {
						page = (AnchorPane) loader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}		
							
					Scene scene = new Scene(page, 288, 159);
					Stage dialog = new Stage();
	
					dialog.initModality(Modality.WINDOW_MODAL);
					dialog.initOwner(anchorPane.getScene().getWindow());		
					dialog.setTitle("System Warning");
					dialog.setScene(scene);
					dialog.setResizable(false);
					
					dialog.show();
				});
			}			
		}, 1000);		
	}
    
}

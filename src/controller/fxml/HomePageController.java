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

/**
 * @author Matheus Teles
 */
public class HomePageController implements Observer, Initializable {
	
	@FXML
    private AnchorPane anchorPane;
	
	@FXML
    private MenuItem infoMenuItem;

    @FXML
    private MenuItem collaboratorsMenuItem;

    @FXML
    private MenuItem deleteMenuItem;
    
	@FXML
    private TabPane tabPane;
    
    @FXML
    private TextArea textArea;
    
    @FXML
    private CheckMenuItem checkWrapMenuItem;
    
    private boolean alt = false;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		tabPane.setTabMinWidth(70);
		tabPane.setTabMaxWidth(70);		
		tabPane.setStyle("-fx-open-tab-animation: NONE; -fx-close-tab-animation: NONE;");		
		
		openFile();
		
		/*
		 * Enables paste text
		 */
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

		/*
		 * Controls input
		 */
		textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	IndexRange range = textArea.getSelection();
            	alt = true;
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
		
		/*
		 * Solves accentuation input
		 */
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
		
		/*
		 * Show alert to non Linux users
		 */
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
    
    /**
     * Opens new file dialog
     * @param event
     * @throws IOException
     */
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
    
    /**
     * Opens info dialog
     * @param event
     * @throws IOException
     */
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

    /**
     * Opens collaborators dialog
     * @param event
     * @throws IOException
     */
    @FXML
    void collaboratorsMenuOnAction(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("fxml/CollaboratorsDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();		
				
		Scene scene = new Scene(page, 296, 323);
		Stage dialog = new Stage();
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(anchorPane.getScene().getWindow());
		dialog.setTitle("Collaborators");
		dialog.setScene(scene);		
		dialog.setResizable(false);
		
		dialog.showAndWait();	
    }

    /**
     * Deletes opened file
     * @param event
     * @throws RemoteException
     */
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
    
    /**
     * Closes system
     * @param event
     */
    @FXML
    void closeMenuOnAction(ActionEvent event) {
    	System.exit(0);
    }
    
    /**
     * Opens about dialog
     * @param event
     * @throws IOException
     */
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
    
    /**
     * Changes file when tabPane action pane
     * @param event
     * @throws NumberFormatException
     * @throws RemoteException
     */
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
			/*
			 * Add tab when a new file was created
			 */
			textArea.setText(ClientController.openedTextFile.getText());
			Tab tab = new Tab("(" + ClientController.openedTextFile.getId() + ") " + ClientController.openedTextFile.getName());
			tab.setId(ClientController.openedTextFile.getId() + "");
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);
			openFile();
		}
	}
	
	/**
	 * Loads user files on home
	 * @throws RemoteException
	 */
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
	
	/**
	 * Opens a file and change menu buttons states
	 */
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
	
	/**
	 * Sets menu buttons states to owner access
	 */
	private void setOwner() {
		deleteMenuItem.setDisable(false);
		collaboratorsMenuItem.setDisable(false);
	}
	
	/**
	 * Sets menu buttons states to normal user access
	 */
	private void removeOwner() {
		deleteMenuItem.setDisable(true);
		collaboratorsMenuItem.setDisable(true);
	}
	
	/**
	 * Update opened file per time
	 */
	private void openedFileUpdater() {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {				
				try {
					if(ClientController.openedTextFile != null) {	
						String oldText = ClientController.openedTextFile.getText();							
						ClientController.changeOpened(ClientController.openedTextFile.getId());
						String newText = ClientController.openedTextFile.getText();
						
						IndexRange range = textArea.getSelection();
						int start = range.getStart();
						int end = range.getEnd();
						Double scrollTopValue = textArea.scrollTopProperty().get();
						Double scrollLeftValue = textArea.scrollLeftProperty().get();						
						
						/*
						 * Moves the cursor or selection for non user text change
						 */
						if(!alt) {
							if(start <= oldText.length() && start <= newText.length() &&
							   !oldText.substring(0, start).equals(newText.substring(0, start))) {
								if(oldText.length() < newText.length()) {
									for(int i = 0; i < newText.length() - oldText.length(); i++) {
										start++;
										end++;
									}								
								} else if (oldText.length() > newText.length()) {
									for(int i = 0; i < oldText.length() - newText.length(); i++) {
										start--;
										end--;
									}	
								}							
							}							
						}
						alt = false;						
						
						/*
						 * Updates textArea selection, caret position and scroll
						 */
						if(ClientController.openedTextFile != null) {
							textArea.setText(ClientController.openedTextFile.getText());
							
							if(start <= newText.length() && end <= newText.length() &&
							   start >= 0 && end >= 0) {
								textArea.selectRange(start, end);
							}
							
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
	
	/**
	 * Update user files per time
	 */
	private void userFilesUpdater() {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					if(ClientController.openedTextFile != null) {
						/*
						 * Update if the user has loaded files
						 */
						try {
							String selected = tabPane.getSelectionModel().getSelectedItem().getId();
							
							TabPane tabPaneAux = tabPane;
							tabPaneAux.getTabs().clear();
							
							/*
							 * Creates a temporary tabPane to change main tabPane
							 */
							for(TextFile textFile: ClientController.getUserFiles()) {
								Tab tab = new Tab("(" + textFile.getId() + ") " + textFile.getName());
								tab.setId(textFile.getId() + "");
								tabPaneAux.getTabs().add(tab);
							}

							tabPane = tabPaneAux;
							
							/*
							 * Selects active tab
							 */
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
						/*
						 * Update if the user does not have loaded files
						 */
						try {
							textArea.setText("");
							TabPane tabPaneAux = tabPane;
							tabPaneAux.getTabs().clear();
							
							/*
							 * Creates a temporary tabPane to change main tabPane
							 */
							for(TextFile textFile: ClientController.getUserFiles()) {
								Tab tab = new Tab("(" + textFile.getId() + ") " + textFile.getName());
								tab.setId(textFile.getId() + "");
								tabPaneAux.getTabs().add(tab);
							}
							
							tabPane = tabPaneAux;
							
							/*
							 * Selects first tab if it exists
							 */
							if(!tabPane.getTabs().isEmpty() && ClientController.openedTextFile == null) {
								ClientController.changeOpened(Integer.parseInt(
										tabPane.getSelectionModel().getSelectedItem().getId()));
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
	 * Shows system warning dialog
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

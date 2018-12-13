import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.TextFile;
import rmi.Service;

public class Client extends Application implements Initializable {
	
	@FXML
    private TabPane tabPane;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private MenuItem colaboratorsMenuItem;
    
	private Service textEditor;
	private String user;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("fxml/Client.fxml"));
		
		Scene scene = new Scene(root, 100, 100);
		
		primaryStage.setTitle("SisalPad");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
		});
	}	

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		startLookUp();	
		user = changeUser();
	}
	
	public void startLookUp() {
		try {
			textEditor = (Service) Naming.lookup("rmi://localhost:1050/TextEditor");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@FXML
    void closeMenuOnAction(ActionEvent event) {
		System.exit(0);
    }
	
	@FXML
    void menuNewOnAction(ActionEvent event) throws Exception {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        AnchorPane anchor = new AnchorPane();        
        
        TextField textField = new TextField();
        textField.setPromptText("Ex: Note 1");
        
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button button = new Button("Create");
        hBox.getChildren().add(button);
        
        GridPane grid = new GridPane();
        
        Separator separator1 = new Separator();
        separator1.setVisible(false);
        Separator separator2 = new Separator();
        separator2.setVisible(false);
        Separator separator3 = new Separator();
        separator3.setVisible(false);
        Separator separator4 = new Separator();
        separator4.setVisible(false);
        
        grid.addRow(0, new Label("File name:"));
        grid.addRow(1, separator1);
        grid.addRow(2, separator2);
        grid.addRow(3, textField);
        grid.addRow(4, separator3);
        grid.addRow(5, separator4);
        grid.addRow(6, hBox);
        grid.setScaleShape(true);        
        
        anchor.getChildren().add(grid);
        
        AnchorPane.setBottomAnchor(grid, 10.0);
        AnchorPane.setTopAnchor(grid, 10.0);
        AnchorPane.setLeftAnchor(grid, 10.0);
        AnchorPane.setRightAnchor(grid, 10.0);
        
        Scene dialogScene = new Scene(anchor, 140, 90);
       
        dialog.setTitle("New file");
        dialog.setScene(dialogScene);
        dialog.show();
        dialog.setResizable(false);
        
        button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					TextFile a = textEditor.newFile(textField.getText(), user);
					System.out.println(a.getId() + " " + a.getName() + " " + a.getOwner() + " " + a.getText());
					dialog.close();
				} catch (RemoteException e) {
					e.printStackTrace();
				}				
			}
        	
        });		
    }
	
	@FXML
    void userMenuOnAction(ActionEvent event) {
		changeUser();
    }
	
	String changeUser() {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        AnchorPane anchor = new AnchorPane();        
        
        TextField textField = new TextField();
        textField.setPromptText("Ex: user@email.com");
        
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button button = new Button("Set");
        hBox.getChildren().add(button);
        
        GridPane grid = new GridPane();
        
        Separator separator1 = new Separator();
        separator1.setVisible(false);
        Separator separator2 = new Separator();
        separator2.setVisible(false);
        Separator separator3 = new Separator();
        separator3.setVisible(false);
        Separator separator4 = new Separator();
        separator4.setVisible(false);
        
        grid.addRow(0, new Label("User e-mail:"));
        grid.addRow(1, separator1);
        grid.addRow(2, separator2);
        grid.addRow(3, textField);
        grid.addRow(4, separator3);
        grid.addRow(5, separator4);
        grid.addRow(6, hBox);
        grid.setScaleShape(true);        
        
        anchor.getChildren().add(grid);
        
        AnchorPane.setBottomAnchor(grid, 10.0);
        AnchorPane.setTopAnchor(grid, 10.0);
        AnchorPane.setLeftAnchor(grid, 10.0);
        AnchorPane.setRightAnchor(grid, 10.0);
        
        Scene dialogScene = new Scene(anchor, 140, 90);
       
        dialog.setTitle("User");
        dialog.setScene(dialogScene);
        dialog.show();
        dialog.setResizable(false);        
        dialog.setAlwaysOnTop(true);
        
        button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(textField.getText().isEmpty()) {
					user = "user@gmail.com";
				} else {
					user = textField.getText();
				}
				dialog.close();				
			}        	
        });
        
        return user;
	}
}

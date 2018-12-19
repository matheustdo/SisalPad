package view;

import java.io.IOException;
import java.rmi.NotBoundException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Client extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException, NotBoundException {	
		primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
		});

		primaryStage.getIcons().add(new Image("/assets/icon.png"));
		
		setServerDialog(primaryStage);		
		
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("fxml/HomePage.fxml"));
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("SisalPad");
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(438);
		primaryStage.setMinWidth(320);
		primaryStage.show();		
	}	
	
	private void setServerDialog(Stage primaryStage) throws IOException, NotBoundException {		
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("fxml/StartupDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();		
				
		Scene scene = new Scene(page, 200, 185);
		Stage dialog = new Stage();
		
		dialog.setOnCloseRequest(event -> {
			System.exit(0);
		});
		
		dialog.getIcons().add(new Image("/assets/icon.png"));
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(primaryStage);		
		dialog.setTitle("SisalPad");
		dialog.setScene(scene);
		dialog.setResizable(false);
		
		dialog.showAndWait();
	}
	
}

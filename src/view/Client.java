package view;

import java.io.IOException;
import java.rmi.NotBoundException;

import controller.fxml.SetServerDialogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Client extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException, NotBoundException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("fxml/SetServerDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();		
				
		Scene scene = new Scene(page, 200, 150);
		Stage dialog = new Stage();
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(primaryStage);		
		dialog.setTitle("SisalPad");
		dialog.setScene(scene);
		dialog.setResizable(false);
		
		SetServerDialogController setServerDialogController = loader.getController();
		setServerDialogController.setDialog(dialog);
		dialog.showAndWait();
		
		System.out.println(setServerDialogController.getIp());
		/*
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("fxml/Client.fxml"));
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("SisalPad");
		primaryStage.setScene(scene);
		primaryStage.show();		
		
		primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
		});
		*/
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

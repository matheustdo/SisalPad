import java.net.URL;
import java.rmi.Naming;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application implements Initializable {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("Client.fxml"));
		
		Scene scene = new Scene(root);
		
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
	}
	
	public void startLookUp() {
		try {
			Service textEditor = (Service) Naming.lookup("rmi://localhost:1050/TextEditor");
			System.out.println("Text echo: " + textEditor.echo("Hi"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}

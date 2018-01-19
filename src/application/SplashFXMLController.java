package application;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SplashFXMLController {
	
	@FXML
    private Pane rootPane;

	
	@FXML
    private void initialize() {
       new SplashScreen().start();
    }
	
	class SplashScreen extends Thread{
		@Override
		public void run() {
			try {
				Thread.sleep(3000);
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Parent root = null;
						try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFXML.fxml"));
							root = loader.load();
						} catch (IOException ex) {}
							
							Scene scene = new Scene(root);
							Stage stage = new Stage();
							stage.setTitle("Storm Water Simulator");
							stage.setScene(scene);
							stage.show();
							rootPane.getScene().getWindow().hide();
							
						}
					
				});
			} catch (InterruptedException ex) {
				
			}
		}
	}
}


package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.utilities.UiNetwork;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("main_layout.fxml"));
        Parent root = loader.load();
        
        Controller controller = loader.getController();
        
        root.setOnKeyPressed(event -> {
        	if (event.isControlDown()) {
        		UiNetwork network = controller.getUiNetwork();
        		if (!network.isBuildingRoute()) {
        			network.startBuildingRoute();
        		}
        	}
        });
        root.setOnKeyReleased(event -> {
        	if (!event.isControlDown()) {
        		UiNetwork network = controller.getUiNetwork();
        		if (network.isBuildingRoute()) {
        			network.endBuildingRoute();
        		}
        	}
        });
        
        primaryStage.setTitle("Railway Network");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(false);
        
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

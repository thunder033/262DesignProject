package FPTS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FPTSApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("SimulationView.fxml"));
        primaryStage.setTitle("ThunderForge FPTS");

        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();

        // Brian's Simulation Testing here



        // End Brian's Simulation Testing
    }

    // Comment to test branching
    public static void main(String[] args) {
        launch(args);
    }
}

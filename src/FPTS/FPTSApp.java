package FPTS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FPTSApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fpts.fxml"));
        primaryStage.setTitle("ThunderForge FPTS");

        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        // Brian's Simulation Testing here

        Simulation sim = new Simulation(100.00);
        sim.setBullAlgorithm(.12, 12, "month", 100);
        primaryStage.setTitle(Double.toString(sim.getCurrentAlgorithm().step()));
        primaryStage.setTitle(Double.toString(sim.revertToActualValue()));

        // End Brian's Simulation Testing
    }

    // Comment to test branching
    public static void main(String[] args) {
        launch(args);
    }
}

package FPTS;

import FPTS.Data.FPTSData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FPTSApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // UI structure is kept in fxml files.
        Parent root = FXMLLoader.load(getClass().getResource("FPTS.fxml"));

        FPTSData.loadBins();

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/assets/appIcon.png")));
        primaryStage.setTitle("ThunderForge FPTS");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

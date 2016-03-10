package FPTS;

import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Models.MarketEquity;
import FPTS.Models.MarketEquityBin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FPTSApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // UI structure is kept in fxml files.
        Parent root = FXMLLoader.load(getClass().getResource("FPTS.fxml"));

        System.out.println("Loading bins...");
        ArrayList<Class<? extends DataBin>> binTypes = new ArrayList<>();
        binTypes.add(MarketEquityBin.class);

        FPTSData.loadBins(binTypes);

        ArrayList<MarketEquity> equities = FPTSData.getInstances(MarketEquity.class);

        for(MarketEquity equity : equities) {
            System.out.println(equity.getName());
        }

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/assets/appIcon.png")));
        primaryStage.setTitle("ThunderForge FPTS");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

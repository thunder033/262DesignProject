package FPTS;

import FPTS.Core.View;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Models.MarketEquity;
import FPTS.Models.MarketEquityBin;
import FPTS.Models.MarketIndex;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * @author: Greg Rozmarynowycz
 * Created: 3/11/2016
 * Revised: 3/11/2016
 * Description: The initialization point for
 * the application via a launch command which automates
 * much of running the thread.  An initial view is launched
 * and CSV file data is loaded into respective bins.
 */

public class FPTSApp extends Application {

    FPTSData data;
    protected View currentView;


    public FPTSData getData()
    {
        return data;
    }

    public View getCurrentView()
    {
        return currentView;
    }

    public void setCurrentView(View view)
    {
        currentView.Exit();
        currentView = view;
        currentView.Load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        // UI structure is kept in fxml files.
        Parent root = FXMLLoader.load(getClass().getResource("/assets/fpts.fxml"));

        System.out.println("Loading data bins...");
        ArrayList<Class<? extends DataBin>> binTypes = new ArrayList<>();
        binTypes.add(MarketEquityBin.class);

        data = FPTSData.getDataRoot();
        data.loadBins(binTypes);

        ArrayList<MarketEquity> equities = data.getInstances(MarketEquity.class);

        for(MarketEquity equity : equities) {
            System.out.println(equity.getName());
        }

        MarketIndex NASDAQ100 = MarketIndex.class.cast(data.getInstanceById(MarketEquity.class, "NASDAQ100"));
        System.out.println(NASDAQ100.getName() + " contains " + NASDAQ100.getEquities().size() + " equities");

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/assets/appIcon.png")));
        primaryStage.setTitle("ThunderForge FPTS");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

package FPTS.Core;

import FPTS.Core.View;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Models.MarketEquity;
import FPTS.Models.MarketEquityBin;
import FPTS.Models.MarketIndex;
import FPTS.Views.LoginView;
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
    public Stage mainStage;


    public FPTSData getData()
    {
        return data;
    }

    public Stage getStage() { return mainStage; }

    public View getCurrentView()
    {
        return currentView;
    }

    public void setCurrentView(View view)
    {
        //currentView.Exit();
        currentView = view;
        currentView.Load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
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

        mainStage = new Stage();
        setCurrentView(new LoginView(this));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/assets/appIcon.png")));
        mainStage.setTitle("ThunderForge FPTS");
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

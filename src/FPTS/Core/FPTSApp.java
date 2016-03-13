package FPTS.Core;

import FPTS.Core.View;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Models.*;
import FPTS.Views.PortfolioView;
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
    private Stage stage;


    public FPTSData getData() {
        return data;
    }

    public View getCurrentView()
    {
        return currentView;
    }

    public void setCurrentView(View view) {
        if(currentView != null) {
            currentView.Exit();
        }

        currentView = view;
        stage.setScene(currentView.getScene());
        currentView.Load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        System.out.println("Loading data bins...");
        ArrayList<Class<? extends DataBin>> binTypes = new ArrayList<>();
        binTypes.add(MarketEquityBin.class);
        binTypes.add(CashAccountBin.class);
        binTypes.add(EquityBin.class);
        binTypes.add(PortfolioBin.class);

        data = FPTSData.getDataRoot();
        data.loadBins(binTypes);

        ArrayList<Portfolio> portfolios = data.getInstances(Portfolio.class);
        System.out.println("Loaded " + portfolios.size() + " portfolios");

        Portfolio portfolio = portfolios.get(0);
        System.out.println(portfolio.id);
        System.out.println(portfolio.getHoldings().size());

        System.out.println(data.getInstanceById(Equity.class, "2").getShares());

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/assets/appIcon.png")));
        primaryStage.setTitle("ThunderForge FPTS");

        //Hard code create the portfolio view
        View view = new PortfolioView(this);
        setCurrentView(view);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

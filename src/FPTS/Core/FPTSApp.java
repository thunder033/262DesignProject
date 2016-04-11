package FPTS.Core;

import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Models.*;
import FPTS.Views.LoginView;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Greg Rozmarynowycz
 * Created: 3/11/16
 * Revised: 3/13/16
 * Description: The initialization point for
 * the application via a launch command which automates
 * much of running the thread.  An initial view is launched
 * and CSV file data is loaded into respective bins.
 */

public class FPTSApp extends Application {

    private FPTSData data;
    private View currentView;
    private Map<String, Stage> stageMap;
    
    /**
     * @return a reference to the data root
     */
    public FPTSData getData() {
        return data;
    }

    private static FPTSApp instance = null;

    public static FPTSApp getInstance(){
        return instance;
    }

    /**
     * @return the current view
     */
    public View getCurrentView() {
        return currentView;
    }

    public void CloseStage(String name){
        stageMap.get(name).close();
        currentView._controller.refreshView();
    }

    public Stage getStage(){
        return stageMap.get("Main");
    }

    public Stage getStage(String name){
        return stageMap.get(name);
    }

    /**
     * Change the current view of the app, triggering exit and load
     * functions on the respective views
     * @param view the view to change to
     */
    public void loadView(View view) {
        if(currentView != null) {
            currentView.Exit();
        }

        if(view.newWindow){
            view.getScene();
            view.Load();
            stageMap.put(view.getClass().getSimpleName(), view._stage);
        } else {
            currentView = view;
            getStage().setScene(currentView.getScene());
            getStage().setTitle(currentView.title);

            currentView.Load();
        }

    }

    public void centerWindow(Stage stage) {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        stageMap = new HashMap<>();
        stageMap.put("Main", primaryStage);

        System.out.println("Loading data bins...");
        ArrayList<Class<? extends DataBin>> binTypes = new ArrayList<>();
        binTypes.add(MarketEquityBin.class);
        binTypes.add(CashAccountBin.class);
        binTypes.add(EquityBin.class);
        binTypes.add(PortfolioBin.class);
        binTypes.add(TransactionBin.class);
        binTypes.add(WatchListBin.class);

        data = FPTSData.getDataRoot();
        data.loadBins(binTypes);

        loadView(new LoginView(this));
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/assets/appIcon.png")));
        primaryStage.setTitle("ThunderForge FPTS");
        primaryStage.show();
        centerWindow(primaryStage);

        List<String> params = this.getParameters().getRaw();
        if(params.size() == 2 && params.get(0).equals("-delete")){
            System.out.println("Delete portfolio " + params.get(1));
            Portfolio portfolio = data.getInstanceById(Portfolio.class, params.get(1));
            if(portfolio != null){
                portfolio.hardDelete();
                data.writeAll();
            }
        }
    }

    public void stop(){
        getData().getInstances(WatchList.class).stream().forEach(WatchList::endWatch);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

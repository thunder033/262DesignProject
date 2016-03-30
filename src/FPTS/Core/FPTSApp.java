package FPTS.Core;

import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Models.*;
import FPTS.Views.LoginView;
import FPTS.Search.SelectSearchListener;
import FPTS.Controllers.AddHoldingController;

import javafx.application.Application;
import javafx.scene.image.Image;
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

public class FPTSApp extends Application implements SelectSearchListener {

    FPTSData data;
    protected View currentView;
    private Map<String, Stage> stageMap;
    public String searchResult;

    private ArrayList<SelectSearchListener> selectSearchListeners = new ArrayList<SelectSearchListener>();
        
    public void addListener(SelectSearchListener toAdd) {
        selectSearchListeners.add(toAdd);
    }
    
    /**
     * @return a reference to the data root
     */
    public FPTSData getData() {
        return data;
    }

    /**
     * Set the public Search Result.
     */
    public void setSearchResult(String result) {
        searchResult = result;
    }

    @Override
    public void SearchResultSelected() {
        
        for (SelectSearchListener hl : selectSearchListeners)
            hl.SearchResultSelected();
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        stageMap = new HashMap<>();
        stageMap.put("Main", primaryStage);

        System.out.println("Loading data bins...");
        ArrayList<Class<? extends DataBin>> binTypes = new ArrayList<>();
        binTypes.add(MarketEquityBin.class);
        binTypes.add(CashAccountBin.class);
        binTypes.add(EquityBin.class);
        binTypes.add(PortfolioBin.class);
        binTypes.add(TransactionBin.class);

        data = FPTSData.getDataRoot();
        data.loadBins(binTypes);

        loadView(new LoginView(this));
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/assets/appIcon.png")));
        primaryStage.setTitle("ThunderForge FPTS");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package FPTS.Core;

import FPTS.Models.Portfolio;
import FPTS.Views.SearchView;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * @author: Greg Rozmarynowycz
 * Created: 3/11/2016
 * Revised: 3/11/2016
 * Description: A superclass for controllers.
 * These components handle requests for user input
 * in the UI that alters the internal state (model) of FPTS.
 * Conversely, it handles communication between a changed model
 * and the UI.  Observer implementation helps with notifications
 * between the UI and model components.
 */

public class Controller implements Observer, Initializable {

    protected FPTSApp _app;
    protected static Portfolio _portfolio;

    /**
     * Operations to perform when the controller loads. Creates a default portfolio if
     * necessary since all controllers have the context of a portfolio
     * @param app reference to the app the controller is in
     * @param portfolio reference to current active portfolio in the system
     */
    public void Load(FPTSApp app, Portfolio portfolio)
    {
        _app = app;

        if(portfolio == null)
        {
            System.out.println("create default portfolio");
            portfolio = new Portfolio("default","");
            portfolio.isPersistent = false;
        }

        _portfolio = portfolio;
        _portfolio.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _app = FPTSApp.getInstance();
        refreshView();
    }

    /**
     * operations to execute before the controller is exited from
     */
    public void Exit()
    {
        _portfolio.deleteObserver(this);
    }

    public void update(Observable subject, Object obj)
    {
        refreshView();
    }

    /**
     * update the view to reflect changes in data
     */
    public void refreshView() {

    }

    public void handleSearch(ActionEvent actionEvent) {
        _app.loadView(new SearchView(_app));
    }
}

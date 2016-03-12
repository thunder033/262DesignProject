package FPTS.Core;

import FPTS.Models.Portfolio;
import javafx.application.Application;
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

    protected Application _app;
    protected Portfolio _portfolio;

    public void Load(Application app, Portfolio portfolio)
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
        refreshView();
    }

    public void Exit()
    {
        _portfolio.deleteObserver(this);
    }

    public void update(Observable subject, Object obj)
    {
        refreshView();
    }

    public void refreshView() {

    }
}

package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Data.Authenticator;
import FPTS.Models.Portfolio;
import FPTS.Views.PortfolioView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * @author: Alexander Kidd
 * Created: 3/11/2016
 * Revised: 3/12/2016
 * Description: This controller handles requests
 * to alternate the register UI based on internal
 * model changes and UI changes that alter the internal model state.
 */
public class RegisterController extends Controller {

    @FXML TextField username;
    @FXML TextField password;
    @FXML TextField passwordConfirm;
    @FXML Text errorMessage;

    @FXML protected void registerUser() {
        String error = "";

        if(!password.getText().equals(passwordConfirm.getText())) {
            error = "Password do not match";
        }

        if(password.getLength() == 0) {
            error = "Password is required";
        }

        if(username.getLength() == 0) {
            error = "Username is required";
        }
        else if(_app.getData().getInstanceById(Portfolio.class, username.getText()) != null) {
            error = "Portfolio with " + username.getText() + "already exists";
        }

        if(error.length() == 0){
            Portfolio portfolio = new Portfolio(username.getText(), Authenticator.makeHash(password.getText()));
            _app.getData().addInstance(portfolio);
            _portfolio = portfolio;
            portfolio.notifyObservers();
            _app.loadView(new PortfolioView(_app));
        }
        else {
            errorMessage.setText(error);
        }
    }
}

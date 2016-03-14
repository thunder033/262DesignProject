package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Data.Authenticator;
import FPTS.Models.Portfolio;
import FPTS.Views.PortfolioView;
import FPTS.Views.RegisterView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * @author: Alexander Kidd
 * Created: 3/11/2016
 * Revised: 3/12/2016
 * Description: This controller handles requests
 * to alternate the login UI based on internal
 * model changes and UI changes that alter the internal model state.
 */
public class LoginController extends Controller {
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Text errorMessage;

    @FXML
    protected void loginUser() {
        Portfolio portfolio = _app.getData().getInstanceById(Portfolio.class, username.getText());
        if(portfolio != null){
            if(portfolio.validateHash(Authenticator.makeHash(password.getText()))){
                _portfolio = portfolio;
                _app.loadView(new PortfolioView(_app));
            } else {
                errorMessage.setText("No portfolio found for username and password");
            }
        }
        else {
            errorMessage.setText("No portfolio found for username and password");
        }
    }

    @FXML
    protected void registerRedirect() {
        _app.loadView(new RegisterView(_app));
    }
}
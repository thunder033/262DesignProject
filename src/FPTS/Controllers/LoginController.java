package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Core.View;
import FPTS.Views.RegisterView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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

    @FXML
    protected void loginUser() {
        System.out.println(username.getText());
    }

    @FXML
    protected void registerRedirect() {
        _app.setCurrentView(new RegisterView(_app));
    }
}
package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Core.View;
import FPTS.Data.CSV;
import FPTS.Views.LoginView;
import FPTS.Views.RegisterView;
import javafx.fxml.FXML;

/**
 * @author: Alexander Kidd
 * Created: 3/11/2016
 * Revised: 3/12/2016
 * Description: This controller handles requests
 * to alternate the register UI based on internal
 * model changes and UI changes that alter the internal model state.
 */
public class RegisterController extends Controller {

    @FXML protected void registerUser() {
        _app.setCurrentView(new LoginView(_app));
    }
}

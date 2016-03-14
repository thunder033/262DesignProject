package FPTS.Views;

import FPTS.Core.FPTSApp;
import FPTS.Core.View;

/**
 * @author: Alexander Kidd
 * Created: 3/12/2016
 * Revised: 3/12/2016
 * Description: This class is responsible for showing
 * the UI associated with logging the user in.
 */
public class LoginView extends View {

    public LoginView(FPTSApp app){
        super(app);
        _fxmlName = "login.fxml";
        width = 300;
        height = 275;
        title = "FPTS - Search Equities";
    }

}

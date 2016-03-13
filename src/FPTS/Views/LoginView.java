package FPTS.Views;

import FPTS.Core.View;
import FPTS.Core.FPTSApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

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
    }

    @Override
    public void Load() {
        try {
            Parent root = FXMLLoader.load(_app.getClass().getResource("/assets/login.fxml"));
            _app.mainStage.setScene(new Scene(root, 300, 275));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

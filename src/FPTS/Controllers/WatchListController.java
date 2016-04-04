package FPTS.Controllers;

import FPTS.Core.Controller;
import javafx.fxml.FXML;

/**
 * @author: Alexander Kidd
 * Created: 4/4/2016
 * Revised: 4/4/2016
 * Description: Facilitates in communication
 * between the watch list logic and the watch list UI View.
 */
public class WatchListController extends Controller {
    @FXML
    private void handleTimer() {
        System.out.println("Timer Set!");
    }
}

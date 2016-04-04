package FPTS.Views;

import FPTS.Core.FPTSApp;
import FPTS.Core.View;

/**
 * @author: Alexander Kidd
 * Created: 4/4/2016
 * Revised: 4/4/2016
 * Description: Responsible for displaying relevant
 * watch list components, such as a means to change
 * the timer that updates the watch list.  The watch list
 * is a group of user-specified holdings that the user wants
 * to be notified about in real-time.
 */
public class WatchListView extends View {
    public WatchListView(FPTSApp app) {
        super(app);
        width = 800;
        height = 800;
        _fxmlName = "watchList.fxml";
        newWindow = true;
    }
}

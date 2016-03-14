package FPTS.Views;

import FPTS.Core.View;
import FPTS.Core.FPTSApp;

/**
 * @author: Alexander Kidd
 * Created: 3/12/2016
 * Revised: 3/12/2016
 * Description: In charge of displaying search component in the UI.
 */
public class SearchView extends View {
    public SearchView(FPTSApp app) {
        super(app);
        _fxmlName = "search.fxml";
    }
}

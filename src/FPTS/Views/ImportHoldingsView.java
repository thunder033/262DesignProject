package FPTS.Views;

import FPTS.Core.FPTSApp;
import FPTS.Core.View;

/**
 * Created by traub_000 on 3/25/2016.
 *
 * Description: Responsible for displaying the import holdings
 * screen to the user
 */
public class ImportHoldingsView extends View {
    public ImportHoldingsView(FPTSApp app){
        super(app);
        _fxmlName = "importHoldings.fxml";
        title = "FPTS - Import Holdings";
    }
}

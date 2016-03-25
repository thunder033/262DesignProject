package FPTS.Views;

import FPTS.Core.FPTSApp;
import FPTS.Core.View;

/**
 * Created by traub_000 on 3/25/2016.
 */
public class ImportHoldingsView extends View {
    public ImportHoldingsView(FPTSApp app){
        super(app);
        _fxmlName = "importHoldings.fxml";
        width = 300;
        height = 400;
        title = "FPTS - Import Holdings";
    }
}

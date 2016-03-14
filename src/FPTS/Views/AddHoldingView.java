package FPTS.Views;

import FPTS.Core.FPTSApp;
import FPTS.Core.View;

/**
 * Created by gjr8050 on 3/13/2016.
 */
public class AddHoldingView extends View {

    public AddHoldingView(FPTSApp app){
        super(app);
        _fxmlName = "addHolding.fxml";
        width = 300;
        height = 400;
        newWindow = true;
        title = "FPTS - Add New Holding";
    }
}

package FPTS.Views;

import FPTS.Core.FPTSApp;
import FPTS.Core.View;

/**
 * @author: Alexander Kidd
 * Created: 3/12/2016
 * Revised: 3/25/2016
 * Description: In charge of setting up UI for holdings transactions.
 */
public class TransactionView extends View {

    public TransactionView(FPTSApp app) {
        super(app);
        _fxmlName = "transaction.fxml";
        width = 500;
        height = 520;
        newWindow = true;
    }
}

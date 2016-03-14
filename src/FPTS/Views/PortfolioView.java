package FPTS.Views;

import FPTS.Core.FPTSApp;
import FPTS.Core.View;

/**
 * @author: Alexander Kidd
 * Created: 3/12/2016
 * Revised: 3/12/2016
 * Description: Responsible for displaying the portfolio to
 * the user in the UI.
 */
public class PortfolioView extends View {
    public PortfolioView(FPTSApp app){
        super(app);
        _fxmlName = "portfolio.fxml";
    }
}

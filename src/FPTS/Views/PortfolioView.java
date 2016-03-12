package FPTS.Views;

import FPTS.Controllers.PortfolioController;
import FPTS.Core.View;
import javafx.application.Application;

/**
 * @author: Alexander Kidd
 * Created: 3/12/2016
 * Revised: 3/12/2016
 * Description: Responsible for displaying the portfolio to
 * the user in the UI.
 */
public class PortfolioView extends View {
    public PortfolioView(Application app){
        super(app);
        _fxmlName = "portfolio.fxml";
        _controller = new PortfolioController();
    }
}

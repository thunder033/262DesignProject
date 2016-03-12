package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Models.Portfolio;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * @author: Alexander Kidd
 * Created: 3/11/2016
 * Revised: 3/11/2016
 * Description: This controller handles requests
 * to alternate the portfolio UI based on internal
 * model changes and UI changes that alter the internal model state.
 */
public class PortfolioController extends Controller {
    @FXML
    private Text portfolioName;

    public PortfolioController()
    {
        _portfolio = new Portfolio("greg", "asdj;flkas;d");
    }

    @Override
    public void refreshView()
    {
        System.out.println("refresh controller");
        portfolioName.setText(String.format("%1$s's Portfolio", _portfolio.getUsername()));
    }
}

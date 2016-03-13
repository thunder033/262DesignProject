package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Data.FPTSData;
import FPTS.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
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
    @FXML private Text portfolioName;
    @FXML private TableView holdingsPane;

    public PortfolioController() {
        _portfolio = new Portfolio("Greg", "asdj;flkas;d");

        MarketEquity marketEquity = FPTSData.getDataRoot().getInstanceById(MarketEquity.class, "ADBE");
        Equity equity = new Equity(marketEquity);
        equity.addValue(123.54f);
        _portfolio.addHolding(equity);
        _portfolio.addHolding(new Equity(FPTSData.getDataRoot().getInstanceById(MarketEquity.class, "AMZN")));
        _portfolio.addHolding(new CashAccount("Bank Account 1", 225));
        _portfolio.addHolding(new Equity(FPTSData.getDataRoot().getInstanceById(MarketEquity.class, "BIDU")));
    }



    @Override
    public void refreshView()
    {
        System.out.println("refresh controller");
        portfolioName.setText(String.format("%1$s's Portfolio", _portfolio.getUsername()));

        ObservableList<Holding> holdings = FXCollections.observableArrayList(_portfolio.getHoldings());
        System.out.println(holdings.size());
        holdingsPane.setItems(holdings);
    }
}

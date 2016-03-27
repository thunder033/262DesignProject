package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Alexander Kidd
 * Created: 3/11/2016
 * Revised: 3/27/2016
 * Description: This controller handles requests
 * to alternate the transaction UI based on internal
 * model changes and UI changes that alter the internal model state.
 */
public class TransactionController extends Controller {

    @FXML NumericField holdingValue;
    @FXML Text errorMessage;

    @FXML Text holdingInfo1;
    @FXML Text holdingInfo2;
    @FXML Text valueInfo1;
    @FXML Text valueInfo2;

    @FXML ChoiceBox<String> holdingTypes;
    @FXML ChoiceBox<String> holdingTypes2;

    @FXML Button transactionControl;

    @FXML Button searchButton1Transaction;
    @FXML Button searchButton2Transaction;

    @FXML DatePicker transactionDate;

    Map<String, Class<? extends Model>> typesMap;

    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);

        typesMap = new HashMap<>();
        for(Holding holding : portfolio.getHoldings()) {
            typesMap.put(holding.getName(), holding.getType().equals(Equity.type) ? Equity.class : CashAccount.class);
        }

        holdingTypes.getItems().setAll(typesMap.keySet());
        holdingTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));
        holdingTypes2.getItems().setAll(typesMap.keySet());
        holdingTypes2.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));

        holdingValue.addEventHandler(KeyEvent.KEY_RELEASED, event -> DisplayEquityInfo());
    }

    void setInputLabels(String holdingType){
        switch (holdingType) {
            case Equity.type:
                holdingValue.promptTextProperty().setValue("Transfer amount in shares");
                transactionControl.setText("Add External Equity");
                DisplayEquityInfo();
                break;
            case CashAccount.type:
                holdingValue.promptTextProperty().setValue("Transfer amount in value($)");
                transactionControl.setText("Create Cash Account");
                holdingInfo1.setText("");
                break;
        }
    }

    private void DisplayEquityInfo(){
        holdingInfo1.setText("");
        valueInfo1.setText("");
        if(Equity.type.equals(holdingTypes.getValue())){
            MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, holdingTypes.getValue());
            if(equity != null) {
                holdingInfo1.setText(equity.getTickerSymbol() + " - " + equity.getName() + " $" + equity.getSharePrice());
                if(holdingValue.getText().length() > 0){
                    float shares = Float.parseFloat(holdingValue.getText());
                    float value = shares * equity.getSharePrice();
                    valueInfo1.setText(String.format("$%.2f", value));
                }
            }
        }
    }

    public void makeTransaction(ActionEvent actionEvent) {
        System.out.println("Transaction Submitted.");
    }
}

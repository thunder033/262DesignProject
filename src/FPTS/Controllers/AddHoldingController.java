package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.*;
import FPTS.Views.AddHoldingView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gjr8050 on 3/13/2016.
 */
public class AddHoldingController extends Controller {
    @FXML TextField holdingName;
    @FXML NumericField holdingValue;
    @FXML Text errorMessage;
    @FXML Text holdingInfo;
    @FXML Text valueInfo;
    @FXML ChoiceBox<String> holdingTypes;
    @FXML Button addHoldingControl;

    Map<String, Class<?>> typesMap;

    private void DisplayEquityInfo(){
        holdingInfo.setText("");
        valueInfo.setText("");
        if(holdingTypes.getValue().equals("Equity")){
            MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, holdingName.getText());
            if(equity != null) {
                holdingInfo.setText(equity.getTickerSymbol() + " - " + equity.getName() + " $" + equity.getSharePrice());
                if(holdingValue.getText().length() > 0){
                    float shares = Float.parseFloat(holdingValue.getText());
                    float value = shares * equity.getSharePrice();
                    valueInfo.setText(String.format("$%.2f", value));
                }
            }
        }
    }

    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);

        typesMap = new HashMap<>();
        typesMap.put("Equity", Equity.class);
        typesMap.put("Cash Account", CashAccount.class);
        holdingTypes.getItems().setAll(typesMap.keySet());

        holdingTypes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch(newValue) {
                    case "Equity":
                        holdingName.promptTextProperty().setValue("Ticker Symbol");
                        holdingValue.promptTextProperty().setValue("Shares");
                        addHoldingControl.setText("Add External Equity");
                        DisplayEquityInfo();
                        break;
                    case "Cash Account":
                        holdingName.promptTextProperty().setValue("Account Name");
                        holdingValue.promptTextProperty().setValue("Value($)");
                        addHoldingControl.setText("Create Cash Account");
                        holdingInfo.setText("");
                        break;
                }
            }
        });

        holdingName.addEventHandler(KeyEvent.KEY_RELEASED, event -> DisplayEquityInfo());
        holdingValue.addEventHandler(KeyEvent.KEY_RELEASED, event -> DisplayEquityInfo());
    }

    public void addHolding(ActionEvent actionEvent) {
        Holding holding = null;
        String error = "";
        float value = 0;

        if(holdingName.getText().length() == 0){
            error = "A ticker symbol or account name is required";
        }

        if(holdingValue.getText().length() == 0){
            error = "You must provide a value for the holding";
        }
        else {
            value = Float.parseFloat(holdingValue.getText());
        }

        switch(holdingTypes.getValue()){
            case "Equity":
                MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, holdingName.getText());
                if(equity != null){
                    holding = new Equity(equity);
                    holding.addValue(value * equity.getSharePrice());
                } else {
                    error = "No Equity was found for " + holdingName.getText();
                }
                break;
            case "Cash Account":
                holding = new CashAccount(holdingName.getText(), value);
                break;
        }

        if(holding != null && error.length() == 0){
            _app.getData().addInstance(Model.class.cast(holding));
            _portfolio.addHolding(holding);
            _portfolio.save();
            _app.CloseStage(AddHoldingView.class.getSimpleName());
        }
    }
}

package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.*;
import FPTS.Views.AddHoldingView;
import FPTS.Views.SearchView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

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
    @FXML Image searchIcon;
    @FXML Button searchButton;

    Map<String, Class<? extends Model>> typesMap;

    private void DisplayEquityInfo(){
        holdingInfo.setText("");
        valueInfo.setText("");
        if(Equity.type.equals(holdingTypes.getValue())){
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

    void setInputLabels(String holdingType){
        switch (holdingType) {
            case Equity.type:
                holdingName.promptTextProperty().setValue("Ticker Symbol");
                holdingValue.promptTextProperty().setValue("Shares");
                addHoldingControl.setText("Add External Equity");
                DisplayEquityInfo();
                break;
            case CashAccount.type:
                holdingName.promptTextProperty().setValue("Account Name");
                holdingValue.promptTextProperty().setValue("Value($)");
                addHoldingControl.setText("Create Cash Account");
                holdingInfo.setText("");
                break;
        }
    }

    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        searchIcon = new Image(this.getClass().getResourceAsStream("/assets/search.png"));
        super.Load(app, portfolio);

        typesMap = new HashMap<>();
        typesMap.put(Equity.type, Equity.class);
        typesMap.put(CashAccount.type, CashAccount.class);

        holdingTypes.getItems().setAll(typesMap.keySet());
        holdingTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));
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

        if(error.length() == 0){
            try {
                holding = _portfolio.addHolding(typesMap.get(holdingTypes.getValue()), holdingName.getText(), value);
            } catch (UnknownMarketEquityException ex){
                error = ex.getMessage();
            }
        }

        if(holding != null && error.length() == 0){
            _app.CloseStage(AddHoldingView.class.getSimpleName());
        }
        else {
            errorMessage.setText(error);
        }
    }

    public void handleSearch(ActionEvent actionEvent) {
        _app.loadView(new SearchView(_app));
    }
}

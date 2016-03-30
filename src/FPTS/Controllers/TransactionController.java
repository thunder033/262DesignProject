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
import javafx.util.StringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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

    @FXML NumericField transactionAmount;
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

    private final String dateTimeFormat = "yyyy-MM-dd";

    Map<String, Class<? extends Model>> typesMap;

    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);

        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(dateTimeFormat);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };

        transactionDate.setConverter(converter);
        transactionDate.setPromptText(dateTimeFormat);

        typesMap = new HashMap<>();
        for(Holding holding : portfolio.getHoldings()) {
            typesMap.put(holding.getName(), holding.getType().equals(Equity.type) ? Equity.class : CashAccount.class);
        }

        holdingTypes.getItems().setAll(typesMap.keySet());
        holdingTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));
        holdingTypes2.getItems().setAll(typesMap.keySet());
        holdingTypes2.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));

        transactionAmount.addEventHandler(KeyEvent.KEY_RELEASED, event -> DisplayEquityInfo());
    }

    void setInputLabels(String holdingType){
        switch (holdingType) {
            case Equity.type:
                transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                transactionControl.setText("Add External Equity");
                DisplayEquityInfo();
                break;
            case CashAccount.type:
                transactionAmount.promptTextProperty().setValue("Transfer amount in value($)");
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
                if(transactionAmount.getText().length() > 0){
                    float shares = Float.parseFloat(transactionAmount.getText());
                    float value = shares * equity.getSharePrice();
                    valueInfo1.setText(String.format("$%.2f", value));
                }
            }
        }
    }

    public void makeTransaction(ActionEvent actionEvent) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(transactionDate.getValue().toString() + "T" + sdf.format(cal.getTime()).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Transaction newTxn = new Transaction.Builder()
            //    .source(_app.getData().getInstanceById(MarketEquity.class, holdingTypes.getValue()))
                .sourcePrice(Float.parseFloat(valueInfo1.getText()))
            //    .destination(_app.getData().getInstanceById(MarketEquity.class, holdingTypes2.getValue()))
                .destinationPrice(Float.parseFloat(valueInfo2.getText()))
                .dateTime(date)
                .value(Float.parseFloat(transactionAmount.getText()))
                .build();

        try {
            newTxn.execute();
            errorMessage.setText("Transaction Successful.");
        } catch (InvalidTransactionException e) {
            System.err.println(e.getMessage());
        } catch (TransactionReExecutionException e) {
            System.err.println(e.getMessage());
        }
    }
}

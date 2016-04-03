package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Models.*;
import FPTS.Transaction.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import java.util.*;

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
    @FXML TextField holdingField1;
    @FXML TextField holdingField2;

    @FXML ChoiceBox<Holding> holdingTypes;
    @FXML ChoiceBox<Holding> holdingTypes2;

    @FXML Button transactionControl;

    @FXML Button searchButton1Transaction;
    @FXML Button searchButton2Transaction;

    @FXML DatePicker transactionDate;

    private final String dateTimeFormat = "yyyy-MM-dd";

    ArrayList<Holding> holdingsArrayList = new ArrayList<>();
    ObservableList<Holding> holdingsObservableList = FXCollections.observableList(holdingsArrayList);

    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);

        holdingsObservableList.addListener((ListChangeListener<Holding>) c -> System.out.println("Holdings List Changed."));

        StringConverter dateConverter = new StringConverter<LocalDate>() {
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

        StringConverter holdingConverter = new StringConverter<Holding>() {
            @Override
            public String toString(Holding holding) {
                return holding.getName();
            }
            @Override
            public Holding fromString(String string) {
                System.err.println("Do Not Attempt Conversion From String In This Manner.");
                return null;
            }
        };

        holdingTypes.setConverter(holdingConverter);
        holdingTypes2.setConverter(holdingConverter);

        transactionDate.setConverter(dateConverter);
        transactionDate.setPromptText(dateTimeFormat);

        holdingsObservableList.addAll(portfolio.getHoldings());

        for(Holding holding : holdingsObservableList) {
            holdingTypes.getItems().add(holding);
            holdingTypes2.getItems().add(holding);
        }

        holdingTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));
        holdingTypes2.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));

        transactionAmount.addEventHandler(KeyEvent.KEY_RELEASED, event -> DisplayEquityInfo());
    }

    void setInputLabels(Holding holding) {
        switch (holding.getType()) {
            case Equity.type:
                transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                break;
            case CashAccount.type:
                transactionAmount.promptTextProperty().setValue("Transfer amount in value($)");
                break;
        }

        if(holdingTypes.getSelectionModel().getSelectedItem() != null) {
            if(holdingTypes.getSelectionModel().getSelectedItem().getType().equals(CashAccount.type)) {
                holdingField1.setDisable(true);
                holdingField1.setText("1");
            }
            else {
                holdingField1.setDisable(false);
            }
        }
        if(holdingTypes2.getSelectionModel().getSelectedItem() != null) {
            if(holdingTypes2.getSelectionModel().getSelectedItem().getType().equals(CashAccount.type)) {
                holdingField2.setDisable(true);
                holdingField2.setText("1");
            }
            else {
                holdingField2.setDisable(false);
            }
        }

        DisplayEquityInfo();
        refreshView();
    }

    private void DisplayEquityInfo(){
        holdingInfo1.setText("");
        valueInfo1.setText("");
        if(Equity.type.equals(holdingTypes.getValue().getType())){
            MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, holdingTypes.getValue().getName());
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
                .source(holdingsArrayList.get(holdingTypes.getSelectionModel().getSelectedIndex()))
                .sourcePrice(Float.parseFloat(valueInfo1.getText()))
                .destination(holdingsArrayList.get(holdingTypes2.getSelectionModel().getSelectedIndex()))
                .destinationPrice(Float.parseFloat(valueInfo2.getText()))
                .dateTime(date)
                .value(Float.parseFloat(transactionAmount.getText()))
                .build();

        //Entry.EntryFormat.;

        try {
            newTxn.execute();
            errorMessage.setText("Transaction Successful.");
        } catch (InvalidTransactionException e) {
            System.err.println(e.getMessage());
            errorMessage.setText("Invalid Transaction.");
        } catch (TransactionReExecutionException e) {
            System.err.println(e.getMessage());
            errorMessage.setText("Transaction Re-executed.");
        }
    }
}

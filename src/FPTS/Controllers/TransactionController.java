package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
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
 * Revised: 4/3/2016
 * Description: This controller handles requests
 * to alternate the transaction UI based on internal
 * model changes and UI changes that alter the internal model state.
 */
public class TransactionController extends Controller {
    @FXML Text errorMessage;

    @FXML TextField sharePriceField1;
    @FXML TextField sharePriceField2;

    @FXML ChoiceBox<Holding> holdingTypes;
    @FXML ChoiceBox<Holding> holdingTypes2;

    @FXML NumericField transactionAmount;

    @FXML Button transactionControl;

    @FXML Button searchButton1Transaction;
    @FXML Button searchButton2Transaction;

    @FXML DatePicker transactionDate;

    private final String dateTimeFormat = "yyyy-MM-dd";

    ArrayList<Holding> holdingsArrayList = new ArrayList<>();
    ObservableList<Holding> holdingsObservableList = FXCollections.observableList(holdingsArrayList);

    Entry.EntryFormat txnFormat = null;

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
                if(holding == null) {
                    return "External Holding";
                }
                if(holding.getType().equals(CashAccount.type)) {
                    return holding.getName() + " (" + holding.getValue() + ")";
                }
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

        holdingsObservableList.add(0, null);
        holdingsObservableList.addAll(portfolio.getHoldings());

        for(Holding holding : holdingsObservableList) {
            holdingTypes.getItems().add(holding);
            holdingTypes2.getItems().add(holding);
        }

        holdingTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));
        holdingTypes2.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels(newVal));

        transactionAmount.addEventHandler(KeyEvent.KEY_RELEASED, event -> DisplayEquityInfo());
    }

    /**
       Precondition: A UI component like ChoiceBox has triggered its EventListener.
       Responsible for responding to a change in the input, namely ChoiceBoxes to guide
       user input based on the transaction type.
     */
    void setInputLabels(Holding holding) {
        txnFormat = Entry.getType(holdingTypes.getSelectionModel().getSelectedItem(),
                holdingTypes2.getSelectionModel().getSelectedItem());

        if(txnFormat != null) {
            switch (txnFormat) {
                case BUY_EQUITY:
                    transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                    transactionDate.setDisable(false);
                    break;
                case SELL_EQUITY:
                    transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                    transactionDate.setDisable(false);
                    break;
                case TRANSFER_CASH_ACCOUNT:
                    transactionAmount.promptTextProperty().setValue("Transfer amount in value($)");
                    if (holdingTypes.getSelectionModel().getSelectedItem() != null) {
                        if (holdingTypes.getSelectionModel().getSelectedItem().getType().equals(CashAccount.type)) {
                            sharePriceField1.setDisable(true);
                            sharePriceField1.setText("1");
                        } else {
                            sharePriceField1.setDisable(false);
                        }
                    }
                    if (holdingTypes2.getSelectionModel().getSelectedItem() != null) {
                        if (holdingTypes2.getSelectionModel().getSelectedItem().getType().equals(CashAccount.type)) {
                            sharePriceField2.setDisable(true);
                            sharePriceField2.setText("1");
                        } else {
                            sharePriceField2.setDisable(false);
                        }
                    }
                    transactionDate.setDisable(false);
                    break;
                case TRANSFER_EQUITIES:
                    transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                    transactionDate.setDisable(false);
                    break;
                case IMPORT_EQUITY:
                    transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                    transactionDate.setDisable(true);
                    break;
                case IMPORT_CASH_ACCOUNT:
                    transactionAmount.promptTextProperty().setValue("Transfer amount in value($)");
                    if (holdingTypes2.getSelectionModel().getSelectedItem() != null) {
                        if (holdingTypes2.getSelectionModel().getSelectedItem().getType().equals(CashAccount.type)) {
                            sharePriceField2.setDisable(true);
                            sharePriceField2.setText("1");
                        } else {
                            sharePriceField2.setDisable(false);
                        }
                    }
                    break;
                case EXPORT_EQUITY:
                    transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                    transactionDate.setDisable(false);
                    break;
                case EXPORT_CASH_ACCOUNT:
                    transactionAmount.promptTextProperty().setValue("Transfer amount in value($)");
                    if (holdingTypes.getSelectionModel().getSelectedItem() != null) {
                        if (holdingTypes.getSelectionModel().getSelectedItem().getType().equals(CashAccount.type)) {
                            sharePriceField1.setDisable(true);
                            sharePriceField1.setText("1");
                        } else {
                            sharePriceField1.setDisable(false);
                        }
                    }
                    transactionDate.setDisable(false);
                    break;
                default:
                    System.err.println("This case should not be possible or null fields.");
                    break;
            }
        }

        DisplayEquityInfo();
        refreshView();
    }

    /**
     * This pulls the equity value from a CSV or a web service
     * and displays it in the UI.  This is vestigial since the choicebox
     * should display the current share price for equities.
     */
    private void DisplayEquityInfo() {
        if(holdingTypes.getSelectionModel().getSelectedItem() != null) {
            if (Equity.type.equals(holdingTypes.getValue().getType())) {
                MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, Model.class.cast(holdingTypes.getValue()).id);
                if (equity != null) {
                    sharePriceField1.setText(equity.getTickerSymbol() + " - " + equity.getName() + " $" + equity.getSharePrice());
                    if (transactionAmount.getText().length() > 0) {
                        float shares = Float.parseFloat(transactionAmount.getText());
                        float value = shares * equity.getSharePrice();
                        sharePriceField1.setText(String.format("$%.2f", value));
                    }
                }
            }
        }
        if(holdingTypes2.getSelectionModel().getSelectedItem() != null) {
            if(Equity.type.equals(holdingTypes2.getValue().getType())) {
                MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, Model.class.cast(holdingTypes2.getValue()).id);
                if(equity != null) {
                    sharePriceField2.setText(equity.getTickerSymbol() + " - " + equity.getName() + " $" + equity.getSharePrice());
                    if(transactionAmount.getText().length() > 0) {
                        float shares = Float.parseFloat(transactionAmount.getText());
                        float value = shares * equity.getSharePrice();
                        sharePriceField2.setText(String.format("$%.2f", value));
                    }
                }
            }
        }
    }

    public void makeTransaction(ActionEvent actionEvent) {
        if (txnFormat != null) {
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
                    .sourcePrice(Float.parseFloat(sharePriceField1.getText()))
                    .destination(holdingsArrayList.get(holdingTypes2.getSelectionModel().getSelectedIndex()))
                    .destinationPrice(Float.parseFloat(sharePriceField2.getText()))
                    .dateTime(date)
                    .value(Float.parseFloat(transactionAmount.getText()))
                    .build();

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
}

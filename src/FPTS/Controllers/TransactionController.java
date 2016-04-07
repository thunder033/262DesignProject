package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.*;
import FPTS.Search.SelectSearchListener;
import FPTS.Transaction.Entry;
import FPTS.Views.SearchView;
import FPTS.Views.TransactionView;
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
import java.time.ZoneId;
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
public class TransactionController extends Controller implements SelectSearchListener {
    @FXML Text errorMessage;
    @FXML Text transactionInfo;

    @FXML TextField sharePriceField;

    @FXML ChoiceBox<Holding> sourceHoldingField;
    @FXML ChoiceBox<Holding> destHoldingField;

    @FXML NumericField transactionAmount;

    @FXML Button transactionControl;

    @FXML Button searchButton2Transaction;

    @FXML DatePicker transactionDate;

    private final String dateTimeFormat = "yyyy-MM-dd";

    private ArrayList<Holding> holdingsArrayList = new ArrayList<>();
    private ObservableList<Holding> holdingsObservableList = FXCollections.observableList(holdingsArrayList);

    private Entry.EntryFormat txnFormat = null;

    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);

        holdingsObservableList.addListener((ListChangeListener<Holding>) c -> {
            sourceHoldingField.setItems(holdingsObservableList);
            destHoldingField.setItems(holdingsObservableList);
        });

        StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>() {
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

        StringConverter<Holding> holdingConverter = new StringConverter<Holding>() {
            @Override
            public String toString(Holding holding) {
                if(holding == null) {
                    return "External Holding";
                }

                return String.format("%s ($%.2f)", holding.getExportIdentifier(), holding.getValue());
            }
            @Override
            public Holding fromString(String string) {
                String eid = string.split(" \\(")[0];
                _portfolio.getHoldingByExportID(eid);
                return null;
            }
        };

        sourceHoldingField.setConverter(holdingConverter);
        destHoldingField.setConverter(holdingConverter);

        transactionDate.setConverter(dateConverter);
        transactionDate.setPromptText(dateTimeFormat);

        holdingsObservableList.add(0, null);
        holdingsObservableList.addAll(portfolio.getHoldings());

        sourceHoldingField.setItems(holdingsObservableList);
        destHoldingField.setItems(holdingsObservableList);

        sourceHoldingField.getSelectionModel().select(0);
        destHoldingField.getSelectionModel().select(0);

        sourceHoldingField.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels());
        destHoldingField.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> setInputLabels());

        transactionAmount.addEventHandler(KeyEvent.KEY_RELEASED, event -> setInputLabels());
        sharePriceField.addEventHandler(KeyEvent.KEY_RELEASED, event -> setInputLabels());

        LocalDate now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        transactionDate.setValue(now);
    }

    private void setSharePriceField(Holding holding) {
        if(holding == null){
            return;
        }

        sharePriceField.setText("0");
        if(holding.getClass() == Equity.class){
            String tickerSymbol = Equity.class.cast(holding).getTickerSymbol();
            MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, tickerSymbol);
            if (equity != null) {
                sharePriceField.setText(Float.toString(equity.getSharePrice()));
            }
        }
        else {
            sharePriceField.setText("1");
        }
    }

    /**
       Precondition: A UI component like ChoiceBox has triggered its EventListener.
       Responsible for responding to a change in the input, namely ChoiceBoxes to guide
       user input based on the transaction type.
     */
    private void setInputLabels() {
        Holding source = sourceHoldingField.getSelectionModel().getSelectedItem();
        Holding destination = destHoldingField.getSelectionModel().getSelectedItem();

        txnFormat = Entry.getType(source, destination);

        searchButton2Transaction.setDisable(false);

        switch (txnFormat) {
            case BUY_EQUITY:
                transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                sharePriceField.setVisible(true);
                setSharePriceField(destination);
                break;
            case SELL_EQUITY:
                transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                sharePriceField.setVisible(true);
                setSharePriceField(source);
                searchButton2Transaction.setDisable(true);
                break;
            case TRANSFER_CASH_ACCOUNT:
                transactionAmount.promptTextProperty().setValue("Transfer amount in USD($)");
                sharePriceField.setVisible(false);
                sharePriceField.setText("1");
                break;
            case IMPORT_EQUITY:
                transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                sharePriceField.setVisible(true);
                setSharePriceField(destination);
                break;
            case IMPORT_CASH_ACCOUNT:
            case EXPORT_CASH_ACCOUNT:
                transactionAmount.promptTextProperty().setValue("Transfer amount in USD($)");
                sharePriceField.setVisible(false);
                sharePriceField.setText("1");
                break;
            case EXPORT_EQUITY:
                transactionAmount.promptTextProperty().setValue("Transfer amount in shares");
                sharePriceField.setVisible(true);
                setSharePriceField(source);
                break;
            default:
                System.err.println("This case should not be possible or null fields.");
                break;
        }

        DisplayEquityInfo();
        SetTransactionInfo(txnFormat);
        refreshView();
    }

    private void SetTransactionInfo(Entry.EntryFormat format) {
        transactionInfo.setText("");
        if(format != null && transactionAmount.getText().length() > 0){
            float value = Float.valueOf(transactionAmount.getText());
            float sharePrice = Float.valueOf(sharePriceField.getText());
            transactionInfo.setText(String.format("Total amount: $%.2f", value * sharePrice));
        }
    }

    /**
     * This pulls the equity value from a CSV or a web service
     * and displays it in the UI.  This is vestigial since the choicebox
     * should display the current share price for equities.
     */
    private void DisplayEquityInfo() {
        if(sourceHoldingField.getSelectionModel().getSelectedItem() != null) {
            if (Equity.type.equals(sourceHoldingField.getValue().getType())) {
                MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, sourceHoldingField.getValue().id);
                if (equity != null) {
                    sharePriceField.setText(equity.getTickerSymbol() + " - " + equity.getName() + " $" + equity.getSharePrice());
                    if (transactionAmount.getText().length() > 0) {
                        float shares = Float.parseFloat(transactionAmount.getText());
                        float value = shares * equity.getSharePrice();
                        sharePriceField.setText(String.format("%.2f", value));
                    }
                }
            }
        }
        if(destHoldingField.getSelectionModel().getSelectedItem() != null) {
            if(Equity.type.equals(destHoldingField.getValue().getType())) {
                MarketEquity equity = _app.getData().getInstanceById(MarketEquity.class, destHoldingField.getValue().id);
                if(equity != null) {
                    sharePriceField.setText(equity.getTickerSymbol() + " - " + equity.getName() + " $" + equity.getSharePrice());
                    if(transactionAmount.getText().length() > 0) {
                        float shares = Float.parseFloat(transactionAmount.getText());
                        float value = shares * equity.getSharePrice();
                        sharePriceField.setText(String.format("%.2f", value));
                    }
                }
            }
        }
    }

    public void makeTransaction(ActionEvent actionEvent) {
        if (txnFormat != null) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
            Date date = null;
            try {
                date = ISO8601.parse(transactionDate.getValue().toString() + "T" + time.format(cal.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //ensure a new holding is persisted in the portfolio
            Holding destHolding = destHoldingField.getValue();
            if(_portfolio.getHoldingByExportID(destHolding.getExportIdentifier()) == null){
                destHolding.isPersistent = true;
                _portfolio.addHolding(destHolding);
                _portfolio.save();
            }

            Transaction newTxn = new Transaction.Builder()
                    .source(sourceHoldingField.getValue())
                    .destination(destHoldingField.getValue())
                    .sharePrice(Float.parseFloat(sharePriceField.getText()))
                    .value(Float.parseFloat(transactionAmount.getText()))
                    .build();

            try {
                newTxn.execute(date);
                errorMessage.setText("Transaction Successful.");
                _app.CloseStage(TransactionView.class.getSimpleName());
            } catch (InvalidTransactionException|TransactionReExecutionException e) {
                System.err.println(e.getMessage());
                errorMessage.setText(e.getMessage());
            }
        }
    }

    public void handleSearch(ActionEvent actionEvent) {
        SearchView view = new SearchView(_app);
        _app.loadView(view);
        SearchController cntrl = SearchController.class.cast(view.getController());
        cntrl.addListener(this);
    }

    @Override
    public void SearchResultSelected(String result) {
        Holding holding = _portfolio.getHoldingByExportID(result);
        if(holding == null){
            holding = new Equity(_app.getData().getInstanceById(MarketEquity.class, result));
            holding.isPersistent = false;
            holdingsObservableList.add(holding);
        }

        destHoldingField.setValue(holding);
        DisplayEquityInfo();
        refreshView();
    }
}

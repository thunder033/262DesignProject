package FPTS.TransactionHistory;

import FPTS.Models.Equity;
import FPTS.Models.Transaction;

import java.text.SimpleDateFormat;
import java.util.function.Function;

/**
 * Created by Greg on 3/26/2016.
 */
public class Entry {

    enum EntryFormat implements Function<Transaction, String> {
        BUY_EQUITY(txn -> String.format(
                "Bought %.4f shares of %s at a value of $%.2f from %s",
                txn.getValue() / txn.getDestinationPrice(),
                txn.getDestination().getName(),
                txn.getValue(),
                txn.getSource().getName()
        )),
        SELL_EQUITY(txn -> String.format(
                "Sold %.4f shares of %s at value of $%.2f in %s",
                txn.getValue() / txn.getSourcePrice(),
                txn.getSource().getName(),
                txn.getValue(),
                txn.getDestination().getName()
        )),
        TRANSFER_CASH_ACCOUNT(txn -> String.format(
                "Transferred $%.2f from %s to %s",
                txn.getValue(),
                txn.getSource(),
                txn.getValue()
        )),
        IMPORT_EQUITY(txn -> String.format(
                "Imported %.4f shares of %s",
                txn.getValue() / txn.getDestinationPrice(),
                txn.getDestination().getName()
        )),
        IMPORT_CASH_ACCOUNT(txn -> String.format(
                "Imported $%.2f into %s",
                txn.getValue(),
                txn.getDestination().getName()
        )),
        EXPORT_EQUITY(txn -> String.format(
                "Exported %.4f shares of %s",
                txn.getValue() / txn.getSourcePrice(),
                txn.getSource().getName()
        )),
        EXPORT_CASH_ACCOUNT(txn -> String.format(
                "Exported $%.2f from %s",
                txn.getValue(),
                txn.getSource().getName()
        ));

        private final Function<Transaction, String> formatter;

        EntryFormat(Function<Transaction, String> formatter) {
            this.formatter = formatter;
        }

        @Override
        public String apply(Transaction txn){
            return formatter.apply(txn);
        }
    }

    Transaction txn;

    public Entry(Transaction transaction){
        txn = transaction;
    }

    private EntryFormat getType(){
        EntryFormat type = EntryFormat.TRANSFER_CASH_ACCOUNT;
        if(txn.getSource() == null){
            type = txn.getDestination().getClass() == Equity.class ? EntryFormat.IMPORT_EQUITY : EntryFormat.IMPORT_CASH_ACCOUNT;
        }
        else if(txn.getDestination() == null){
            type = txn.getSource().getClass() == Equity.class ? EntryFormat.EXPORT_EQUITY : EntryFormat.EXPORT_CASH_ACCOUNT;
        }
        else if(txn.getSource().getClass() != txn.getDestination().getClass()) {
            type = txn.getSource().getClass() == Equity.class ? EntryFormat.SELL_EQUITY : EntryFormat.BUY_EQUITY;
        }
        return type;
    }

    public String getDescription(){
        return getType().apply(txn);
    }

    public String getDateTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return format.format(txn.getDateTime());
    }
}

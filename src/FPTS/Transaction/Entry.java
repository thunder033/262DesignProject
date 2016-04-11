package FPTS.Transaction;

import FPTS.Models.Equity;
import FPTS.Models.Holding;
import FPTS.Models.Transaction;

import java.text.SimpleDateFormat;
import java.util.function.Function;

/**
 * Created by Greg on 3/26/2016.
 * Provides user friendly formatting for a transaction record
 */
public class Entry {

    public enum EntryFormat implements Function<Transaction, String> {
        BUY_EQUITY(txn -> String.format(
                "Bought %.4f shares of %s at a value of $%.2f from %s",
                txn.getValue(),
                txn.getDestination().getExportIdentifier(),
                txn.getValue() * txn.getSharePrice(),
                txn.getSource().getExportIdentifier()
        )),
        SELL_EQUITY(txn -> String.format(
                "Sold %.4f shares of %s at value of $%.2f in %s",
                txn.getValue(),
                txn.getSource().getExportIdentifier(),
                txn.getValue() * txn.getSharePrice(),
                txn.getDestination().getExportIdentifier()
        )),
        TRANSFER_CASH_ACCOUNT(txn -> String.format(
                "Transferred $%.2f from %s to %s",
                txn.getValue(),
                txn.getSource(),
                txn.getValue()
        )),
        IMPORT_EQUITY(txn -> String.format(
                "Imported %.4f shares of %s",
                txn.getValue() * txn.getSharePrice(),
                txn.getDestination().getExportIdentifier()
        )),
        IMPORT_CASH_ACCOUNT(txn -> String.format(
                "Imported $%.2f into %s",
                txn.getValue(),
                txn.getDestination().getExportIdentifier()
        )),
        EXPORT_EQUITY(txn -> String.format(
                "Sold %.4f shares of %s at value of %.2f to an external account",
                txn.getValue(),
                txn.getSource().getExportIdentifier(),
                txn.getValue() * txn.getSharePrice()
        )),
        EXPORT_CASH_ACCOUNT(txn -> String.format(
                "Exported $%.2f from %s",
                txn.getValue(),
                txn.getSource().getExportIdentifier()
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

    private final Transaction txn;

    public Entry(Transaction transaction){
        txn = transaction;
    }

    private EntryFormat getType(){
        return getType(txn.getSource(), txn.getDestination());
    }

    /**
     * Determines the type of transaction that would be performed
     * @param src source holding or null
     * @param dest destination holding or null
     * @return the type of transaction
     */
    public static EntryFormat getType(Holding src, Holding dest) {
        EntryFormat type = EntryFormat.TRANSFER_CASH_ACCOUNT;
        if(src == null && dest == null) {
            return type;
        }
        if(src == null){
            type = dest.getClass() == Equity.class ? EntryFormat.IMPORT_EQUITY : EntryFormat.IMPORT_CASH_ACCOUNT;
        }
        else if(dest == null){
            type = src.getClass() == Equity.class ? EntryFormat.EXPORT_EQUITY : EntryFormat.EXPORT_CASH_ACCOUNT;
        }
        else if(src.getClass() != dest.getClass()) {
            type = src.getClass() == Equity.class ? EntryFormat.SELL_EQUITY : EntryFormat.BUY_EQUITY;
        }
        return type;
    }

    /**
     * @return Plain-text description of the transaction
     */
    public String getDescription(){
        return getType().apply(txn);
    }

    public String getDateTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return format.format(txn.getDateTime());
    }

    public Transaction getTransaction(){
        return txn;
    }
    
    public String getStatus(){
        if (txn.isRolledBack()){
            return "Disabled";
        } else {
            return "Enabled";
        }
    }
}

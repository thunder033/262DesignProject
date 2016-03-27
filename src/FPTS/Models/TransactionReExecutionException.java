package FPTS.Models;

import java.text.SimpleDateFormat;

/**
 * Created by gjrwcs on 3/17/2016.
 */
public class TransactionReExecutionException extends Exception {
    public TransactionReExecutionException(Transaction transaction){
        super(String.format("Transaction %s was already executed on %s", transaction.id, new SimpleDateFormat().format(transaction.getDateTime())));
    }
}

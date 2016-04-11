package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;

import java.util.Date;

/**
 * Created by Greg on 3/26/2016.
 * Description: Responsible for
 * mapping transactions and converting between
 * instances and values arrays.
 */
public class TransactionBin extends DataBin {

    public TransactionBin(){
        dataClass = Transaction.class;
        fileName = "transactions.csv";
    }

    /**
     * De-serializes a value array into a transaction instance
     * @param values an array of string values read from CSV
     * @return a Transaction instance
     */
    @Override
    public Model fromValueArray(String[] values) {
       return new Transaction.Builder(values[0])
                .source(Holding.deserializeHolding(values[1]))
                .destination(Holding.deserializeHolding(values[2]))
                .sharePrice(Float.parseFloat(values[3]))
                .dateTime(new Date(Long.parseLong(values[4])))
                .value(Float.parseFloat(values[5]))
                .build();
    }

    /**
     * Serializes a transaction into a value array
     * @param instance instance to serialize
     * @return an array of string values representing the transaction
     */
    @Override
    public String[] toValueArray(Model instance) {
        Transaction transaction = Transaction.class.cast(instance);
        return new String[] {
                transaction.id,
                Holding.serializeHolding(transaction.getSource()),
                Holding.serializeHolding(transaction.getDestination()),
                Float.toString(transaction.getSharePrice()),
                Long.toString(transaction.getDateTime().getTime()),
                Float.toString(transaction.getValue())
        };
    }
}

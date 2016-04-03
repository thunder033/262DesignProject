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

    @Override
    public Model fromValueArray(String[] values) {
       return new Transaction.Builder(values[0])
                .source(Holding.deserializeHolding(values[1]))
                .sourcePrice(Float.parseFloat(values[2]))
                .destination(Holding.deserializeHolding(values[3]))
                .destinationPrice(Float.parseFloat(values[4]))
                .dateTime(new Date(Long.parseLong(values[5])))
                .value(Float.parseFloat(values[6]))
                .build();
    }

    @Override
    public String[] toValueArray(Model instance) {
        Transaction transaction = Transaction.class.cast(instance);
        return new String[] {
                transaction.id,
                Holding.serializeHolding(transaction.getSource()),
                Float.toString(transaction.getSourcePrice()),
                Holding.serializeHolding(transaction.getDestination()),
                Float.toString(transaction.getDestinationPrice()),
                Long.toString(transaction.getDateTime().getTime()),
                Float.toString(transaction.getValue())
        };
    }
}

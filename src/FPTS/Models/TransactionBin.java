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
    public Model fromCSV(String[] values) {

        Holding source = Holding.deserializeHolding(values[1]);
        float sourcePrice = Float.parseFloat(values[2]);
        Holding dest = Holding.deserializeHolding(values[3]);
        float destPrice = Float.parseFloat(values[4]);
        Date date = new Date(Long.parseLong(values[5]));
        float value = Float.parseFloat(values[6]);

        return new Transaction(values[0], source, sourcePrice, dest, destPrice, date, value);
    }

    @Override
    public String[] toCSV(Model instance) {
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

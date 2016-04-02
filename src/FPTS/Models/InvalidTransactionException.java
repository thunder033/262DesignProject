package FPTS.Models;

/**
 * Created by gjrwcs on 3/17/2016.
 */
public class InvalidTransactionException extends Exception {
    public InvalidTransactionException(Holding holding, float value){
        super(String.format("Transaction cannot be completed because source holding \"%s\" has a value less than withdraw amount %f", holding.getName(), value));
    }
}

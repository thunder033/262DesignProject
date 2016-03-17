package FPTS.Models;

import FPTS.Core.Model;

import java.util.Date;

/**
 * Created by gjrwcs on 3/17/2016.
 */
public class Transaction extends Model {

    Holding source;
    Holding destination;
    float value;
    Date dateTime;

    public Transaction(Holding source, Holding destination){
        setSource(source);
        setDestination(destination);
    }

    protected Transaction(String id, Holding source, Holding destination, Date date, float value){
        super(id);
        setSource(source);
        setDestination(destination);
        setDateTime(date);
        this.value = value;
    }

    protected void setSource(Holding source){
        setChanged();
        this.source = source;
    }

    public Holding getSource(){
        return source;
    }

    public void setDestination(Holding destination){
        setChanged();
        this.destination = destination;
    }

    public Holding getDestination(){
        return destination;
    }

    public void setValue(float value){
        if(dateTime == null){
            setChanged();
            this.value = value;
        }
    }

    public float getValue(){
        return value;
    }

    private void setDateTime(Date date){
        setChanged();
        this.dateTime = date;
    }

    public Date getDateTime(){
        return dateTime;
    }

    public void execute() throws InvalidTransactionException, TransactionReExecutionException {
        execute(new Date());
    }

    public void execute(Date date) throws InvalidTransactionException, TransactionReExecutionException {
        if(dateTime == null){
            if(source.getValue() >= value){
                if(source != null){
                    source.removeValue(value);
                }

                if(destination != null){
                    destination.addValue(value);
                }

                setDateTime(date);

                saveModels(Equity.class);
                saveModels(CashAccount.class);
                save(true);
            }
            else {
                throw new InvalidTransactionException(source, value);
            }
        } else {
            throw new TransactionReExecutionException(this);
        }
    }
}

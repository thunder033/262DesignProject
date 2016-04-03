package FPTS.Models;

import FPTS.Core.Model;

import java.util.Date;

/**
 * Created by gjrwcs on 3/17/2016.
 * Facilitates transferring liquidity between holdings and
 * provides a record of actions
 */
public class Transaction extends Model {

    private Holding source;
    private Holding destination;
    private float value;
    private float destPrice = 1;
    private float sourcePrice = 1;
    private Date dateTime;

    private boolean rolledBack = false;

    private Transaction(){

    }

    private Transaction(String id){
        super(id);
    }

    public static class Builder {
        Transaction txn;

        public Builder(String id){
            txn = new Transaction(id);
        }

        public Builder(){
            txn = new Transaction();
    }

        public Builder source(Holding source){
            txn.source = source; return this;}

        public Builder destination(Holding destination){
            txn.destination = destination; return this;}

        public Builder sourcePrice(float price){
            txn.sourcePrice = price; return this;}

        public Builder destinationPrice(float price){
            txn.destPrice = price; return this;}

        public Builder value(float value){
            txn.value = value; return this;}

        public Builder dateTime(Date date){
            txn.dateTime = date; return this;}

        public Transaction build(){
            Transaction ret = txn;
            txn = null;
            return ret;
        }
    }

    public Holding getSource(){
        return source;
    }

    public Holding getDestination(){
        return destination;
    }

    public float getSourcePrice(){
        return sourcePrice;
    }

    public float getDestinationPrice(){
        return destPrice;
    }

    public String getStatus(){
        if (true){
            return "";
        } else {
            return "Disabled";
        }
    }
    
    /**
     * Gets the holding the transaction is indexed on
     * @return source holding, or destination if source is null
     */
    public Holding getIndexHolding(){
        return source == null ? getDestination() : getSource();
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
        if(dateTime == null || rolledBack){
            if(source.getValue() >= value * sourcePrice){
                if(source != null){
                    if(source.getClass() == Equity.class && !rolledBack){
                        sourcePrice = Equity.class.cast(source).getCurrentSharePrice();
                    }

                    source.removeValue(value * sourcePrice);
                }

                if(destination != null){
                    if(destination.getClass() == Equity.class && !rolledBack){
                        destPrice = Equity.class.cast(destination).getCurrentSharePrice();
                    }

                    destination.addValue(value * destPrice);
                }

                setDateTime(rolledBack ? dateTime : date);

                rolledBack = false;
                isPersistent = true;

                setChanged();
                saveModels(Equity.class);
                saveModels(CashAccount.class);
                save();
            }
            else {
                throw new InvalidTransactionException(source, value);
            }
        } else {
            throw new TransactionReExecutionException(this);
        }
    }

    public boolean isRolledBack()
    {
        return rolledBack;
    }

    public void rollback() throws InvalidTransactionException {
        if(dateTime != null){
            if(destination.getValue() >= value * destPrice){
                if(source != null){
                    source.addValue(value * sourcePrice);
                }

                if(destination != null){
                    destination.removeValue(value * destPrice);
                }
                rolledBack = true;
                isPersistent = false;

                setChanged();
                saveModels(Equity.class);
                saveModels(CashAccount.class);
                save();
            }
            else {
                throw new InvalidTransactionException(source, value);
            }
        } else {
            throw new UnsupportedOperationException("Transaction " + id + " cannot be rolled back because it has not been executed.");
        }
    }
}
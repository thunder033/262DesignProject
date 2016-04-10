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
    private float sharePrice = 1;
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

        public Builder sharePrice(float price){
            txn.sharePrice = price; return this;}

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

    public float getSharePrice(){
        return sharePrice;
    }
    
    /**
     * Gets the holding the transaction is indexed on
     * @return source holding, or destination if source is null
     */
    public Holding getIndexHolding(){
        return source == null || Model.class.cast(source).isDeleted() ? getDestination() : getSource();
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

            float cashValue = value * sharePrice;

            if(source == null && destination == null || source != null && source.getValue() < cashValue){
                throw new InvalidTransactionException(source, cashValue);
            }

            if(source != null && destination != null && source.getExportIdentifier().equals(destination.getExportIdentifier())){
                throw new InvalidTransactionException(source, destination);
            }

            if(destination != null){
                destination.addValue(cashValue);
            }

            if(source != null) {
                source.removeValue(cashValue);
            }

            setDateTime(rolledBack ? dateTime : date);

            rolledBack = false;
            isPersistent = true;

            setChanged();
            saveModels(Equity.class);
            saveModels(CashAccount.class);
            save();
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

            float cashValue = value * sharePrice;
            if(destination.getValue() < cashValue) {
                throw new InvalidTransactionException(source, value);
            }

            if(source != null){
                source.addValue(cashValue);
            }

            if(destination != null){
                destination.removeValue(cashValue);
            }

            rolledBack = true;
            isPersistent = false;

            setChanged();
            saveModels(Equity.class);
            saveModels(CashAccount.class);
            save();

        } else {
            throw new UnsupportedOperationException("Transaction " + id + " cannot be rolled back because it has not been executed.");
        }
    }
}

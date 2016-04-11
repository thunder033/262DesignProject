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

    //These are private because only the builder can make a transaction
    private Transaction(){}

    /**
     * Rebuild a persisted transaction
     * @param id the ID of the transaction
     */
    private Transaction(String id){
        super(id);
    }

    /**
     * Provides a simple interface to define a transaction
     */
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

        /**
         * Complete the builder
         * @return the created transaction
         */
        public Transaction build(){
            //Destroy the reference to the build transaction so the builder is invalid
            Transaction ret = txn;
            txn = null;
            return ret;
        }
    }

    /**
     * @return the source holding (where funds are coming from)
     */
    public Holding getSource(){
        return source;
    }

    /**
     * @return the destination holding (where funds are going to)
     */
    public Holding getDestination(){
        return destination;
    }

    /**
     * @return get the share price of any equity at the time of the transaction
     */
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

    /**
     * @return the monetary value of funds transferred by the transaction
     */
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

    /**
     * Complete the transaction with a default date (now)
     * @throws InvalidTransactionException
     * @throws TransactionReExecutionException
     */
    public void execute() throws InvalidTransactionException, TransactionReExecutionException {
        execute(new Date());
    }

    /**
     * Complete the transaction, writing all associated models
     * @param date when the transaction was completed
     * @throws InvalidTransactionException
     * @throws TransactionReExecutionException
     */
    public void execute(Date date) throws InvalidTransactionException, TransactionReExecutionException {
        if(dateTime == null || rolledBack){

            float cashValue = value * sharePrice; //calculate the funds to transfer

            //A valid transaction must have at least source or dest, and the source must be able to remain above $0
            if(source == null && destination == null || source != null && source.getValue() < cashValue){
                throw new InvalidTransactionException(source, cashValue);
            }

            //A valid transaction cannot be completed between to reference to the same holding
            if(source != null && destination != null && source.getExportIdentifier().equals(destination.getExportIdentifier())){
                throw new InvalidTransactionException(source, destination);
            }

            if(destination != null){
                destination.addValue(cashValue);
            }

            if(source != null) {
                source.removeValue(cashValue);
            }
            //If the transaction was rolled back, use the existing date
            setDateTime(rolledBack ? dateTime : date);

            rolledBack = false;
            isPersistent = true;
            //Write all changes
            setChanged();
            save();

            saveModels(Equity.class);
            saveModels(CashAccount.class);
            saveModels(Transaction.class);
        } else {
            throw new TransactionReExecutionException(this);
        }
    }

    public boolean isRolledBack()
    {
        return rolledBack;
    }

    /**
     * Reverses all fund transfers performed by this transaction, writing the changes
     * This also make the transaction non-persistent and allows it to be re-executed
     * @throws InvalidTransactionException
     */
    public void rollback() throws InvalidTransactionException {
        if(dateTime != null){
            //The transaction must have been previously executed to roll it back
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
            //write all changes
            setChanged();
            save();

            saveModels(Equity.class);
            saveModels(CashAccount.class);
            saveModels(Transaction.class);

        } else {
            throw new UnsupportedOperationException("Transaction " + id + " cannot be rolled back because it has not been executed.");
        }
    }
}

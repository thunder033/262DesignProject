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

    public Transaction(Holding source, Holding destination){
        setSource(source);
        setDestination(destination);
    }

    protected Transaction(String id, Holding source, float sourcePrice, Holding destination, float destPrice, Date date, float value){
        super(id);
        setSource(source);
        this.sourcePrice = sourcePrice;
        setDestination(destination);
        this.destPrice = destPrice;
        setDateTime(date);
        this.value = value;
    }

    public void setSource(Holding source){
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

    public float getSourcePrice(){
        return sourcePrice;
    }

    public float getDestinationPrice(){
        return destPrice;
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
        if(dateTime == null){
            if(source.getValue() >= value){
                if(source != null){
                    if(source.getClass() == Equity.class){
                        sourcePrice = Equity.class.cast(source).getCurrentSharePrice();
                    }

                    source.removeValue(value);
                }

                if(destination != null){
                    if(destination.getClass() == Equity.class){
                        destPrice = Equity.class.cast(destination).getCurrentSharePrice();
                    }

                    destination.addValue(value);
                }

                setDateTime(date);

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

    public void rollback(){

    }
}
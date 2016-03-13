package FPTS.Models;

import FPTS.Core.Model;

/**
 * Created by gjr8050 on 3/10/2016.
 * Representation of liquid assets held in the portfolio
 */
public class CashAccount extends Model implements Holding {

    protected float _value;
    protected String name;

    public CashAccount(String id){
        super(id);
    }

    public CashAccount(String accName, float initialValue){
        _value = initialValue;
        name = accName;
    }

    /**
     * @return the name of the account
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the value of the account
     */
    @Override
    public float getValue() {
        return _value;
    }

    /**
     * add additional funds to the account
     * @param value the amount of monetary value to add
     */
    @Override
    public void addValue(float value) {
        _value += value;
    }

    /**
     * remove funds from the cash account
     * @param value the amount of monetary value to remove
     */
    @Override
    public void removeValue(float value) {
        _value -= value;
    }

    /**
     * @return descriptor of the holding type
     */
    @Override
    public String getType() {
        return "Cash Account";
    }
}

package FPTS.Models;

import FPTS.Core.Model;

/**
 * @author: Greg
 * Created: 3/10/16
 * Revised: 3/13/16
 * Description: Representation of
 * liquid assets held in the portfolio.
 */
public class CashAccount extends Holding {

    protected float _value;
    protected String name;
    public static final String type = "Cash Account";

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
     * The unique external id of a cash account is it's name
     * @return the cash account name
     */
    @Override
    public String getExportIdentifier() {
        return  name;
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
        setChanged();
    }

    /**
     * remove funds from the cash account
     * @param value the amount of monetary value to remove
     */
    @Override
    public void removeValue(float value) {
        _value -= value;
        setChanged();
    }

    /**
     * @return descriptor of the holding type
     */
    @Override
    public String getType() {
        return type;
    }
}

package FPTS.Models;

import FPTS.Core.Model;

/**
 * Created by gjr8050 on 3/10/2016.
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getValue() {
        return _value;
    }

    @Override
    public void addValue(float value) {
        _value += value;
    }

    @Override
    public void removeValue(float value) {
        _value -= value;
    }

    @Override
    public String getType() {
        return "Cash Account";
    }
}

package FPTS.Models;

import FPTS.Core.Model;

/**
 * Created by gjr8050 on 3/10/2016.
 */
public class Equity extends Model implements Holding {
    MarketEquity _marketEquity;
    float shares;

    public Equity(String id, MarketEquity marketEquity)
    {
        super(id);
        _marketEquity = marketEquity;
    }

    public Equity(MarketEquity marketEquity) {
        _marketEquity = marketEquity;
    }

    public float getShares() {
        return shares;
    }

    @Override
    public String getName() {
        return _marketEquity.getName();
    }

    public String getTickerSybmol() {
        return _marketEquity.getTickerSymbol();
    }

    @Override
    public float getValue() {
        return shares * _marketEquity.getSharePrice();
    }

    @Override
    public void addValue(float value) {
        shares += value / _marketEquity.getSharePrice();
    }

    @Override
    public void removeValue(float value) {
        shares -= value / _marketEquity.getSharePrice();
    }

    public String getType(){
        return "Equity";
    }
}

package FPTS.Models;

import FPTS.Core.Model;

/**
 * @author: Greg
 * Created: 3/9/16
 * Revised: 3/13/16
 * Description: Stores the ticker
 * symbol, name, and share price
 * of given equity on the market.
 */
public class MarketEquity extends Model {
    protected String _name;
    protected float _sharePrice;

    //Equity index;
    protected MarketEquity(String tickerSymbol)
    {
        super(tickerSymbol);
    }

    public String getTickerSymbol() {
        return id;
    }

    public String getName() {
        return _name;
    }

    public float getSharePrice() {
        return _sharePrice;
    }
}

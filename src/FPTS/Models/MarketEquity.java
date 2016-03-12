package FPTS.Models;

import FPTS.Core.Model;

import java.util.ArrayList;

/**
 * Created by Greg on 3/9/2016.
 */
public class MarketEquity extends Model {
    protected String _name;
    protected float _sharePrice;
    private ArrayList<MarketIndex> indices;

    //Equity index;
    protected MarketEquity(String tickerSymbol)
    {
        super(tickerSymbol);
        indices = new ArrayList<>();
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

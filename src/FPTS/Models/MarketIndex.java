package FPTS.Models;

import java.util.ArrayList;

/**
 * Created by gjr8050 on 3/10/2016.
 */
public class MarketIndex extends MarketEquity {

    private ArrayList<MarketEquity> equities;

    protected MarketIndex(String indexName)
    {
        super(indexName);
        id = indexName;
        _name = indexName;
        equities = new ArrayList<>();
    }

    @Override
    public float getSharePrice()
    {
        //TODO: Calculate share price from market equities in index
        return 0;
    }

    public void addEquity(MarketEquity equity) {
        equities.add(equity);
    }

    public boolean contains(MarketEquity equity) {
        return equities.contains(equity);
    }

    public ArrayList<MarketEquity> getEquities()
    {
        return equities;
    }
}

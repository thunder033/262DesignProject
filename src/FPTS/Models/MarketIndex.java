package FPTS.Models;

import java.util.ArrayList;

/**
 * Created by gjr8050 on 3/10/2016.
 */
public class MarketIndex extends MarketEquity {
    protected MarketIndex(String indexName)
    {
        super(indexName);
        id = indexName;
        _name = indexName;
    }

    @Override
    public float getSharePrice()
    {
        //TODO: Calculate share price from market equities in index
        return 0;
    }
}

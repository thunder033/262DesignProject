package FPTS.Models;

import FPTS.Data.DataBin;
import FPTS.Model;

/**
 * Created by Greg on 3/9/2016.
 */
public class MarketEquityBin extends DataBin {

    public MarketEquityBin()
    {
        fileName = "marketEquities.csv";
        dataClass = MarketEquity.class;
    }

    @Override
    public Model fromCSV(String[] values) {

        MarketEquity mEquity = new MarketEquity(values[0]);
        mEquity._name = values[1];
        mEquity._sharePrice = Float.parseFloat(values[2]);

        //convert values[3] to index reference
        return mEquity;
    }
}

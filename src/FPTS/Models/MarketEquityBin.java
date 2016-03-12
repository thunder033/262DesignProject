package FPTS.Models;

import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Core.Model;

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

        for (int i = 3; i < values.length; i++) {
            MarketIndex index = MarketIndex.class.cast(FPTSData.getDataRoot().getInstanceById(MarketEquity.class, values[i]));

            if(index == null) {
                index = new MarketIndex(values[i]);
                addInstance(index);
            }

            index.addEquity(mEquity);
        }

        //convert values[3] to index reference
        return mEquity;
    }
}

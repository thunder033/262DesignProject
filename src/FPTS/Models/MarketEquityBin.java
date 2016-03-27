package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;

/**
 * @author: Greg
 * Created: 3/9/16
 * Revised: 3/13/16
 * Description: Represents a storage
 * space in persistent memory for
 * market equities.
 */
public class MarketEquityBin extends DataBin {

    public MarketEquityBin()
    {
        fileName = "marketEquities.csv";
        dataClass = MarketEquity.class;
    }

    /**
     * Parses a market equity values array into a market equity instance
     * Builds market indexs by adding the created equity to the appropriate index
     * @param values [tickerSymbol, name, sharePrice, index/sector]
     * @return a market equity index
     */
    @Override
    public Model fromCSV(String[] values) {

        MarketEquity mEquity = new MarketEquity(values[0]);
        mEquity._name = values[1];
        mEquity._sharePrice = Float.parseFloat(values[2]);
        mEquity.isPersistent = false;

        for (int i = 3; i < values.length; i++) {
            MarketIndex index = MarketIndex.class.cast(FPTSData.getDataRoot().getInstanceById(MarketEquity.class, values[i]));

            if(index == null) {
                index = new MarketIndex(values[i]);
                addInstance(index);
            }

            index.addEquity(mEquity);
        }

        return mEquity;
    }

    @Override
    public String[] toCSV(Model instance) {
        return new String[0];
    }
}

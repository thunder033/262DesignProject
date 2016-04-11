package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;

/**
 * @author: Greg
 * Created: 3/13/16
 * Revised: 3/13/16
 * Description: Responsible for storing
 * equities and converting between
 * instances and values arrays.
 */
public class EquityBin extends DataBin {

    public EquityBin() {
        fileName = "equities.csv";
        dataClass = Equity.class;
    }

    /**
     * Creates an equity instance from the values array
     * @param values an array of string values read from CSV
     * @return an equity instance
     */
    @Override
    public Model fromValueArray(String[] values) {
        MarketEquity marketEquity = FPTSData.getDataRoot().getInstanceById(MarketEquity.class, values[1]);
        Equity equity = new Equity(values[0], marketEquity);
        equity.shares = Float.parseFloat(values[2]);
        return equity;
    }

    /**
     * Serializes an equity into an internal values array
     * @param instance instance to serialize
     * @return an array of values representing the equity
     */
    @Override
    public String[] toValueArray(Model instance) {
        Equity equity = Equity.class.cast(instance);

        String[] values = new String[3];
        values[0] = equity.id;
        values[1] = equity.getTickerSymbol();
        values[2] = Float.toString(equity.shares);

        return  values;
    }
}

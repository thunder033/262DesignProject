package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;

/**
 * Equity Bin
 * Responsible for storing equities and converting between
 * instances and values arrays.
 */
public class EquityBin extends DataBin {

    public EquityBin() {
        fileName = "equities.csv";
        dataClass = Equity.class;
    }

    @Override
    public Model fromCSV(String[] values) {
        MarketEquity marketEquity = FPTSData.getDataRoot().getInstanceById(MarketEquity.class, values[1]);
        Equity equity = new Equity(values[0], marketEquity);
        equity.shares = Float.parseFloat(values[2]);
        return equity;
    }

    @Override
    public String[] toCSV(Model instance) {
        Equity equity = Equity.class.cast(instance);

        String[] values = new String[3];
        values[0] = equity.id;
        values[1] = equity.getTickerSymbol();
        values[2] = Float.toString(equity.shares);

        return  values;
    }
}

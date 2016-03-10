package FPTS.Models;

import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Model;

/**
 * Created by gjr8050 on 3/10/2016.
 */
public class PortfolioBin extends DataBin {

    public PortfolioBin()
    {
        dataClass = Portfolio.class;
        fileName = "portfolios.csv";
    }

    @Override
    public Model fromCSV(String[] values) {
        Portfolio portfolio = new Portfolio(values[0], values[1]);

        for (int h = 3; h < values.length; h++) {
            String id = values[h].substring(1);
            Class type = values[h].charAt(0) == 'C' ? CashAccount.class : Equity.class;
            Holding holding = Holding.class.cast(FPTSData.getInstanceById(type, id));

            if(holding != null)
            {
                portfolio.addHolding(holding);
            }
        }

        return portfolio;
    }
}

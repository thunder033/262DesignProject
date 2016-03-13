package FPTS.Models;

import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;
import FPTS.Core.Model;

import java.util.ArrayList;

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

        for (int h = 2; h < values.length; h++) {
            String id = values[h].substring(1);
            Class type = values[h].charAt(0) == 'C' ? CashAccount.class : Equity.class;
            Holding holding = Holding.class.cast(FPTSData.getDataRoot().getInstanceById(type, id));

            if(holding != null)
            {
                portfolio.addHolding(holding);
            }
        }

        return portfolio;
    }

    private String getHoldingIdHash(Holding holding) {
        Model model = Model.class.cast(holding);
        return holding instanceof CashAccount ? "C" + model.id : "E" + model.id;
    }

    @Override
    public String[] toCSV(Model instance) {

        Portfolio portfolio = Portfolio.class.cast(instance);
        ArrayList<String> values = new ArrayList<>();
        values.add(portfolio.id);
        values.add(portfolio._passHash);
        portfolio.holdings.stream().map(holding -> values.add(getHoldingIdHash(holding)));

        String[] stringVals = new String[values.size()];
        stringVals = values.toArray(stringVals);
        return stringVals;
    }
}

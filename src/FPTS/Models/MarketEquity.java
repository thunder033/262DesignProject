package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.YFSClient;

import java.io.IOException;

/**
 * @author: Greg
 * Created: 3/9/16
 * Revised: 3/13/16
 * Description: Stores the ticker
 * symbol, name, and share price
 * of given equity on the market.
 */
public class MarketEquity extends Model {
    String _name;
    float _sharePrice;

    //Equity index;
    protected MarketEquity(String tickerSymbol)
    {
        super(tickerSymbol);
        isPersistent = false;
    }

    public String getTickerSymbol() {
        return id;
    }

    public String getName() {
        return _name;
    }

    public float getSharePrice() {
        try {
            return YFSClient.instance().getSharePrice(this);
        }catch (NumberFormatException | IOException e){
            return _sharePrice;
        }
    }
}

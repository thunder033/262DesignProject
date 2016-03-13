package FPTS.Models;

import FPTS.Core.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjr8050 on 3/10/2016.
 * A collection of holdings managed by one user
 */
public class Portfolio extends Model {

    ArrayList<Holding> holdings;
    String _passHash;

    public Portfolio(String username, String passHash)
    {
        super(username);
        holdings = new ArrayList<>();
        _passHash = passHash;
    }

    /**
     * Get all holdings in the portfolio
     * @return a list of holding in the portfolio
     */
    public List<Holding> getHoldings() {
        return holdings;
    }

    /**
     * Get the username associated with the portfolio
     * @return portfolio's associated username
     */
    public String getUsername()
    {
        return id;
    }

    /**
     * Add a new holding to the portfolio
     * @param holding the holding to add
     */
    public void addHolding(Holding holding)
    {
        holdings.add(holding);
    }
}

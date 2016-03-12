package FPTS.Models;

import FPTS.Core.Model;

import java.util.ArrayList;

/**
 * Created by gjr8050 on 3/10/2016.
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

    public String getUsername()
    {
        return id;
    }

    public void addHolding(Holding holding)
    {
        holdings.add(holding);
    }
}

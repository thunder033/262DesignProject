package FPTS.Models;

import FPTS.Core.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: Greg
 * Created: 3/10/16
 * Revised: 3/13/16
 * Description: A collection
 * of holdings managed by one user.
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

    @Override
    public void save() {
        holdings.stream().forEach(holding -> Model.class.cast(holding).save());
        super.save();
    }

    public boolean validateHash(String hash){
        return hash.equals(_passHash);
    }

    /**
     * Get all holdings in the portfolio
     * @return a list of holding in the portfolio
     */
    public List<Holding> getHoldings() {
        return holdings.stream()
                .filter(holding -> !Model.class.cast(holding).isDeleted())
                .collect(Collectors.toList());
    }

    public List<Holding> getHoldings(boolean includeDeleted)
    {
        return includeDeleted ? holdings : getHoldings();
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
     * Add a new holding to the portfolio and save the portfolio
     * @param holding the holding to add
     */
    public void addHolding(Holding holding)
    {
        if(holding != null){
            System.out.println(String.format("Adding %s %s to %s", holding.getClass().getSimpleName(), Model.class.cast(holding).id, id));
            holdings.add(holding);
            setChanged();
        }
    }

    public Holding getHoldingByExportID(String eid){
        Optional<Holding> holdingOptional = getHoldings().stream().
        filter(holding -> holding.getExportIdentifier().equals(eid)).
        findFirst();
        if(holdingOptional.isPresent()){
            return holdingOptional.get();
        }
        return null;
    }

    public void removeHolding(Holding holding)
    {
        if(holding != null){
            holdings.remove(holding);
            setChanged();
        }
    }

    /**
     * create and add a new holding to the portfolio, saving the portfolio if the holding was successfully added
     * @param type a type of holding model to create
     * @param name the name or ticker symbol of cash account or equity
     * @param value the monetary value of the holding
     * @param <T> class that extends model and implements holding
     * @return the holding created or null
     * @throws UnknownMarketEquityException
     */
    public <T extends Model> Holding addHolding(Class<T> type, String name, float value) throws UnknownMarketEquityException {
        Holding holding = null;
        System.out.println(String.format("Add Holding from %s %s and %f", type.getSimpleName(), name, value));
        //maybe this should be broken into 2 methods...hmm
        if(type.equals(Equity.class)){
            MarketEquity equity = findById(MarketEquity.class, name);
            if(equity != null){
                holding = new Equity(equity);
                holding.addValue(value * equity.getSharePrice());
            } else {
               throw new UnknownMarketEquityException(name);
            }
        } else if(type.equals(CashAccount.class)){
            holding = new CashAccount(name, value);
        }

        addHolding(holding);

        return holding;
    }
}

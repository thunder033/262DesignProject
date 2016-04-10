package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Transaction.Entry;
import FPTS.Transaction.Log;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Greg
 * Created: 3/10/16
 * Revised: 3/13/16
 * Description: A collection
 * of holdings managed by one user.
 */
public class Portfolio extends Model {

    Map<String, Holding> holdings;
    String _passHash;

    public Portfolio(String username, String passHash)
    {
        super(username);
        holdings = new HashMap<>();
        _passHash = passHash;
    }

    @Override
    public void save() {
        holdings.values().stream().forEach(Holding::save);
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
        return holdings.values().stream()
                .filter(holding -> !Model.class.cast(holding).isDeleted())
                .collect(Collectors.toList());
    }

    public List<Holding> getHoldings(boolean includeDeleted)
    {
        return includeDeleted ? new ArrayList<>(holdings.values()) : getHoldings();
    }

    public boolean containsHolding(Holding holding){
        return holdings.containsKey(holding.getExportIdentifier());
    }

    public Holding getHolding(String exportID){
        if(holdings.containsKey(exportID)){
            return holdings.get(exportID);
        }
        return null;
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
            System.out.println(String.format("Adding %s %s to %s", holding.getClass().getSimpleName(), holding.id, id));
            holdings.put(holding.getExportIdentifier(), holding);
            setChanged();
        }
    }

    public void removeHolding(Holding holding)
    {
        if(holding != null){
            holdings.remove(holding.getExportIdentifier());
            setChanged();
        }
    }

    @Override
    public void hardDelete() {
        new Log(this).getEntries().stream()
                .map(Entry::getTransaction)
                .forEach(Model::hardDelete);
        getHoldings().stream().forEach(Model::hardDelete);
        findById(WatchList.class, id).hardDelete();
        super.hardDelete();
    }
}

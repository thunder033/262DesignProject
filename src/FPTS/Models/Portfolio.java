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

    /**
     * Validates the provided hash against the that stored in the portfolio
     * @param hash the password hash to check
     * @return boolean indicating if hash is correct
     */
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

    /**
     * Retrieve the collection of holding in the portfolio
     * @param includeDeleted whether or not to include soft-deleted holdings (default false)
     * @return a list of holding
     */
    public List<Holding> getHoldings(boolean includeDeleted)
    {
        return includeDeleted ? new ArrayList<>(holdings.values()) : getHoldings();
    }

    /**
     * Determines if the holding is contained in the portfolio based on export ID
     * @param holding the holding to check
     * @return if the portfolio contains the holding
     */
    public boolean containsHolding(Holding holding){
        return holdings.containsKey(holding.getExportIdentifier());
    }

    /**
     * Retrieve a holding from the portfolio based on its Export ID
     * @param exportID the holding Export ID (Ticker symbol or CA Name)
     * @return the holding if it exists, or null
     */
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

    /**
     * Removes the holding from the portfolio, based on export ID
     * @param holding the holding to remove
     */
    public void removeHolding(Holding holding)
    {
        if(holding != null){
            holdings.remove(holding.getExportIdentifier());
            setChanged();
        }
    }

    /**
     * Hard deletes the holding and all of its associated entities. NOT Undoable.
     */
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

package FPTS.Search;

import FPTS.Data.FPTSData;
import FPTS.Models.MarketEquity;
import FPTS.Models.MarketIndex;

import java.util.ArrayList;

/**
 * @author: Eric
 * Created: 3/12/16
 * Revised: 3/14/16
 * Description: Main Search Query Implementation
 */
public class SearchQuery{
    private SearchMethod searchMethod;
    
    public SearchQuery(SearchMethod searchMethod){
        this.searchMethod = searchMethod;
    }
    
    /**
     * Performs the search
     */
    public ArrayList<MarketEquity> executeStrategy(ArrayList<MarketEquity> marketEquities, String searchTerm, SearchParameter.searchParameter f) {
        
        
        ArrayList<MarketEquity> results = new ArrayList<>();
        
        //Iterate through Equities for each parameter to find if the searchTerm (not case sensitive) matches the Fields
        if (f == SearchParameter.searchParameter.id){
            marketEquities.stream().filter((i) -> (searchMethod.compare(searchTerm.toUpperCase(), i.getTickerSymbol().toUpperCase()))).forEach((i) -> {
                results.add(i);
            });
        }   
        if (f == SearchParameter.searchParameter.name){
            marketEquities.stream().filter((i) -> (searchMethod.compare(searchTerm.toUpperCase(), i.getName().toUpperCase()))).forEach((i) -> {
                results.add(i);
            });
        }
        if (f == SearchParameter.searchParameter.marketAverage){
            marketEquities.stream().filter((i) -> (searchMethod.compare(searchTerm.toUpperCase(), "" +(i.getName())))).forEach((i) -> {
                if(i.getClass() == MarketIndex.class) {
                    results.add(i);
                }
            });
        }

        return results;

    }

}
package FPTS.Search;

import java.util.ArrayList;
import FPTS.Models.MarketEquity;
import FPTS.Data.FPTSData;

public class SearchQuery{
    private SearchMethod searchMethod;

    public SearchQuery(SearchMethod searchMethod){

        this.searchMethod = searchMethod;

    }

    public ArrayList<MarketEquity> executeStrategy(String searchTerm) {
        
        ArrayList<MarketEquity> marketEquities = FPTSData.getDataRoot().getInstances(MarketEquity.class);
        ArrayList<MarketEquity> results = new ArrayList<>();

        marketEquities.stream().filter((i) -> (searchMethod.compare(searchTerm, i.getName()))).forEach((i) -> {
            results.add(i);
        });

        return results;

    }

}
package FPTS.Search;

import java.util.ArrayList;
import FPTS.Models.MarketEquity;
import FPTS.Data.FPTSData;

public class SearchQuery{

    
    private SearchMethod searchMethod;

    public SearchQuery(SearchMethod searchMethod){

        this.searchMethod = searchMethod;

    }

    public ArrayList<MarketEquity> executeStrategy(String searchTerm, SearchParameter.searchParameter f) {
        
        ArrayList<MarketEquity> marketEquities = FPTSData.getDataRoot().getInstances(MarketEquity.class);
        ArrayList<MarketEquity> results = new ArrayList<>();
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
        if (f == SearchParameter.searchParameter.index){
            //marketEquities.stream().filter((i) -> (searchMethod.compare(searchTerm.toUpperCase(), i..toUpperCase()))).forEach((i) -> {
            //    results.add(i);
            //});
        }
        if (f == SearchParameter.searchParameter.sector){
            //marketEquities.stream().filter((i) -> (searchMethod.compare(searchTerm.toUpperCase(), i..getName().toUpperCase()))).forEach((i) -> {
            //    results.add(i);
            //});
        }

        return results;

    }

}
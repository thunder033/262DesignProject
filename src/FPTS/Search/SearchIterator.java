package FPTS.Search;

import FPTS.Models.MarketEquity;
import java.util.ArrayList;
import static java.util.stream.Collectors.toList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created 4/7/15
 * @author Eric
 */
public class SearchIterator {
    int nPages = 10;
    private int pageNumber = 0;
    private ObservableList<MarketEquity> entries;
    
    
    public SearchIterator(ObservableList<MarketEquity> e){
        entries = e;
        
        //Sorts results based on ticker symbol
        entries = FXCollections.observableArrayList(entries.stream()
                .sorted((MarketEquity a, MarketEquity b) -> a.getTickerSymbol().compareTo(b.getTickerSymbol()))
                .collect(toList()));
    }
    
    //Method for getting total number of pages based on the number of 
    //entries and the results per page
    public int getPagesTotal(){
        return (int)Math.ceil(((double)entries.size())/((double)nPages));
    }
    
    public int getPageNumber(){
        return pageNumber;
    }
    
    //Returns the next page of results, given a page number and the 
    //complete set of equities returned by the search
    public ObservableList<MarketEquity> getNextResultSet(){
        pageNumber++;
        
        ObservableList<MarketEquity> ret = FXCollections.observableArrayList(new ArrayList<MarketEquity>());
        
        //If there are not enough entries to present the specified 
        //number of results per page, then show what is left
        if(entries.size() < getPageNumber()*nPages){
            for(int i = getPageNumber()*nPages; i < entries.size();){
                ret.add(entries.get(i));
            }
            return ret;
        }
        
        //Get the next page of results, restricted to what nPages is set to
        for(int i = 0; i< nPages; i++){
            ret.add(entries.get(i + getPageNumber()*nPages));
        }
        
        return ret;
        }
    
    //Return the current results, useful for initialization
    public ObservableList<MarketEquity> getCurrentResults(){
        if(entries.size() < nPages){
            return entries;
        }
        
        ObservableList<MarketEquity> ret = FXCollections.observableArrayList(new ArrayList<MarketEquity>());
        
        for(int i = 0; i< nPages; i++){
            ret.add(entries.get(i + getPageNumber()*nPages));
        }
        return ret;
    }
    //Returns the previous page of results, given a page number and the 
    //complete set of equities returned by the search
    public ObservableList<MarketEquity> getPrevResultSet(){
        pageNumber--;
        if (pageNumber < 0){
            pageNumber = 0;
        }
        
        ObservableList<MarketEquity> ret = FXCollections.observableArrayList(new ArrayList<MarketEquity>());
        //Iterate to return the correct number of entries for the previous page.
        for(int i = 0; i< nPages; i++){
            ret.add(entries.get(i + getPageNumber()*nPages));
        }
        return ret;
        }
}

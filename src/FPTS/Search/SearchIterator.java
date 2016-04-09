/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FPTS.Search;

import FPTS.Models.MarketEquity;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Eric
 */
public class SearchIterator {
    int nPages = 3;
    private int pageNumber = 0;
    private ObservableList<MarketEquity> entries;
    
    public SearchIterator(ObservableList<MarketEquity> e){
        entries = e;
    }
    
    public int getPagesTotal(){
        return (int)Math.ceil(((double)entries.size())/((double)nPages));
    }
    
    public int getPageNumber(){
        return pageNumber;
    }
    
    public ObservableList<MarketEquity> getNextResultSet(){
        pageNumber++;
        ObservableList<MarketEquity> ret = FXCollections.observableArrayList(new ArrayList<MarketEquity>());
        if(entries.size() < getPageNumber()*nPages){
            for(int i = getPageNumber()*nPages; i < entries.size();){
                ret.add(entries.get(i));
            }
            return ret;
        }
        for(int i = 0; i< nPages; i++){
            ret.add(entries.get(i + getPageNumber()*nPages));
        }
        return ret;
        }
    
    public ObservableList<MarketEquity> getCurrentResults(){
        if(entries.size() < nPages){
            return entries;
        }
        ObservableList<MarketEquity> ret = FXCollections.observableArrayList(new ArrayList<MarketEquity>());
        for(int i = 0; i< nPages; i++){
            ret.add(entries.get(i + getPageNumber()*nPages));
        }
        return ret;
        //return (ObservableList<MarketEquity>) entries.subList(getPageNumber()*nPages, getPageNumber()*nPages + 19);
    }
     public ObservableList<MarketEquity> getPrevResultSet(){
        pageNumber--;
        if (pageNumber < 0){
            pageNumber = 0;
        }
        ObservableList<MarketEquity> ret = FXCollections.observableArrayList(new ArrayList<MarketEquity>());
        for(int i = 0; i< nPages; i++){
            ret.add(entries.get(i + getPageNumber()*nPages));
        }
        return ret;
        }
}

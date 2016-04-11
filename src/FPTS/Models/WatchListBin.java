package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Greg on 4/5/2016.
 * Indexes WatchList instances and serializes/deserializes them
 */
public class WatchListBin extends DataBin {

    public WatchListBin(){
        dataClass = WatchList.class;
        fileName = "watchLists.csv";
    }

    /**
     * Creates an instance of a WatchList from a valid value array
     * @param values an array of string values read from CSV
     * @return a WatchList instance
     */
    @Override
    public Model fromValueArray(String[] values) {

        int i = 0;
        WatchList watchList = new WatchList(values[i++]);
        watchList.setUpdateInterval(Integer.valueOf(values[i++]) / 1000);

        while(i < values.length) {
            MarketEquity equity = FPTSData.getDataRoot().getInstanceById(MarketEquity.class, values[i++]);
            WatchedEquity watchedEquity = new WatchedEquity(equity);
            watchedEquity.setLowerTrigger(Float.parseFloat(values[i++]));
            watchedEquity.setUpperTrigger(Float.parseFloat(values[i++]));
            watchList.loadWatchedEquity(watchedEquity);
        }

        return watchList;
    }

    /**
     * Generates a value array from a WatchList instance
     * @param instance instance to serialize
     * @return an array of values that represent the WatchList
     */
    @Override
    public String[] toValueArray(Model instance) {

        WatchList watchList = WatchList.class.cast(instance);
        ArrayList<String> values = new ArrayList<>();

        values.add(watchList.id);
        values.add(Integer.toString(watchList.getUpdateInterval() * 1000));

        watchList.getWatchedEquities().stream()
                .map(WatchedEquity::serialize)
                .map(Arrays::asList)
                .forEach(values::addAll);

        String[] stringVals = new String[values.size()];
        stringVals = values.toArray(stringVals);
        return stringVals;
    }
}

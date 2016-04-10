package FPTS.PortfolioImporter;

import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.CashAccount;
import FPTS.Models.Equity;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;
import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by traub_000 on 4/1/2016.
 *
 * Description: Contains methods for retrieving holdings that have been imported
 * with a CSV importer, finding conflicts with current holdings, and saving new
 * holdings to a portfolio
 */
public class HoldingImportHandler {

    private static ObservableList<Holding> holdings = FXCollections.observableArrayList();
    private static List<Holding> CSVHoldings;
    private static boolean[] hasDuplCA;
    private static ArrayList<Holding> equityMergeList;
    private static Portfolio _portfolio;
    private static ArrayList<Holding> CAMergeList = new ArrayList<>();
    private static ArrayList<Holding> CAReplaceList = new ArrayList<>();
    private static ArrayList<Holding> CAIgnoreList = new ArrayList<>();

    public static ObservableList<ImportItem> getNewHoldings(Path path, Portfolio portfolio){

        _portfolio = portfolio;
        Importer importer = new Importer(path);
        importer.setStrategy(new CSVImporter());
        if(CSVHoldings != null){
            holdings.removeAll(CSVHoldings);
        }
        CSVHoldings = importer.importData().portfolio.getHoldings();
        holdings.addAll(FXCollections.observableArrayList(CSVHoldings));
        System.out.println(holdings.size());
        ArrayList<ImportItem> items = CSVHoldings.stream()
                .map(holding -> new ImportItem(holding, _portfolio))
                .collect(Collectors.toCollection(ArrayList::new));
        return FXCollections.observableArrayList(items);
    }

    public static ObservableList getNewHoldings(String newCAName, String newCAValue) throws IllegalArgumentException {

        if(newCAName.equals("")){
            throw new IllegalArgumentException("New Cash Account must have a name");
        }

        float value = Float.parseFloat(newCAValue);

        CashAccount newCA = new CashAccount(newCAName,value);
        holdings.add(newCA);
        System.out.println(holdings.size());

        return holdings;
    }

    public static void determineConflicts(Portfolio _portfolio){
        equityMergeList = new ArrayList<>();
        HoldingImportHandler._portfolio = _portfolio;
        hasDuplCA = new boolean[holdings.size()];

        for(int i = 0; i < holdings.size(); i++) {
            if(_portfolio.containsHolding(holdings.get(i))){
                if(holdings.get(i).getType().equals(Equity.type)){
                    equityMergeList.add(holdings.get(i));
                }else{
                    hasDuplCA[i] = true;
                }
            }
        }
    }

    public static boolean[] getDuplCAIndices() {
        return hasDuplCA;
    }

    private static void addHolding(Holding holding){
        _portfolio.addHolding(holding);
    }


    public static void importHoldings(List<ImportItem> items) throws InvalidChoiceException {
        // Method to separate out merge, replace, ignore
        separateCases(items);
        mergeHoldings();
        replaceHoldings();
        holdings.stream().forEach(HoldingImportHandler::addHolding);
        _portfolio.save();
        holdings.clear();
    }

    private static void replaceHoldings() {
        for(Holding hold : CAReplaceList){
            System.out.println("CA Replace " + hold.getExportIdentifier());
            Holding oldHold = _portfolio.getHolding(hold.getExportIdentifier());
            _portfolio.removeHolding(oldHold);
            oldHold.delete();
            _portfolio.addHolding(hold);
        }
    }

    private static void mergeHoldings() {
        for(Holding hold : CAMergeList){
            System.out.println("CA Merge " + hold.getExportIdentifier());
            _portfolio.getHolding(hold.getExportIdentifier()).addValue(hold.getValue());
        }
    }

    private static void separateCases(List<ImportItem> items) throws InvalidChoiceException{
        CAMergeList.clear();
        CAReplaceList.clear();
        CAIgnoreList.clear();

        items.stream().forEach(item -> {
            switch (item.getAction()){
                case IGNORE:
                    CAIgnoreList.add(item.getHolding());
                    break;
                case MERGE:
                    CAMergeList.add(item.getHolding());
                    break;
                case REPLACE:
                    CAReplaceList.add(item.getHolding());
            }
        });

        holdings.removeAll(CAIgnoreList);
        holdings.removeAll(CAMergeList);
        holdings.removeAll(equityMergeList);
    }

}

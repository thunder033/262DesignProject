package FPTS.PortfolioImporter;

import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.CashAccount;
import FPTS.Models.Equity;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by traub_000 on 4/1/2016.
 *
 * Description: Contains methods for retrieving holdings that have been imported
 * with a CSV importer, finding conflicts with current holdings, and saving new
 * holdings to a portfolio
 */
public class HoldingImportHandler {

    private static ObservableList<Holding> holdings = FXCollections.observableArrayList();
    private static boolean[] hasDuplCA;
    private static ArrayList<Holding> equityMergeList;
    private static Portfolio _portfolio;
    private static FPTSApp _app;
    private static ArrayList<Holding> CAMergeList = new ArrayList<>();
    private static ArrayList<Holding> CAReplaceList = new ArrayList<>();
    private static ArrayList<Holding> CAIgnoreList = new ArrayList<>();

    public static ObservableList getNewHoldings(Path path){

        Importer importer = new Importer(path);
        importer.setStrategy(new CSVImporter());
        holdings.addAll(FXCollections.observableArrayList(importer.importData().portfolio.getHoldings()));
        System.out.println(holdings.size());
        return holdings;

    }

    public static ObservableList getNewHoldings(String newCAName, String newCAValue) throws IOException{

        if(newCAName.equals("")){
            throw new IOException();
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
        Map<String, String> OldHolding = new HashMap<>();
        for(Holding hold: _portfolio.getHoldings()){
            OldHolding.put(hold.getName(),hold.getType());
        }

        for(int i = 0; i < holdings.size(); i++) {
            String newName = holdings.get(i).getName();
            if(OldHolding.containsKey(newName) && OldHolding.get(newName).equals(holdings.get(i).getType())){
                if(holdings.get(i).getType().equals(Equity.type)){
                    equityMergeList.add(holdings.get(i));
                }else{
                    hasDuplCA[i] = true;
                }
            }
        }
    }

    public static ObservableList getDuplOpts() {
        String[] items = {"Merge", "Replace", "Ignore"};
        return FXCollections.observableArrayList(items);
    }

    public static boolean[] getDuplCAIndices() {
        return hasDuplCA;
    }

    private static void addHolding(Holding holding){
        _portfolio.addHolding(holding);
        _app.getData().addInstance(Model.class.cast(holding));
    }


    public static void importHoldings(FPTSApp _app, GridPane boxes){
        HoldingImportHandler._app = _app;
        // Method to separate out merge, replace, ignore
        try{
            separateCases(boxes);
            mergeHoldings();
            replaceHoldings();
            holdings.stream().forEach(HoldingImportHandler::addHolding);
            _portfolio.save();
            holdings.clear();
        } catch (InvalidChoiceException e){
            e.printStackTrace();
        }

    }

    private static void replaceHoldings() {
        for(Holding hold : CAReplaceList){
            // Uncomment when merged with delete functionality
            Holding oldHold = ( (Holding)_portfolio.getHoldings().stream().filter(holding ->
                    // Uncomment this and delete the lines below when delete function is merged
                    holding.getName().equals(hold.getName())).toArray()[0]);
            Model instance = Model.class.cast(oldHold);
            _portfolio.removeHolding(oldHold);
            instance.delete();
        }
    }

    private static void mergeHoldings() {
        for(Holding hold : CAMergeList){
            ( (Holding) _portfolio.getHoldings().stream().filter(holding ->
                    holding.getName().equals(hold.getName())).toArray()[0] ).addValue(hold.getValue());
        }

    }

    private static void separateCases(GridPane boxes) throws InvalidChoiceException{
        Object choice;
        ComboBox CBox;
        CAMergeList.clear();
        CAReplaceList.clear();
        CAIgnoreList.clear();
        for(int i = 0; i < boxes.getChildren().size() - 1; i++) {
            if (hasDuplCA[i]) {
                CBox = ((ComboBox) boxes.getChildren().get(i + 1));
                choice = CBox.getSelectionModel().getSelectedItem();
                if (choice == null) {
                    throw new InvalidChoiceException(holdings.get(i).getName());
                }else if(choice.toString().equals("Merge")){
                    CAMergeList.add(holdings.get(i));
                } else if (choice.toString().equals("Replace")) {
                    CAReplaceList.add(holdings.get(i));
                } else if (choice.toString().equals("Ignore")) {
                    CAIgnoreList.add(holdings.get(i));
                } else {
                    throw new InvalidChoiceException(holdings.get(i).getName());
                }

            }
        }
        holdings.removeAll(CAIgnoreList);
        holdings.removeAll(CAMergeList);
        holdings.removeAll(equityMergeList);
    }

}

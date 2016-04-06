package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Models.MarketEquity;
import FPTS.Models.MarketIndex;
import FPTS.Search.*;
import FPTS.Data.FPTSData;

import FPTS.Views.AddHoldingView;
import FPTS.Views.SearchView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Observable;

//
//@author: Eric
//Created: 3/14/16
//Revised: 3/14/16
//Description: Contains behavior and elements for UI.
//
public class SearchController extends Controller {

    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField marketAverage;
    
    @FXML
    RadioButton beginsWith1;        
    @FXML
    RadioButton contains1;        
    @FXML
    RadioButton exactlyMatches1;
    @FXML
    RadioButton beginsWith2;        
    @FXML
    RadioButton contains2;        
    @FXML
    RadioButton exactlyMatches2;
    @FXML
    RadioButton beginsWith3;        
    @FXML
    RadioButton contains3;        
    @FXML
    RadioButton exactlyMatches3;
    
    private SearchQuery _search1;
    private SearchQuery _search2;
    private SearchQuery _search3;

    @FXML private TableView<MarketEquity> searchResultsPane;
    
    private ArrayList<SelectSearchListener> selectSearchListeners = new ArrayList<>();
        
    public void addListener(SelectSearchListener toAdd) {
        selectSearchListeners.add(toAdd);
    }
    
    public SearchController() {
        _search1 = new SearchQuery(new BeginsWith());
        _search2 = new SearchQuery(new BeginsWith());
        _search3 = new SearchQuery(new BeginsWith());
    }
    
    @Override
    public void refreshView()
    {
        contains1.setSelected(true);
        contains2.setSelected(true);
        contains3.setSelected(true);
    }
    
    @FXML
    protected void handleSelectAction(ActionEvent actionEvent) {

        TableView.TableViewSelectionModel selectionModel = searchResultsPane.getSelectionModel();
        List selectedCells = selectionModel.getSelectedCells();
        TablePosition tablePosition = (TablePosition) selectedCells.get(0);
        int row = tablePosition.getRow();
        Object item = searchResultsPane.getItems().get(row);
        TableColumn col = (TableColumn)searchResultsPane.getColumns().get(0);
        String data = (String) col.getCellObservableValue(item).getValue();

        for (SelectSearchListener hl : selectSearchListeners)
            hl.SearchResultSelected(data);

        _app.CloseStage(SearchView.class.getSimpleName());
    }
    
    @FXML
    protected void handleSearchAction(ActionEvent event) {
        if(beginsWith1.isSelected())
            _search1 = new SearchQuery(new BeginsWith());
        if(contains1.isSelected())
            _search1 = new SearchQuery(new Contains());
        if(exactlyMatches1.isSelected())
            _search1 = new SearchQuery(new ExactlyMatches());
        if(beginsWith2.isSelected())
            _search2 = new SearchQuery(new BeginsWith());
        if(contains2.isSelected())
            _search2 = new SearchQuery(new Contains());
        if(exactlyMatches2.isSelected())
            _search2 = new SearchQuery(new ExactlyMatches());
        if(beginsWith3.isSelected())
            _search3 = new SearchQuery(new BeginsWith());
        if(contains3.isSelected())
            _search3 = new SearchQuery(new Contains());
        if(exactlyMatches3.isSelected())
            _search3 = new SearchQuery(new ExactlyMatches());
        ArrayList<MarketEquity> marketEquities = FPTSData.getDataRoot().getInstances(MarketEquity.class);
        if(!"".equals(marketAverage.getText())){
            ArrayList<MarketEquity> marketIndecies = _search3.executeStrategy(marketEquities, marketAverage.getText(), SearchParameter.searchParameter.marketAverage);
            for (int i = 0; i < marketIndecies.size();i++){
                marketEquities.addAll(MarketIndex.class.cast(marketIndecies.get(i)).getEquities());
            }
            //marketEquities.addAll(marketIndecies);
        }
        
        marketEquities = _search1.executeStrategy(marketEquities, id.getText(),SearchParameter.searchParameter.id);
        marketEquities = _search2.executeStrategy(marketEquities, name.getText(), SearchParameter.searchParameter.name);

        ObservableList<MarketEquity> observableResults = FXCollections.observableArrayList(marketEquities);

        searchResultsPane.setItems(observableResults);
    }
    
}

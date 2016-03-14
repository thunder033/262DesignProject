/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Models.Holding;
import FPTS.Models.MarketEquity;
import FPTS.PortfolioImporter.Exporter;
import FPTS.Search.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;

public class SearchController extends Controller {
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField index;
    @FXML private TextField sector;
    
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
    @FXML
    RadioButton beginsWith4;        
    @FXML
    RadioButton contains4;        
    @FXML
    RadioButton exactlyMatches4;
    
    SearchQuery _search1;
    SearchQuery _search2;
    SearchQuery _search3;
    SearchQuery _search4;
    
    @FXML private TableView searchResultsPane;
    
    public SearchController() {
        _search1 = new SearchQuery(new BeginsWith());
        _search2 = new SearchQuery(new BeginsWith());
        _search3 = new SearchQuery(new BeginsWith());
        _search4 = new SearchQuery(new BeginsWith());
    }
    
    @Override
    public void refreshView()
    {
        contains1.setSelected(true);
        contains2.setSelected(true);
        contains3.setSelected(true);
        contains4.setSelected(true);
    }
    
    @FXML
    protected void handleSelectAction() {
        
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
        if(beginsWith4.isSelected())
            _search4 = new SearchQuery(new BeginsWith());
        if(contains4.isSelected())
            _search4 = new SearchQuery(new Contains());
        if(exactlyMatches4.isSelected())
            _search4 = new SearchQuery(new ExactlyMatches());
        
        
        
        ArrayList<MarketEquity> results1 = _search1.executeStrategy(id.getText(),SearchParameter.searchParameter.id);
        ArrayList<MarketEquity> results2 = _search2.executeStrategy(name.getText(), SearchParameter.searchParameter.name);
        ArrayList<MarketEquity> results3 = _search3.executeStrategy(index.getText(), SearchParameter.searchParameter.index);
        ArrayList<MarketEquity> results4 = _search4.executeStrategy(sector.getText(), SearchParameter.searchParameter.sector);
        ArrayList<MarketEquity> results = new ArrayList<>();
        if(!id.getText().equals(""))
            results.addAll(results1);
        if(!name.getText().equals(""))
            results.addAll(results2);
        if(!index.getText().equals(""))
            results.addAll(results3);
        if(!sector.getText().equals(""))
            results.addAll(results4);
        ObservableList<MarketEquity> observableResults = FXCollections.observableArrayList(results);
        searchResultsPane.setItems(observableResults);
    }
    
}

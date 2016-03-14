/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.PortfolioImporter.Exporter;
import FPTS.Search.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SearchController extends Controller {
    SearchQuery _search;
    public SearchController() {
        _search = new SearchQuery(new Contains());
        
    }
    
    @FXML
    protected void handleSearchAction(ActionEvent event) {
        _search.executeStrategy("test");
        
    }
    
}

package FPTS.PortfolioImporter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by traub_000 on 3/30/2016.
 *
 * Description: A class that was supposed to return a ComboBox
 * to the Table in the UI but it didn't work
 */
public class CustomComboBoxFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        String[] choices = {"Merge", "Replace", "Ignore"};
        ObservableList items = FXCollections.observableArrayList(choices);
        ComboBox comboBox = new ComboBox(items);
        return new ComboBoxTableCell<S, T>();
    }
}

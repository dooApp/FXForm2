package com.dooapp.fxform2.fxreport;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.property.TableViewProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 11/03/2020
 * Time: 11:23
 */
public class LoopTableViewFactory implements Callback<Void, FXFormNode> {

    @Override
    public FXFormNode call(Void aVoid) {
        final BorderPane borderPane = new BorderPane();
        final TableView tableView = new TableView();
        final Button addButton = new Button("+");

        return new FXFormNodeWrapper(borderPane, new TableViewProperty(tableView)) {
            @Override
            public void init(Element element, AbstractFXForm fxForm) {
                borderPane.setCenter(tableView);
                borderPane.setBottom(addButton);
                tableView.setFixedCellSize(35);
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(1)));
                tableView.minHeightProperty().bind(tableView.prefHeightProperty());
                tableView.maxHeightProperty().bind(tableView.prefHeightProperty());
                addButton.setOnAction(event -> tableView.getItems().add(
                        new SimpleMapProperty<>(FXCollections.observableHashMap())));
                tableView.setEditable(true);

                ListElementWrapper listElementWrapper = (ListElementWrapper) element;
                List<String> fields = listElementWrapper.getFields();
                for (String field : fields) {
                    TableColumn col = new TableColumn(field);
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                        @Override
                        public ObservableValue call(TableColumn.CellDataFeatures param) {
                            return ((MapProperty) param.getValue()).valueAt(field);
                        }
                    });
                    col.setCellFactory(TextFieldTableCell.forTableColumn());
                    col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent event) {
                            ((MapProperty) event.getRowValue()).put(field, event.getNewValue());
                        }
                    });
                    col.setEditable(true);
                    tableView.getColumns().add(col);
                }
            }
        };
    }

}
package com.dooapp.fxform.view.factory.impl;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.util.function.Predicate;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 25/02/2016
 * Time: 11:10
 */
public class PaginatedTableView extends Pagination {

    private final ListProperty items = new SimpleListProperty<>();

    private final TableView tableView;

    private IntegerProperty itemsByPage = new SimpleIntegerProperty(10);

    public TableView getTableView() {
        return tableView;
    }

    public PaginatedTableView(TableView tableView) {
        this.tableView = tableView;
        pageCountProperty().bind(Bindings.size(items).divide(itemsByPage));
        FilteredList filteredList = items.filtered(o -> true);
        tableView.setItems(filteredList);
        setPageFactory(page -> {
            filteredList.setPredicate(new Predicate() {
                @Override
                public boolean test(Object o) {
                    return items.indexOf(o) >= page * getItemsByPage() && items.indexOf(o) < (page + 1) * getItemsByPage();
                }
            });
            return tableView;
        });
    }

    public Object getItems() {
        return items.get();
    }

    public ListProperty itemsProperty() {
        return items;
    }

    public void setItems(Object items) {
        this.items.set(items);
    }

    public int getItemsByPage() {
        return itemsByPage.get();
    }

    public IntegerProperty itemsByPageProperty() {
        return itemsByPage;
    }

    public void setItemsByPage(int itemsByPage) {
        this.itemsByPage.set(itemsByPage);
    }

}

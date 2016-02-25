package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.reflection.ReflectionUtils;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 25/02/2016
 * Time: 09:45
 */
public class PaginatedTableViewFactory implements Callback<Void, FXFormNode> {


    @Override
    public FXFormNode call(Void aVoid) {

        final PaginatedTableView paginatedTableView = new PaginatedTableView(new FXFormTableView());

        return new FXFormNodeWrapper(paginatedTableView, paginatedTableView.itemsProperty()) {
            @Override
            public void init(Element element) {
                Class wrappedType = element.getWrappedType();
                List<Field> fields = ReflectionUtils.listFields(wrappedType);
                for (Field field : fields) {
                    TableColumn col = new TableColumn(field.getName());
                    col.setCellValueFactory(new PropertyValueFactory(field.getName()));
                    paginatedTableView.getTableView().getColumns().add(col);
                }
            }
        };

    }

}

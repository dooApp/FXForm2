package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.ListProperty;
import javafx.util.Callback;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 17/07/15
 * Time: 17:15
 */
public class ListChoiceBoxFactory<T> implements Callback<Void, FXFormNode> {

    private final ListProperty<T> choices;

    public ListChoiceBoxFactory(ListProperty<T> choices) {
        this.choices = choices;
    }

    public FXFormNode call(Void aVoid) {

        return new FXFormChoiceBoxNode() {
            @Override
            public void init(Element element) {
                choiceBox.itemsProperty().bind(choices);
                choiceBox.getSelectionModel().select(element.getValue());
            }

            @Override
            public boolean isEditable() {
                return true;
            }

        };

    }


}

/*
 * Copyright (c) 2012, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Skin for the MapPropertyControl.
 * <p/>
 * Created at 14/10/13 17:39.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class MapPropertySkin implements Skin<MapPropertyControl> {
    /**
     * The logger
     */
    private static final System.Logger logger = System.getLogger(MapPropertySkin.class.getName());

    private final MapPropertyControl control;

    GridPane gridPane = new GridPane();

    int index = 0;

    private Map<String, Integer> indexMap = new HashMap<String, Integer>();

    BorderPane root = new BorderPane();
    private MapChangeListener<String, String> mapChangeListener = new MapChangeListener<String, String>() {
        @Override
        public void onChanged(Change<? extends String, ? extends String> change) {
            if (change.wasAdded()) {
                addEntry(change.getKey(), change.getValueAdded());
            }
            if (change.wasRemoved()) {
                removeEntry(change.getKey(), change.getValueRemoved());
            }
        }
    };

    public MapPropertySkin(MapPropertyControl control) {
        this.control = control;
        control.mapProperty().addListener(new ChangeListener<ObservableMap<String, String>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableMap<String, String>> observableValue, ObservableMap<String, String> stringStringObservableMap, ObservableMap<String, String> stringStringObservableMap2) {
                if (stringStringObservableMap != null) {
                    unconfigure(stringStringObservableMap);
                }
                if (stringStringObservableMap2 != null) {
                    configure(stringStringObservableMap2);
                }
            }
        });
        if (control.getMap() != null) {
            configure(control.getMap());
        }
        final TextField keyTextField = new TextField();
        final TextField valueTextField = new TextField();
        gridPane.setHgap(10);
        root.setCenter(gridPane);
        Button button = new Button("Add");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MapPropertySkin.this.control.getMap().put(keyTextField.getText(), valueTextField.getText());
            }
        });
        HBox hBox = new HBox(keyTextField, valueTextField, button);
        root.setBottom(hBox);
    }

    private void configure(ObservableMap<String, String> stringStringObservableMap2) {
        for (String key : stringStringObservableMap2.keySet()) {
            addEntry(key, stringStringObservableMap2.get(key));
        }
        stringStringObservableMap2.addListener(mapChangeListener);
    }

    private void addEntry(final String key, final String value) {
        Label keyLabel = new Label(key);
        Label valueLabel = new Label(value);
        Button button = new Button("-");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removeEntry(key, value);
            }
        });
        GridPane.setColumnIndex(keyLabel, 0);
        GridPane.setColumnIndex(valueLabel, 1);
        GridPane.setColumnIndex(button, 2);
        GridPane.setRowIndex(keyLabel, index);
        GridPane.setRowIndex(valueLabel, index);
        GridPane.setRowIndex(button, index);
        gridPane.getChildren().addAll(keyLabel, valueLabel, button);
        indexMap.put(key, index);
        index++;
    }

    private void removeEntry(String key, String valueRemoved) {
        removeIndex(indexMap.get(key));
        indexMap.remove(key);
    }

    private void removeIndex(int i) {
        List<Node> toRemove = new LinkedList<Node>();
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == i) {
                toRemove.add(node);
            }
        }
        for (Node node : toRemove) {
            gridPane.getChildren().remove(node);
        }
    }

    private void unconfigure(ObservableMap<String, String> stringStringObservableMap) {
        stringStringObservableMap.removeListener(mapChangeListener);
        gridPane.getChildren().clear();
        index = 0;
    }

    @Override
    public MapPropertyControl getSkinnable() {
        return control;
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void dispose() {
    }

}
package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import javafx.scene.control.ColorPicker;
import javafx.util.Callback;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 26/11/2013
 * Time: 10:27
 */
public class ColorPickerFactory implements Callback<Void, FXFormNode> {

    @Override
    public FXFormNode call(Void aVoid) {
        ColorPicker colorPicker = new ColorPicker();
        return new FXFormNodeWrapper(colorPicker, colorPicker.valueProperty());
    }

}

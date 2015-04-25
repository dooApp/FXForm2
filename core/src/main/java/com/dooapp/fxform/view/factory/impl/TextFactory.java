package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 25/04/15
 * Time: 10:58
 */
public class TextFactory implements Callback<Void, FXFormNode> {

    public FXFormNode call(Void aVoid) {
        final TextFlow textFlow = new TextFlow();
        final Text text = new Text();
        textFlow.getChildren().add(text);
        return new FXFormNodeWrapper(textFlow, text.textProperty());
    }

}

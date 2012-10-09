package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.control.ConstraintLabel;
import javafx.util.Callback;

/**
 * Created at 28/09/12 17:51.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class DefaultConstraintFactory implements Callback<Void, FXFormNode> {

    public FXFormNode call(Void aVoid) {
        ConstraintLabel constraintLabel = new ConstraintLabel();
        return new FXFormNodeWrapper(constraintLabel, constraintLabel.constraintProperty());
    }
}
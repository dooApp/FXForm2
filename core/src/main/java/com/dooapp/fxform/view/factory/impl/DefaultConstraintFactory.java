package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.control.ConstraintLabel;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created at 28/09/12 17:51.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class DefaultConstraintFactory implements Callback<Void, FXFormNode> {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultConstraintFactory.class);

    public FXFormNode call(Void aVoid) {
        ConstraintLabel constraintLabel = new ConstraintLabel();
        return new FXFormNodeWrapper(constraintLabel, constraintLabel.constraintProperty());
    }
}
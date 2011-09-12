package com.dooapp.fxform;

import com.dooapp.fxform.annotation.NonVisual;
import javafx.beans.property.*;
import org.junit.Ignore;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 16:19
 */
@Ignore
public class TestBean {

    private final StringProperty stringProperty = new SimpleStringProperty();

    private final BooleanProperty booleanProperty = new SimpleBooleanProperty();

    @NonVisual
    private final IntegerProperty integerProperty = new SimpleIntegerProperty();

    private final DoubleProperty doubleProperty = new SimpleDoubleProperty();

}

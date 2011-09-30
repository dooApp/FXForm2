package com.dooapp.fxform;

import com.dooapp.fxform.annotation.NonVisual;
import javafx.beans.property.*;
import org.junit.Ignore;

/**
 * Model object used for unit tests. Be careful when updating this bean, since most tests are based on its structure.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
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

    private final ObjectProperty<TestEnum> objectProperty = new SimpleObjectProperty<TestEnum>();

}

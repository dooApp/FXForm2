package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.model.Element;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;

import java.text.Format;
import java.text.NumberFormat;

/**
 * Default format provider implementation<br>
 * <br>
 * Created at 20/09/11 11:34.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class FormatProviderImpl implements FormatProvider {

    public Format getFormat(Element element) {
        if (IntegerProperty.class.isAssignableFrom(element.getField().getType())) {
            return NumberFormat.getIntegerInstance();
        } else if (LongProperty.class.isAssignableFrom(element.getField().getType())) {
            return NumberFormat.getNumberInstance();
        } else if (DoubleProperty.class.isAssignableFrom(element.getField().getType())) {
            return NumberFormat.getNumberInstance();
        }
        return new ToStringFormat();
    }
}
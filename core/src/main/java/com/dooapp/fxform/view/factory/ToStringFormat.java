package com.dooapp.fxform.view.factory;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * A one-direction Format (no parsing supported) based on toString()<br>
 * <br>
 * Created at 20/09/11 11:37.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ToStringFormat extends Format {

    @Override
    public StringBuffer format(Object o, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        stringBuffer.append(o.toString());
        return stringBuffer;
    }

    @Override
    public Object parseObject(String s, ParsePosition parsePosition) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
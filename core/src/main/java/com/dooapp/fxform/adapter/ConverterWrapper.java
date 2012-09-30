package com.dooapp.fxform.adapter;

import javafx.util.StringConverter;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 29/09/12
 * Time: 19:03
 */
public class ConverterWrapper<T> implements Adapter<T, String> {

    private final StringConverter converter;

    public ConverterWrapper(StringConverter converter) {
        this.converter = converter;
    }

    @Override
    public String adaptTo(T from) {
        return converter.toString(from);
    }

    @Override
    public T adaptFrom(String to) {
        return (T) converter.fromString(to);
    }
}

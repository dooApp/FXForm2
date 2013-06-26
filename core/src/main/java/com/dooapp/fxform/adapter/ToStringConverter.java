package com.dooapp.fxform.adapter;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 21/03/13
 * Time: 17:38
 */
public class ToStringConverter implements Adapter<Object, String> {

    @Override
    public String adaptTo(Object from) {
        if (from != null) {
            return from.toString();
        } else {
            return "";
        }
    }

    @Override
    public Object adaptFrom(String to) {
        throw new UnsupportedOperationException("This converter should not be used to convert a String back to an Object," +
                "if you need to do this, register your own Adapter.");
    }

}

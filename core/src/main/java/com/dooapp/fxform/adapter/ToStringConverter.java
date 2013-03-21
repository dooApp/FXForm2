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
        return null;
    }

}

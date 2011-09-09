package com.dooapp.fxform.view.handler;

import com.dooapp.fxform.reflection.Util;

import java.lang.reflect.Field;

/**
 * User: antoine
 * Date: 09/09/11
 * Time: 14:50
 */
public class EnumHandler implements FieldHandler {

    public boolean handle(Field field) {
        try {
            return Util.getObjectPropertyGeneric(field).isEnum();
        } catch (Exception e) {
        }
        return false;
    }
}

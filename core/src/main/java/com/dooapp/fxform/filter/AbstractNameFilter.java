package com.dooapp.fxform.filter;

import com.dooapp.fxform.model.FormException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 15:15
 */
public abstract class AbstractNameFilter implements FieldFilter {

    protected final String[] names;

    public AbstractNameFilter(String[] names) {
        this.names = names;
    }

    protected Field extractFieldByName(List<Field> remaining, String name) throws FormException {
        for (Field field : remaining) {
            if (name.equals(field.getName())) {
                remaining.remove(field);
                return field;
            }
        }
        throw new FormException(name + "not found in field list");
    }
}

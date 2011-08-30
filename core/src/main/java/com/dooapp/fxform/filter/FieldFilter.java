package com.dooapp.fxform.filter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Antoine Mischler
 * Date: 30/08/11
 * Time: 09:15
 */
public interface FieldFilter {

    public List<Field> filter(List<Field> toFilter);

}

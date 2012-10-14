package com.dooapp.fxform.model;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:04
 */
public interface ElementFactory {

    public Element create(Field field) throws FormException;

}

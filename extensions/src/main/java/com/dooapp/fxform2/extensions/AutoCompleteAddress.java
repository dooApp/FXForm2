package com.dooapp.fxform2.extensions;

import java.lang.annotation.*;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 27/10/2017
 * Time: 13:58
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoCompleteAddress {

    AddressField value();

}

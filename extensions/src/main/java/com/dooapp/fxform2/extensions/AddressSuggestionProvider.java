package com.dooapp.fxform2.extensions;

import java.util.Collection;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 27/10/2017
 * Time: 15:17
 */
public interface AddressSuggestionProvider {

    Collection<Address> getSuggestions(String address);

}

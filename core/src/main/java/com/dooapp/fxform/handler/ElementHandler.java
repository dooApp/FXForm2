package com.dooapp.fxform.handler;

import com.dooapp.fxform.model.Element;

/**
 * Created at 28/09/12 10:10.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public interface ElementHandler<T extends Element> {

    public boolean handle(T element);

}

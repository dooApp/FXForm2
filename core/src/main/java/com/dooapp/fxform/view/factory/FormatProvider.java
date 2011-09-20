package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.model.Element;

import java.text.Format;

/**
 * Interface used to provide the various {@code Format} for Object<->String conversion.<br>
 * <br>
 * Created at 20/09/11 11:11.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public interface FormatProvider {

    public Format getFormat(Element element);

}

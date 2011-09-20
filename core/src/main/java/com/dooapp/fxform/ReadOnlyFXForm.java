package com.dooapp.fxform;

import com.dooapp.fxform.view.factory.FormatProvider;
import com.dooapp.fxform.view.factory.ReadOnlyFactory;

/**
 * A read-only form<br>
 * <br>
 * Created at 20/09/11 10:49.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ReadOnlyFXForm<T> extends FXForm<T> {

    public ReadOnlyFXForm() {
        super(new ReadOnlyFactory());
    }

    public ReadOnlyFXForm(FormatProvider formatProvider) {
        super(new ReadOnlyFactory(formatProvider));
    }

    public ReadOnlyFXForm(T source) {
        super(source, new ReadOnlyFactory());
    }

    public ReadOnlyFXForm(T source, FormatProvider formatProvider) {
        super(source, new ReadOnlyFactory(formatProvider));
    }

}
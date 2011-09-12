package com.dooapp.fxform.filter;

import com.dooapp.fxform.reflection.impl.ReflectionFieldProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 16:23
 */
@Ignore
public abstract class AbstractFilterTest {

    private FieldFilter filter;

    private List<Field> toFilter;

    protected List<Field> filtered;

    @Before
    public void setUp() throws Exception {
        this.filter = createFilter();
        this.toFilter = new ReflectionFieldProvider().getProperties(new TestBean());
        filtered = filter.filter(toFilter);
    }

    abstract FieldFilter createFilter();

    protected boolean containsNamedField(String name, List<Field> fields) {
        for (Field field : fields) {
            if (name.equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

}

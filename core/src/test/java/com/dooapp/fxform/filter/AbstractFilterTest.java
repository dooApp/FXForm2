package com.dooapp.fxform.filter;

import com.dooapp.fxform.TestUtils;
import com.dooapp.fxform.model.Element;
import org.junit.Before;
import org.junit.Ignore;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 16:23
 */
@Ignore
public abstract class AbstractFilterTest {

    private FieldFilter filter;

    private List<Element> toFilter;

    protected List<Element> filtered;

    @Before
    public void setUp() throws Exception {
        this.filter = createFilter();
        this.toFilter = TestUtils.getTestFields();
        filtered = filter.filter(toFilter);
    }

    abstract FieldFilter createFilter();

}

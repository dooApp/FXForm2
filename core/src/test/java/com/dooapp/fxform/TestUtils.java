package com.dooapp.fxform;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyFieldElement;
import com.dooapp.fxform.reflection.impl.ReflectionFieldProvider;
import org.junit.Ignore;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 16:53
 */
@Ignore
public class TestUtils {

    /**
     * Check whether a list of fields contains a field with the given name.
     *
     * @param name
     * @param fields
     * @return
     */
    public static boolean containsNamedField(String name, List<Element> fields) {
        for (Element field : fields) {
            if (name.equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of test fields based from TestBean.
     *
     * @return
     */
    public static List<Element> getTestFields() {
        List<Field> fields = new ReflectionFieldProvider().getProperties(new TestBean());
        List<Element> elements = new LinkedList<Element>();
        for (Field field: fields) {
            try {
                elements.add(new ReadOnlyPropertyFieldElement(field));
            } catch (FormException e) {
                e.printStackTrace();
            }
        }
        return elements;
    }

}

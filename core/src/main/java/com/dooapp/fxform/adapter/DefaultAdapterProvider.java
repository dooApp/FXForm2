package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 29/09/12
 * Time: 17:31
 */
public class DefaultAdapterProvider implements AdapterProvider {

    private final static Map<AdapterMatcher, Adapter> DEFAULT_MAP = new HashMap();

    private final static Map<AdapterMatcher, Adapter> GLOBAL_MAP = new HashMap();

    private final Map<AdapterMatcher, Adapter> USER_MAP = new HashMap();

    public DefaultAdapterProvider() {
        DEFAULT_MAP.put(new TypeAdapterMatcher(IntegerProperty.class, StringProperty.class),
                new ConverterWrapper(new IntegerStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(FloatProperty.class, StringProperty.class),
                new ConverterWrapper(new FloatStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(DoubleProperty.class, StringProperty.class),
                new ConverterWrapper(new DoubleStringConverter()));
    }

    @Override
    public Adapter getAdapter(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode) {
        // check user defined factories
        Adapter adapter = getAdapter(fromClass, toClass, element, fxFormNode, USER_MAP);
        // check user defined global factories
        if (adapter == null) {
            adapter = getAdapter(fromClass, toClass, element, fxFormNode, GLOBAL_MAP);
        }
        // check default map
        if (adapter == null) {
            adapter = getAdapter(fromClass, toClass, element, fxFormNode, DEFAULT_MAP);
        }
        // use default factory
        if (adapter == null) {
            adapter = new DefaultAdapter();
        }
        return adapter;
    }

    private Adapter getAdapter(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode, Map<AdapterMatcher, Adapter> map) {
        for (AdapterMatcher matcher : map.keySet()) {
            if (matcher.matches(fromClass, toClass, element, fxFormNode)) {
                return map.get(matcher);
            }
        }
        return null;
    }

    public static void addGlobalAdapter(AdapterMatcher adapterMatcher, Adapter adapter) {
        GLOBAL_MAP.put(adapterMatcher, adapter);
    }

    public void addFactory(AdapterMatcher adapterMatcher, Adapter adapter) {
        USER_MAP.put(adapterMatcher, adapter);
    }

}

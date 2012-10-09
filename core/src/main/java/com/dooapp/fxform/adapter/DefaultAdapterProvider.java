package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.property.ChoiceBoxDefaultProperty;
import javafx.beans.property.*;
import javafx.util.converter.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 29/09/12
 * Time: 17:31
 */
public class DefaultAdapterProvider implements AdapterProvider {

    private final static Logger logger = Logger.getLogger(DefaultAdapterProvider.class.getName());

    private final static Map<AdapterMatcher, Adapter> DEFAULT_MAP = new HashMap();

    private final static Map<AdapterMatcher, Adapter> GLOBAL_MAP = new HashMap();

    private final Map<AdapterMatcher, Adapter> USER_MAP = new HashMap();

    public DefaultAdapterProvider() {
        DEFAULT_MAP.put(new TypeAdapterMatcher(StringProperty.class, StringProperty.class),
                new ConverterWrapper(new DefaultStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(IntegerProperty.class, StringProperty.class),
                new ConverterWrapper(new IntegerStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(FloatProperty.class, StringProperty.class),
                new ConverterWrapper(new FloatStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(DoubleProperty.class, StringProperty.class),
                new ConverterWrapper(new DoubleStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(BooleanProperty.class, StringProperty.class),
                new ConverterWrapper(new BooleanStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(BooleanProperty.class, BooleanProperty.class)
                , new DefaultAdapter());
        DEFAULT_MAP.put(new TypeAdapterMatcher(BooleanProperty.class, BooleanProperty.class)
                , new DefaultAdapter());
        DEFAULT_MAP.put(new TypeAdapterMatcher(ObjectProperty.class, ChoiceBoxDefaultProperty.class)
                , new DefaultAdapter());
        DEFAULT_MAP.put(new TypeAdapterMatcher(IntegerProperty.class, IntegerProperty.class)
                , new DefaultAdapter());
        DEFAULT_MAP.put(new TypeAdapterMatcher(FloatProperty.class, FloatProperty.class)
                , new DefaultAdapter());
        DEFAULT_MAP.put(new TypeAdapterMatcher(DoubleProperty.class, DoubleProperty.class)
                , new DefaultAdapter());
        DEFAULT_MAP.put(new TypeAdapterMatcher(IntegerProperty.class, DoubleProperty.class),
                new Adapter<Integer, Double>() {

                    @Override
                    public Double adaptTo(Integer from) {
                        return from.doubleValue();
                    }

                    @Override
                    public Integer adaptFrom(Double to) {
                       return to.intValue();
                    }
                });
        DEFAULT_MAP.put(new TypeAdapterMatcher(FloatProperty.class, DoubleProperty.class),
                new Adapter<Float, Double>() {

                    @Override
                    public Double adaptTo(Float from) {
                        return from.doubleValue();
                    }

                    @Override
                    public Float adaptFrom(Double to) {
                        return to.floatValue();
                    }
                });
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
        if (adapter == null) {
            adapter = new DefaultAdapter();
            logger.log(Level.WARNING, "No adapter between types " + fromClass + " and " + toClass + " was found (to adapt " + element + " and " + fxFormNode + ")" +
                    "\nMake sure to register the required adapter in DefaultAdapterProvider either in the global or in the user map. See FXForm#setAdapterProvider");
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

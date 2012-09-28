package com.dooapp.fxform.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created at 27/09/12 11:08.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class DefaultAdapter implements Adapter<Object, Object> {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultAdapter.class);

    public Object adaptTo(Object from) {
        return from;
    }

    public Object adaptFrom(Object to) {
        return to;
    }
}
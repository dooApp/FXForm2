package com.dooapp.fxform.filter;

import com.dooapp.fxform.model.FormException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 15:10
 */
public class ExcludeFilter extends AbstractNameFilter implements FieldFilter {

    private final Logger logger = LoggerFactory.getLogger(ExcludeFilter.class);

    public ExcludeFilter(String[] names) {
        super(names);
    }

    public List<Field> filter(List<Field> toFilter) {
        for (String name : names) {
            try {
                extractFieldByName(toFilter, name);
            } catch (FormException e) {
                logger.warn(name + " not found in field list for exclusion", e);
            }
        }
        return toFilter;
    }
}

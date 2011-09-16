package com.dooapp.fxform.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dooapp.fxform.model.FormException;

/**
 * User: antoine Date: 12/09/11 Time: 15:10
 */
public class IncludeFilter extends AbstractNameFilter implements FieldFilter {

	private final Logger logger = LoggerFactory.getLogger(IncludeFilter.class);

	public IncludeFilter(String... names) {
		super(names);
	}

	public List<Field> filter(List<Field> toFilter) {
		List<Field> filtered = new ArrayList<Field>();
		for (String name : names) {
			try {
				filtered.add(extractFieldByName(toFilter, name));
			} catch (FormException e) {
				logger.warn(name + " not found in field list for inclusion", e);
			}
		}
		return filtered;
	}
}

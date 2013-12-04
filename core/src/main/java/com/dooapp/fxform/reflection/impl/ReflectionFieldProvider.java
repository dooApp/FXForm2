/*
 * Copyright (c) 2013, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.reflection.impl;

import com.dooapp.fxform.reflection.FieldProvider;
import com.dooapp.fxform.reflection.MultipleBeanSource;
import com.dooapp.fxform.reflection.ReflectionUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This default implementations retrieves all fields of the given source object, including inherited fields.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/04/11
 * Time: 22:31
 */
public class ReflectionFieldProvider implements FieldProvider {

    private Map<Class, WeakReference<List<Field>>> fullCache = new HashMap<Class, WeakReference<List<Field>>>();
    private Map<Class, WeakReference<List<Field>>> includeCache = new HashMap<Class, WeakReference<List<Field>>>();

    public List<Field> getProperties(Object source, String... fields) {
        List<Field> result = new LinkedList<Field>();
        if (source != null) {
            if (source instanceof MultipleBeanSource) {
                MultipleBeanSource multipleBeanSource = (MultipleBeanSource) source;
                for (Object s : multipleBeanSource.getSources()) {
                    getFields(result, s.getClass(), fields);
                }
            } else {
                getFields(result, source.getClass(), fields);
            }
        }
        return result;
    }

    /**
     * Cache implementation. We use two caches for request with or without include filters.
     * In the case we have an include filter, we check whether we have the required fields in the cache,
     * and if it's not the case we try to retrieve only the missing fields.
     */
    private void getFields(List<Field> result, Class clazz, String[] fields) {
        if (fields != null && fields.length > 0) {
            if (includeCache.containsKey(clazz)) {
                List<Field> cachedList = includeCache.get(clazz).get();
                if (cachedList != null) {
                    List<String> fieldsCopy = new LinkedList();
                    for (String s : fields) {
                        fieldsCopy.add(s);
                    }
                    for (Field field : cachedList) {
                        if (fieldsCopy.remove(field.getName())) {
                            result.add(field);
                        }
                    }
                    if (!fieldsCopy.isEmpty()) {
                        ReflectionUtils.fillFieldsByName(clazz, result, fieldsCopy.toArray(new String[]{}));
                        includeCache.put(clazz, new WeakReference<List<Field>>(result));
                    }
                } else {
                    ReflectionUtils.fillFieldsByName(clazz, result, fields);
                    includeCache.put(clazz, new WeakReference<List<Field>>(result));
                }
            } else {
                ReflectionUtils.fillFieldsByName(clazz, result, fields);
                includeCache.put(clazz, new WeakReference<List<Field>>(result));
            }
        } else {
            if (fullCache.containsKey(clazz)) {
                List<Field> cachedList = fullCache.get(clazz).get();
                if (cachedList != null) {
                    result.addAll(cachedList);
                } else {
                    ReflectionUtils.fillFields(clazz, result);
                    fullCache.put(clazz, new WeakReference<List<Field>>(result));
                }
            } else {
                ReflectionUtils.fillFields(clazz, result);
                fullCache.put(clazz, new WeakReference<List<Field>>(result));
            }
        }
    }

}


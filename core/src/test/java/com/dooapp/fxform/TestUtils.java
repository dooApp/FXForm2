/*
 * Copyright (c) 2012, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

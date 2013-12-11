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

package com.dooapp.fxform.filter;

import com.dooapp.fxform.model.Element;
import sun.launcher.resources.launcher_sv;

import java.util.LinkedList;
import java.util.List;

/**
 * Filter used to attribute categories to the elements.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 11/12/2013
 * Time: 13:28
 */
public class CategorizeFilter extends AbstractNameFilter implements FieldFilter {

    public CategorizeFilter(String[] names) {
        super(names);
    }

    @Override
    public List<Element> filter(List<Element> toFilter) throws FilterException {
        String category = null;
        List<Element> remaining = new LinkedList<Element>(toFilter);
        List<Element> list = new LinkedList<Element>();
        for (String name : getNames()) {
            if (name.startsWith("-")) {
                category = name;
            } else {
                Element element = extractFieldByName(remaining, name);
                element.setCategory(category);
                list.add(element);
            }
        }
        list.addAll(remaining);
        return list;
    }

}

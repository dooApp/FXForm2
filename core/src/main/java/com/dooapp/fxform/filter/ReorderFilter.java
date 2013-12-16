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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com> Date: 01/09/11 Time: 17:12
 */
public class ReorderFilter extends AbstractNameFilter implements ElementListFilter {

	public ReorderFilter(String... names) {
		super(names);
	}

	public List<Element> filter(List<Element> toFilter) throws FilterException {
		List<Element> remaining = new ArrayList<Element>(toFilter);
		List<Element> filtered = new ArrayList<Element>();
		for (String name : names) {
			filtered.add(extractFieldByName(remaining, name));
		}
		filtered.addAll(remaining);
		return filtered;
	}

	public String toString() {
		return "ReorderFiter[" + Arrays.toString(names) + "]";
	}
}

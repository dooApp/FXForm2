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
package com.dooapp.fxform.reflection;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.TestBean;
import com.dooapp.fxform.model.Element;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 21/11/2013
 * Time: 16:13
 */
public class MultipleBeanSourceTest {

	public static class TestBean2 {

		private final StringProperty propInBean2 = new SimpleStringProperty();
	}

	@Test
	public void testMultipleBeanSource() throws InvalidArgumentException {
		FXForm fxForm = new FXForm();
		fxForm.setSource(new MultipleBeanSource(new TestBean(), new TestBean2()));
		Assert.assertEquals(5, fxForm.getElements().size());
		Assert.assertTrue(hasElement(fxForm.getElements(), "propInBean2"));
		Assert.assertTrue(hasElement(fxForm.getElements(), "stringProperty"));
		Assert.assertTrue(hasElement(fxForm.getElements(), "booleanProperty"));
		Assert.assertTrue(hasElement(fxForm.getElements(), "doubleProperty"));
		Assert.assertTrue(hasElement(fxForm.getElements(), "objectProperty"));
	}

	protected boolean hasElement(List<Element> elementList, String name) {
		for (Element element : elementList) {
			if (name.equals(element.getName())) {
				return true;
			}
		}
		return false;
	}
}

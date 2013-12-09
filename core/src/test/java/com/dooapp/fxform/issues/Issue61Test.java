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
package com.dooapp.fxform.issues;

import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.reflection.MultipleBeanSource;
import com.dooapp.fxform.reflection.ReflectionUtils;
import com.dooapp.fxform.reflection.impl.ReflectionFieldProvider;
import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Kevin Senechal <kevin.senechal@dooapp.com>
 * Date: 09/12/2013
 * Time: 15:14
 */
public class Issue61Test {

	public static class Bean1 {

		private StringProperty property = new SimpleStringProperty();
	}

	public static class Bean2 {

		private StringProperty property = new SimpleStringProperty();
	}

	@Test
	public void testExtractExpectedFields()
			throws
			FormException,
			InvalidArgumentException {
		Bean1 bean1 = new Bean1();
		Bean2 bean2 = new Bean2();
		ReflectionFieldProvider reflectionFieldProvider = new ReflectionFieldProvider();
		List<Field> fields = reflectionFieldProvider.getProperties(new MultipleBeanSource(bean1, bean2),
				Bean1.class.getName() + "-property");
		Assert.assertEquals(1, fields.size());
		Assert.assertEquals(fields.get(0), ReflectionUtils.getFieldByName(Bean1.class, "property"));
	}
}

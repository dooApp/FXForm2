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

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.validation.DefaultFXFormValidator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * User: Kevin Senechal <kevin.senechal@dooapp.com>
 * Date: 09/12/2013
 * Time: 15:14
 */
public class Issue81Test {

	@Rule
	public JavaFXRule javaFXRule = new JavaFXRule();

	private DefaultFXFormValidator validator;

	public static class Bean1 {

		private StringProperty property = new SimpleStringProperty();

		public String getProperty() {
			return property.get();
		}
	}

	public static class Bean2 extends Bean1 {

		@NotEmpty
		public String getProperty() {
			return super.getProperty();
		}
	}

	@Before
	public void setup() {
		validator = new DefaultFXFormValidator();
	}

	@Test
	public void testThatBean1ValidationIsOk() {
		FXForm fxForm = new FXForm();
		Bean1 bean1 = new Bean1();
		fxForm.setSource(bean1);
		Assert.assertEquals(0, validator.validate(fxForm.getElements().get(0), "").size());
		Bean2 bean2 = new Bean2();
		fxForm.setSource(bean2);
		Assert.assertEquals(1, validator.validate(fxForm.getElements().get(0), "").size());
	}
}

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
package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.FormException;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Element based on a Field.
 * <p/>
 * Created at 15/10/12 08:47.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public abstract class AbstractFieldElement<SourceType, WrappedType> extends AbstractSourceElement<SourceType, WrappedType> implements Element<WrappedType> {

	protected final Field field;

	public AbstractFieldElement(Field field) throws FormException {
		super();
		this.field = field;
		init();
	}

	protected void init() {
		wrappedProperty().addListener((observableValue, oldValue, newValue) -> {
			for (InvalidationListener invalidationListener : invalidationListeners) {
				if (oldValue != null) {
					oldValue.removeListener(invalidationListener);
				}
				if (newValue != null) {
					newValue.addListener(invalidationListener);
				}
				invalidationListener.invalidated(observableValue);
			}
			for (ChangeListener<? super WrappedType> changeListener : changeListeners) {
				if (oldValue != null) {
					oldValue.removeListener(changeListener);
				}
				if (newValue != null) {
					newValue.addListener(changeListener);
					changeListener.changed(
							observableValue.getValue(),
							oldValue != null ? oldValue.getValue() : null,
							newValue.getValue());
				}
			}
		});
	}

	public Field getField() {
		return field;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

	public String getName() {
		return field.getName();
	}

	@Override
	public Class getDeclaringClass() {
		return field.getDeclaringClass();
	}

	@Override
	protected Binding<ObservableValue<WrappedType>> createValue() {
		if (List.class.isAssignableFrom(getType())) {
			return new ListBinding() {
				{
					super.bind(sourceProperty());
				}

				@Override
				protected ObservableList computeValue() {
					if (getSource() == null) {
						return null;
					}
					return (ObservableList) AbstractFieldElement.this.computeValue();
				}

				@Override
				public void dispose() {
					super.dispose();
					unbind(sourceProperty());
				}
			};
		}
		return new ObjectBinding<ObservableValue<WrappedType>>() {
			{
				super.bind(sourceProperty());
			}

			@Override
			protected ObservableValue<WrappedType> computeValue() {
				if (getSource() == null) {
					return null;
				}
				return AbstractFieldElement.this.computeValue();
			}

			@Override
			public void dispose() {
				super.dispose();
				unbind(sourceProperty());
			}
		};
	}
}
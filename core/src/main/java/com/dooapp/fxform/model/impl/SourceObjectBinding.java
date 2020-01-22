package com.dooapp.fxform.model.impl;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;

import java.lang.ref.WeakReference;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/01/2020
 * Time: 12:04
 */
public class SourceObjectBinding<E> extends ObjectBinding<ObservableValue<E>> {

    private final WeakReference<AbstractFieldElement> element;

    public SourceObjectBinding(AbstractFieldElement element) {
        super.bind(element.sourceProperty());
        this.element = new WeakReference<>(element);
    }

    @Override
    protected ObservableValue<E> computeValue() {
        if (element.get() != null && element.get().getSource() == null) {
            return null;
        }
        return element.get().computeValue();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (element.get() != null) {
            unbind(element.get().sourceProperty());
        }
    }
}

package com.dooapp.fxform.model.impl;

import javafx.beans.binding.MapBinding;
import javafx.collections.ObservableMap;

import java.lang.ref.WeakReference;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/01/2020
 * Time: 12:29
 */
public class SourceMapBinding extends MapBinding {

    private final WeakReference<AbstractFieldElement> element;

    public SourceMapBinding(AbstractFieldElement element) {
        super.bind(element.sourceProperty());
        this.element = new WeakReference<>(element);
    }

    @Override
    protected ObservableMap computeValue() {
        if (element.get() == null || element.get().getSource() == null) {
            return null;
        }
        return (ObservableMap) element.get().computeValue();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (element.get() != null) {
            unbind(element.get().sourceProperty());
        }
    }

}

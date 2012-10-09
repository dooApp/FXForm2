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

package com.dooapp.fxform.utils;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/08/11
 * Time: 14:07
 */
public class ConfigurationStore<T> implements ObservableList<T> {

    private ObservableList<T> store = FXCollections.observableList(new ArrayList<T>());

    private ObservableList<Configurer<T>> configurers = FXCollections.observableList(new ArrayList<Configurer<T>>());

    public ObservableList<T> getStore() {
        return store;
    }

    public ObservableList<Configurer<T>> getConfigurers() {
        return configurers;
    }

    public ConfigurationStore() {
        store.addListener(new ListChangeListener() {

            public void onChanged(Change change) {
                while (change.next()) {
                    configure(change.getAddedSubList());
                    unconfigure(change.getRemoved());
                }
            }
        });
        configurers.addListener(new ListChangeListener() {

            public void onChanged(Change change) {
                while (change.next()) {
                    configurerAdded(change.getAddedSubList());
                    configurerRemoved(change.getRemoved());
                }
            }
        });
    }

    protected void configure(Collection<T> toConfigure) {
        for (T t : toConfigure) {
            for (Configurer c : configurers) {
                c.configure(t);
            }
        }
    }

    protected void unconfigure(Collection<T> toConfigure) {
        for (T t : toConfigure) {
            for (Configurer c : configurers) {
                c.unconfigure(t);
            }
        }
    }

    protected void configurerAdded(Collection<Configurer<T>> configurer) {
        for (Configurer c : configurer) {
            for (T t : store) {
                c.configure(t);
            }
        }
    }

    protected void configurerRemoved(Collection<Configurer<T>> configurer) {
        for (Configurer c : configurer) {
            for (T t : store) {
                c.unconfigure(t);
            }
        }
    }

    public void addConfigurer(Configurer<T> configurer) {
        configurers.add(configurer);
    }

    public void removeConfigurer(Configurer<T> configurer) {
        configurers.remove(configurer);
    }

    public void addListener(ListChangeListener<? super T> listChangeListener) {
        store.addListener(listChangeListener);
    }

    public void removeListener(ListChangeListener<? super T> listChangeListener) {
        store.removeListener(listChangeListener);
    }

    public boolean addAll(T... ts) {
        return store.addAll(ts);
    }

    public boolean setAll(T... ts) {
        return store.setAll(ts);
    }

    public boolean setAll(Collection<? extends T> ts) {
        return store.setAll(ts);
    }

    public boolean removeAll(T... ts) {
        return store.removeAll(ts);
    }

    public boolean retainAll(T... ts) {
        return store.retainAll(ts);
    }

    public void remove(int i, int i1) {
        store.remove(i, i1);
    }

    public int size() {
        return store.size();
    }

    public boolean isEmpty() {
        return store.isEmpty();
    }

    public boolean contains(Object o) {
        return store.contains(o);
    }

    public Iterator<T> iterator() {
        return store.iterator();
    }

    public Object[] toArray() {
        return store.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return store.toArray(ts);
    }

    public boolean add(T t) {
        return store.add(t);
    }

    public boolean remove(Object o) {
        return store.remove(o);
    }

    public boolean containsAll(Collection<?> objects) {
        return store.containsAll(objects);
    }

    public boolean addAll(Collection<? extends T> ts) {
        return store.addAll(ts);
    }

    public boolean addAll(int i, Collection<? extends T> ts) {
        return store.addAll(i, ts);
    }

    public boolean removeAll(Collection<?> objects) {
        return store.removeAll(objects);
    }

    public boolean retainAll(Collection<?> objects) {
        return store.retainAll(objects);
    }

    public void clear() {
        store.clear();
    }

    @Override
    public boolean equals(Object o) {
        return store.equals(o);
    }

    @Override
    public int hashCode() {
        return store.hashCode();
    }

    public T get(int i) {
        return store.get(i);
    }

    public T set(int i, T t) {
        return store.set(i, t);
    }

    public void add(int i, T t) {
        store.add(i, t);
    }

    public T remove(int i) {
        return store.remove(i);
    }

    public int indexOf(Object o) {
        return store.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return store.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return store.listIterator();
    }

    public ListIterator<T> listIterator(int i) {
        return store.listIterator(i);
    }

    public List<T> subList(int i, int i1) {
        return store.subList(i, i1);
    }

    public void addListener(InvalidationListener invalidationListener) {
        store.addListener(invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        store.removeListener(invalidationListener);
    }
}

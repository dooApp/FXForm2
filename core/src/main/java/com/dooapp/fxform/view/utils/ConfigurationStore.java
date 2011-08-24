/*
 * Copyright (c) 2011, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.view.utils;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: antoine
 * Date: 24/08/11
 * Time: 14:07
 */
public class ConfigurationStore<T> {

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

}

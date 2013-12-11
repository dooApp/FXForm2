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

package com.dooapp.fxform.builder;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.ReadOnlyFXForm;
import com.dooapp.fxform.filter.CategorizeFilter;
import com.dooapp.fxform.filter.IncludeFilter;
import com.dooapp.fxform.filter.ReorderFilter;
import com.dooapp.fxform.view.skin.FXMLSkin;
import com.dooapp.fxform.view.skin.InlineSkin;
import javafx.scene.control.Skin;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * User: Bastien Billey <bastien.billey@dooapp.com>
 * Date: 29/11/2013
 * Time: 10:52
 */
public class FXFormBuilder<BUILDER extends FXFormBuilder<?>> {

    private Skin skin;

    private Object source;

    private String[] includeFilters;

    private String[] reorderFilter;

    private String[] categorizeFilter;

    private ResourceBundle resourceBundle;

    private URL fxmlUrl;

    private boolean readOnly;

    public FXForm build() {
        FXForm res;
        if (readOnly == true) {
            res = new ReadOnlyFXForm();
        } else {
            res = new FXForm();
        }
        if (skin == null) {
            handleDefaultSkin(res);
        } else {
            res.setSkin(skin);
        }
        if (resourceBundle == null) {

        } else {
            res.setResourceBundle(resourceBundle);
        }
        if (includeFilters == null) {

        } else {
            res.addFilters(new IncludeFilter(includeFilters));
        }
        if (reorderFilter == null) {

        } else {
            res.addFilters(new ReorderFilter(reorderFilter));
        }
        if (categorizeFilter == null) {

        } else {
            res.addFilters(new CategorizeFilter(categorizeFilter));
        }
        if (source == null) {
        } else {
            res.setSource(source);
        }
        if (fxmlUrl != null) {
            res.setSkin(new FXMLSkin(res, fxmlUrl));
        }
        return res;
    }

    protected void handleDefaultSkin(FXForm fxForm) {
        fxForm.setSkin(new InlineSkin(fxForm));
    }

    public BUILDER fxml(URL url) {
        this.fxmlUrl = url;
        return (BUILDER) this;
    }

    public BUILDER skin(Skin skin) {
        this.skin = skin;
        return (BUILDER) this;
    }

    public BUILDER source(Object source) {
        this.source = source;
        return (BUILDER) this;
    }

    public BUILDER include(String... includeFilters) {
        this.includeFilters = includeFilters;
        return (BUILDER) this;
    }

    public BUILDER reorder(String... reorderFilter) {
        this.reorderFilter = reorderFilter;
        return (BUILDER) this;
    }

    public BUILDER includeAndReorder(String... strings) {
        this.includeFilters = strings;
        this.reorderFilter = strings;
        return (BUILDER) this;
    }

    public BUILDER resourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        return (BUILDER) this;
    }

    public BUILDER readOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return (BUILDER) this;
    }

    public BUILDER categorize(String... strings) {
        this.categorizeFilter = strings;
        return (BUILDER) this;
    }

}

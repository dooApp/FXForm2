package com.dooapp.fxform.sampler.model;

import com.dooapp.fxform.sampler.Utils;

import java.util.ResourceBundle;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 04/04/14 14:27.<br>
 *
 * @author Bastien
 *
 */
public enum Movies {

    LOTR("Movies.LOTR"),
    MATRIX("Movies.MATRIX");

    public static final ResourceBundle rb = Utils.SAMPLE;

    public final String key;


    private Movies(String myKey) {
        this.key = myKey;
    }

    @Override
    public String toString() {
        return rb.getString(key);
    }

}

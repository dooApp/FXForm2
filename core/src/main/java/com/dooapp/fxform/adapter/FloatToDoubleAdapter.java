package com.dooapp.fxform.adapter;

public class FloatToDoubleAdapter implements Adapter<Float, Double> {

    @Override
    public Double adaptTo(Float from) {
        return from.doubleValue();
    }

    @Override
    public Float adaptFrom(Double to) {
        return to.floatValue();
    }
}

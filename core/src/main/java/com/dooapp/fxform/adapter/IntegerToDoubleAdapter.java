package com.dooapp.fxform.adapter;

public class IntegerToDoubleAdapter implements Adapter<Integer, Double> {

    @Override
    public Double adaptTo(Integer from) {
        return from.doubleValue();
    }

    @Override
    public Integer adaptFrom(Double to) {
        return to.intValue();
    }
}

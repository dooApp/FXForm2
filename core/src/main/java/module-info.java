module com.dooapp.fxform {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.validation;
    requires java.logging;
    exports com.dooapp.fxform;
    exports com.dooapp.fxform.builder;
    exports com.dooapp.fxform.reflection;
    exports com.dooapp.fxform.annotation;
    exports com.dooapp.fxform.view;
    exports com.dooapp.fxform.model;
    exports com.dooapp.fxform.view.factory;
    exports com.dooapp.fxform.view.factory.impl;
    exports com.dooapp.fxform.view.skin;
    exports com.dooapp.fxform.handler;
    exports com.dooapp.fxform.validation;
    exports com.dooapp.fxform.controller;
    exports com.dooapp.fxform.adapter;
    exports com.dooapp.fxform.filter;
    exports com.dooapp.fxform.filter.field;
    exports com.dooapp.fxform.view.property;
    exports com.dooapp.fxform.view.control;
}
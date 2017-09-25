package com.dooapp.fxform.sampler;

import fxsampler.FXSamplerProject;
import fxsampler.model.WelcomePage;
import javafx.scene.control.Label;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 04/04/14 13:56.<br>
 *
 * @author Bastien
 *
 */
public class FXFormSamplerProject implements FXSamplerProject {

    @Override
    public String getProjectName() {
        return "FXForm";
    }

    @Override
    public String getSampleBasePackage() {
        return "com.dooapp.fxform.samples";
    }

    @Override
    public WelcomePage getWelcomePage() {
        return new WelcomePage("FXForm sampler", new Label("Hello"));
    }
}

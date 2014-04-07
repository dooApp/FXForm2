package com.dooapp.fxform;

import fxsampler.FXSamplerProject;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 04/04/14 13:56.<br>
 *
 * @author Bastien
 *
 */
public class FXFormSampler implements FXSamplerProject {

    @Override
    public String getProjectName() {
        return "FXForm";
    }

    @Override
    public String getSampleBasePackage() {
        return "com.dooapp.fxform.samples";
    }
}

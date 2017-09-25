package com.dooapp.fxform.sampler;

import fxsampler.SampleBase;

import java.util.ResourceBundle;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 04/04/14 13:56.<br>
 *
 * @author Bastien
 *
 */
public abstract class FXFormSample extends SampleBase {
    @Override
    public String getProjectName() {
        return "FXForm";
    }

    @Override
    public String getProjectVersion() {
        return ResourceBundle.getBundle("fxformSampler").getString("version");
    }

    @Override
    public String getSampleSourceURL() {
        return "https://raw.githubusercontent.com/dooApp/FXForm2/master/samples/src/main/java/com/dooapp/fxform/samples/"+ getClass().getSimpleName() + ".java";
    }


    @Override
    public String getJavaDocURL() {
        return "";
    }
}

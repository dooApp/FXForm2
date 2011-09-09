package com.dooapp.fxform.view;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.view.skin.DefaultSkin;
import com.dooapp.fxform.view.skin.InlineSkin;

/**
 * User: Antoine Mischler
 * Date: 28/08/11
 * Time: 14:19
 */
public interface FXFormSkinFactory {

    public FXFormSkin createSkin(FXForm form);

    public final static FXFormSkinFactory DEFAULT_FACTORY = new FXFormSkinFactory() {
        public FXFormSkin createSkin(FXForm form) {
            return new DefaultSkin(form);
        }

        public String toString() {
            return "Default skin";
        }
    };

    public final static FXFormSkinFactory INLINE_FACTORY = new FXFormSkinFactory() {
        public FXFormSkin createSkin(FXForm form) {
            return new InlineSkin(form);
        }

        public String toString() {
            return "Inline skin";
        }
    };



}

package com.dooapp.fxform2.extensions;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 27/10/2017
 * Time: 12:19
 */
public class AddressFactory implements Callback<Void, FXFormNode> {

    AddressSuggestionProvider addressSuggestionProvider = new AlgoliaAddressSuggestionProvider();

    @Override
    public FXFormNode call(Void param) {
        final CustomTextField textField = new CustomTextField();
        Platform.runLater(() -> textField.setLeft(new Glyph("FontAwesome", FontAwesome.Glyph.MAP_MARKER)));
        AutoCompletionBinding<Address> autoCompletionBinding = new AutoCompletionTextFieldBinding<Address>(textField,
                param1 -> addressSuggestionProvider.getSuggestions(param1.getUserText()),
                new StringConverter<Address>() {
                    @Override
                    public String toString(Address object) {
                        return object.getStreet() + " " + object.getCity();
                    }

                    @Override
                    public Address fromString(String string) {
                        return null;
                    }
                }) {

            @Override
            protected void completeUserInput(Address completion) {
                String newText = completion.getStreet();
                this.getCompletionTarget().setText(newText);
                this.getCompletionTarget().positionCaret(newText.length());
            }
        };
        autoCompletionBinding.setDelay(500);
        return new FXFormNodeWrapper(textField, textField.textProperty()) {
            @Override
            public void init(Element element, AbstractFXForm fxForm) {
                autoCompletionBinding.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<Address>>() {
                    @Override
                    public void handle(AutoCompletionBinding.AutoCompletionEvent<Address> event) {
                        setAddressFieldValue(AddressField.CITY, event.getCompletion().getCity());
                        setAddressFieldValue(AddressField.STREET, event.getCompletion().getStreet());
                        setAddressFieldValue(AddressField.POSTCODE, event.getCompletion().getPostcode());
                    }

                    private void setAddressFieldValue(AddressField field, String value) {
                        fxForm.getElements().stream()
                                .filter(element -> element.getAnnotation(AutoCompleteAddress.class) != null
                                        && field.equals(((AutoCompleteAddress) element.getAnnotation(AutoCompleteAddress.class)).value()))
                                .findFirst().ifPresent(element -> ((PropertyElement) element).setValue(value));
                    }
                });
            }
        };
    }

}

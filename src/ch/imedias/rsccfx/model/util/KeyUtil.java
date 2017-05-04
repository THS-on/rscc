package ch.imedias.rsccfx.model.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by François Martin on 04.05.17.
 */
public class KeyUtil {

  private static final String KEY_FORMAT_DELIMITER = " ";
  private static final int KEY_FORMAT_DELIMITER_EVERY = 3;

  private final StringProperty key = new SimpleStringProperty();
  private final StringProperty formattedKey = new SimpleStringProperty();
  private final BooleanProperty keyValid = new SimpleBooleanProperty(false);

  public KeyUtil() {
    attachEvents();
    setupBindings();
  }

  private void attachEvents(){
    key.addListener(
        (observable, oldKey, newKey) -> {
          if (!oldKey.equals(newKey)) {
            setKey(deformatKey(newKey));
          }
        }
    );
  }

  private void setupBindings() {
    formattedKeyProperty().bind(
        Bindings.createStringBinding(
            () -> formatKey(getKey()), key
        )
    );

    keyValidProperty().bind(
        Bindings.createBooleanBinding(
            () -> validateKey(getKey()), key
        )
    );
  }

  /**
   * Formats the key, so it has spaces every 3 characters.
   */
  public String formatKey(String key) {
    if (key != null) {
      key = deformatKey(key); // make sure the key doesn't have spaces in it already
      Iterable<String> pieces = Splitter.fixedLength(KEY_FORMAT_DELIMITER_EVERY).split(key);
      key = Streams.stream(pieces)
          .collect(Collectors.joining(KEY_FORMAT_DELIMITER));
    }
    return key;
  }

  /**
   * Removes spaces in a key which has been previously formatted with spaces.
   */
  public String deformatKey(String key) {
    if(key != null){
      key = key.replace(KEY_FORMAT_DELIMITER,"");
    }
    return key;
  }

  /**
   * Determines if a key is valid or not.
   * The key must not be null and must be a number with exactly 9 digits.
   *
   * @param key the string to validate.
   * @return true when key has a valid format.
   */
  public boolean validateKey(String key) {
    return key != null && key.matches("\\d{9}");
  }

  public String getKey() {
    return key.get();
  }

  public StringProperty keyProperty() {
    return key;
  }

  public void setKey(String key) {
    this.key.set(key);
  }

  public String getFormattedKey() {
    return formattedKey.get();
  }

  public StringProperty formattedKeyProperty() {
    return formattedKey;
  }

  public void setFormattedKey(String formattedKey) {
    this.formattedKey.set(formattedKey);
  }

  public boolean isKeyValid() {
    return keyValid.get();
  }

  public BooleanProperty keyValidProperty() {
    return keyValid;
  }

  public void setKeyValid(boolean keyValid) {
    this.keyValid.set(keyValid);
  }
}

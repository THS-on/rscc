package ch.imedias.rsccfx.model.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Handles everything related to the key.
 */
public class KeyUtil {

  private static final Logger LOGGER =
      Logger.getLogger(KeyUtil.class.getName());

  private static final String KEY_FORMAT_DELIMITER = " ";
  private static final int KEY_FORMAT_DELIMITER_EVERY = 3;

  private final StringProperty key = new SimpleStringProperty();
  private final StringProperty formattedKey = new SimpleStringProperty();
  private final BooleanProperty keyValid = new SimpleBooleanProperty(false);

  public KeyUtil() {
    setupBindings();
  }

  private void setupBindings() {
    // if the key gets changed, this sets the formatted key
    formattedKey.bind(
        Bindings.createStringBinding(
            () -> formatKey(getKey()), key
        )
    );

    // if the key gets changed, the key will get validated
    keyValid.bind(
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
      // shorten key to 9 characters if it's too long
      if (key.length() > 9) {
        key = key.substring(0, 9);
      }
      // Split the key and join with delimiter
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
    if (key != null) {
      key = key.replace(KEY_FORMAT_DELIMITER, "");
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

  /**
   * Sets the key deformatted.
   * @param key can be a formatted or unformatted key.
   */
  public void setKey(String key) {
    this.key.set(deformatKey(key));
    LOGGER.fine("Key was set to: " + getKey() + " and " + getFormattedKey());
  }

  public String getKey() {
    return key.get();
  }

  public ReadOnlyStringProperty keyProperty() {
    return key;
  }

  public String getFormattedKey() {
    return formattedKey.get();
  }

  public ReadOnlyStringProperty formattedKeyProperty() {
    return formattedKey;
  }

  public boolean isKeyValid() {
    return keyValid.get();
  }

  public ReadOnlyBooleanProperty keyValidProperty() {
    return keyValid;
  }

  public void setKeyValid(boolean keyValid) {
    this.keyValid.set(keyValid);
  }
}

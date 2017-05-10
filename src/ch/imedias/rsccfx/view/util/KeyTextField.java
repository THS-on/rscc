package ch.imedias.rsccfx.view.util;

import ch.imedias.rsccfx.model.util.KeyUtil;
import java.util.function.Predicate;
import javafx.scene.control.TextField;

/**
 * Represents a custom version of the TextField for showing and entering of the key.
 * Validates the key and makes the implementation of the spaces every 3 characters possible.
 */
public class KeyTextField extends TextField {
  private static final int TOTAL_CHARACTERS_AMOUNT =
      KeyUtil.KEY_AMOUNT_SPACES + KeyUtil.KEY_MAXIMUM_DIGITS;

  /**
   * Returns true if the entered text is "", which means that the backspace key was pressed.
   */
  private static final Predicate<String> IS_BACKSPACE = ""::equals;

  /**
   * Returns a subset of the text input's content.
   * Modified to not throw exceptions when inserting spaces, in which start and end condition
   * of the standard TextField implementation cannot be met.
   */
  @Override
  public String getText(int start, int end) {
    if (start > end || start < 0 || end > getLength()) {
      return null;
    }
    return getContent().get(start, end);
  }

  /**
   * Replaces a range of characters with the given text.
   * Validation was added to the standard implementation.
   */
  @Override
  public void replaceText(int start, int end, String text) {
    if (validate(text)) {
      super.replaceText(start, end, text);
    }
  }

  /**
   * Replaces a range of characters with the given text upon selecting.
   * Validation was added to the standard implementation.
   */
  @Override
  public void replaceSelection(String text) {
    if (validate(text)) {
      super.replaceSelection(text);
    }
  }

  /**
   * Validates every input to make sure nothing but spaces, digits and backspaces can be entered.
   */
  private boolean validate(String text) {
    return text.matches("[0-9 ]*")
        && (super.getLength() + text.length() <= TOTAL_CHARACTERS_AMOUNT
        || IS_BACKSPACE.test(text));
  }

}

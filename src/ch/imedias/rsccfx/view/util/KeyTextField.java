package ch.imedias.rsccfx.view.util;

import javafx.scene.control.TextField;

/**
 * Created by François Martin on 05.05.17.
 */
public class KeyTextField extends TextField {

  /**
   * Returns a subset of the text input's content.
   * Modified to not throw exceptions since they are false positives.
   */
  @Override
  public String getText(int start, int end) {
    if (start > end || start < 0 || end > getLength()) {
      return null;
    }
    return getContent().get(start, end);
  }

  @Override
  public void replaceText(int start, int end, String text)
  {
    if (validate(text))
    {
      super.replaceText(start, end, text);
    }
  }

  @Override
  public void replaceSelection(String text)
  {
    if (validate(text))
    {
      super.replaceSelection(text);
    }
  }

  // match for digits, spaces and backspaces
  private boolean validate(String text)
  {
    return text.matches("[0-9 ]*");
  }

}

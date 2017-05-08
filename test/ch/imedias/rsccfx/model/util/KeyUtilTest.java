package ch.imedias.rsccfx.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the KeyUtil class.
 */
public class KeyUtilTest {
  private KeyUtil keyUtil;
  private static final String[] UNFORMATTED_KEYS = new String[] {"1", "12", "123", "123 4",
      "123 45", "123 456", "123 456 7", "123 456 78", "123 456 789"};
  private static final String[] FORMATTED_KEYS = new String[] {"1", "12", "123", "1234",
      "12345", "123456", "1234567", "12345678", "123456789"};
  private static final String KEY_WITH_SPACES = "  1  2  3   4   5  7    8 9 ";
  private static final String KEY_STANDARD = "123456789";
  private static final String KEY_STANDARD_FORMATTED = "123 456 789";


  /**
   * Initializes test fixture before each test.
   */
  @Before
  public void setUp() {
    keyUtil = new KeyUtil();
  }

  /**
   * Test for {@link KeyUtil#formatKey(String)}.
   */
  @Test
  public void formatKey() {
    // test if null is returned
    assertSame(null, keyUtil.formatKey(null));

    // test if key is being deformatted
    String key = KEY_WITH_SPACES;
    assertEquals(KEY_STANDARD_FORMATTED, keyUtil.formatKey(key));

    // test if key is kept at length of 9
    key = KEY_STANDARD_FORMATTED + "0";
    assertEquals(KEY_STANDARD_FORMATTED, keyUtil.formatKey(key));

    // test if splitting is properly done
    for (int i = 0; i < FORMATTED_KEYS.length; i++) {
      assertEquals(FORMATTED_KEYS[i], keyUtil.formatKey(UNFORMATTED_KEYS[i]));
    }
  }

  /**
   * Test for {@link KeyUtil#deformatKey(String)}.
   */
  @Test
  public void deformatKey() {
    // test if null is returned
    assertSame(null, keyUtil.deformatKey(null));

    // standard cases
    for (int i = 0; i < FORMATTED_KEYS.length; i++) {
      assertEquals(UNFORMATTED_KEYS[i], keyUtil.deformatKey(FORMATTED_KEYS[i]));
    }

    // unusual cases
    assertEquals(KEY_STANDARD, keyUtil.deformatKey(KEY_WITH_SPACES));
  }

  /**
   * Test for {@link KeyUtil#validateKey(String)}.
   */
  @Test
  public void validateKey() {
    final String[] invalidKeys =
        {"123123", "0", "12345678", "1234567890", "abcdefghi", "12345678 ", "12345678 9"};
    final String[] validKeys = {"123456789", "000000000", "999999999"};

    assertFalse(keyUtil.validateKey(null));

    for (String invalidKey : invalidKeys) {
      assertFalse(keyUtil.validateKey(invalidKey));
    }

    for (String validKey : validKeys) {
      assertTrue(keyUtil.validateKey(validKey));
    }
  }

  /**
   * Test for {@link KeyUtil#setKey(String)}.
   * Tests with an unformatted key.
   */
  @Test
  public void setKeyUnformatted() {
    // initially, no key should be set and validation should be false
    assertSame(null, keyUtil.getKey());
    assertSame(null, keyUtil.getFormattedKey());
    assertSame(false, keyUtil.isKeyValid());

    // when setting the key unformatted, it should be set and also
    // the formattedKey should be correct and validation should be true
    keyUtil.setKey(KEY_STANDARD);
    assertEquals(KEY_STANDARD, keyUtil.getKey());
    assertEquals(KEY_STANDARD_FORMATTED, keyUtil.getFormattedKey());
    assertSame(true, keyUtil.isKeyValid());

    // test if validation is false again
    keyUtil.setKey("123");
    assertSame(false, keyUtil.isKeyValid());
  }

  /**
   * Test for {@link KeyUtil#setKey(String)}.
   * Tests with a formatted key.
   */
  @Test
  public void setKeyFormatted() {
    // initially, no key should be set
    assertSame(null, keyUtil.getKey());
    assertSame(null, keyUtil.getFormattedKey());

    // when setting the key formatted, it should be set deformatted and also
    // the formattedKey should be correct and validation should be true
    keyUtil.setKey(KEY_STANDARD_FORMATTED);
    assertEquals(KEY_STANDARD, keyUtil.getKey());
    assertEquals(KEY_STANDARD_FORMATTED, keyUtil.getFormattedKey());
    assertSame(true, keyUtil.isKeyValid());
  }


}

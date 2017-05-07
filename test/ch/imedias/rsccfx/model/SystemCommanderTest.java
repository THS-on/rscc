package ch.imedias.rsccfx.model;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the SystemCommander class.
 */
public class SystemCommanderTest {

  SystemCommander systemCommander;

  /**
   * Initializes test fixture before each test.
   */
  @Before
  public void setUp() throws Exception {
    systemCommander = new SystemCommander();
  }

  /**
   * Test for {@link SystemCommander#executeTerminalCommand(String)}.
   */
  @Test
  public void testExecuteTerminalCommandIllegalArguments() throws Exception {
    try {
      systemCommander.executeTerminalCommand(null);
      fail("IllegalArgumentException was expected when Command is null");
    } catch (Exception e) {
      // expected behavior
    }
  }

  /**
   * Test for {@link SystemCommander#executeTerminalCommand(String)}.
   * Compares method output using standard Linux shell-command "echo".
   */
  @Test
  public void testExecuteTerminalCommand() throws Exception {
    String testTerminalCommand = "testExecuteTerminalCommand";
    StringBuilder command = new StringBuilder("echo ").append(testTerminalCommand);
    String output = systemCommander.executeTerminalCommand(command.toString());
    assertEquals(testTerminalCommand, output);
  }
}

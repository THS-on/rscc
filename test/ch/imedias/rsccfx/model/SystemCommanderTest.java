package ch.imedias.rsccfx.model;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Created by jp on 20/04/17.
 */
public class SystemCommanderTest {

  @Test
  public void testExecuteTerminalCommandIllegalArguments() throws Exception {
    try {
      SystemCommander systemCommander = new SystemCommander();
      systemCommander.executeTerminalCommand(null);
      fail("IllegalArgumentException was expected when Command is null");
    } catch (IllegalArgumentException e) {
      // expected behavior
    }
  }

  @Test
  public void testExecuteTerminalCommand() throws Exception {
    //Tests method with shellcommand "echo"
    String teststring = "testExecuteTerminalCommand";
    StringBuilder command = new StringBuilder("echo ").append(teststring);
    SystemCommander systemCommander = new SystemCommander();
    String output = systemCommander.executeTerminalCommand(command.toString());
    assertEquals(teststring, output);
  }
}
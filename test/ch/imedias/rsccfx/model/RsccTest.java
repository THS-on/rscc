package ch.imedias.rsccfx.model;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ch.imedias.rscc.ProcessExecutor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the rscc model class.
 */
public class RsccTest {
  private static final String KEY = "123456789";

  Rscc model;
  ProcessExecutor mockProcessExecutor;

  @Before
  public void setUp() throws Exception {
    mockProcessExecutor = mock(ProcessExecutor.class);
    model = new Rscc(mockProcessExecutor);
  }

  // Test Constructor


  // Test public keyServerSetup


  // Test private keyServerSetup


  // Test killConnection
  @Test
  public void testKillConnection() throws Exception {
    model.killConnection(KEY);
    verify(mockProcessExecutor).executeScript(eq(true), eq(true),
        argThat(script -> script.startsWith("bash ") && script.endsWith("port_stop.sh")), eq(KEY));
  }

  // Test requestTokenFromServer


  // Test connectToUser


  // Test refreshKey


  // Test executeP2pScript


  @Ignore
  @Test
  public void testRequestTokenFromServer() throws Exception {
    /*
    final String terminalCommandUse = "bash resources/docker-build_p2p/use.sh 86.119.39.89 800";
    final String terminalCommandPortShare =
        "bash resources/docker-build_p2p/port_share.sh --p2p_server=86.119.39.89 --p2p_port=2201"
            + " --compress=yes 4999";
    SystemCommander mockCommander = mock(SystemCommander.class);
    model = new Rscc(mockCommander);

    when(mockCommander.executeTerminalCommand(terminalCommandPortShare)).thenReturn("6a2b9op6bq");

    String key = model.requestTokenFromServer(4999, "86.119.39.89", 2201, 800, true);

    // actual test
    verify(mockCommander).executeTerminalCommand(terminalCommandUse);
    assertEquals("6a2b9op6bq", key);
    */
  }
}

package ch.imedias.rsccfx.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.imedias.rscc.ProcessExecutor;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the rscc model class.
 */
public class RsccTest {
  private static final String KEY = "123456789";
  private static final String KEY_SERVER_IP = "86.119.39.89";
  private static final String KEY_SERVER_HTTP_PORT =  "800";

  Rscc model;
  ProcessExecutor mockProcessExecutor;

  @Before
  public void setUp() throws Exception {
    mockProcessExecutor = mock(ProcessExecutor.class);
    model = new Rscc(mockProcessExecutor);
    model.keyServerSetup(KEY_SERVER_IP,KEY_SERVER_HTTP_PORT);
    when(mockProcessExecutor.getOutput()).thenReturn("OUTPUT>" + KEY);
  }

  // Test Constructor


  // Test public keyServerSetup


  // Test private keyServerSetup
  public void testPrivateKeyServerSetup() throws Exception {
    verify(mockProcessExecutor).executeScript(eq(true), eq(true),
        argThat(script -> script.startsWith("bash ") && script.endsWith("use.sh")), eq(KEY_SERVER_IP), eq(KEY_SERVER_HTTP_PORT));
  }

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



  @Test
  public void testRequestTokenFromServer() throws Exception {
    String key = model.requestTokenFromServer();

    testPrivateKeyServerSetup();
    // make sure the script was executed
    verify(mockProcessExecutor).executeScript(eq(true), eq(true),
        argThat(script -> script.startsWith("bash ") && script.endsWith("start_x11vnc.sh") ), eq(KEY_SERVER_IP), eq(KEY_SERVER_HTTP_PORT));
    // make sure the key which is being returned is right
    assertEquals(KEY, key);
  }


}

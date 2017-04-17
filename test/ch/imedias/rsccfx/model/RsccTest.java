package ch.imedias.rsccfx.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.imedias.rscc.ProcessExecutor;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationsException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the rscc model class.
 */
public class RsccTest {
  private static final String KEY = "123456789";
  private static final String KEY_SERVER_IP = "86.119.39.89";
  private static final String KEY_SERVER_HTTP_PORT = "800";

  Rscc model;
  SystemCommander mockProcessExecutor;

  /**
   * Initializes test fixture before each test.
   */
  @Before
  public void setUp() throws Exception {
    mockProcessExecutor = mock(SystemCommander.class);
    model = new Rscc(mockProcessExecutor);
    model.keyServerSetup(KEY_SERVER_IP, KEY_SERVER_HTTP_PORT);
    when(mockProcessExecutor.executeTerminalCommand(
        argThat(string -> string.contains("start_x11vnc.sh")))).thenReturn(KEY);
  }

  /**
   * Test for Constructor {@link Rscc#Rscc(SystemCommander)}.
   */
  @Test
  public void testRsccConstructorIllegalArguments() {
    try{
      new Rscc(null);
      fail("Test has to fail");
    } catch (IllegalArgumentException e) {
    }
  }

  /**
   * Test for Constructor {@link Rscc#Rscc(SystemCommander)}.
   */
  @Test
  public void testRsccConstructor() {
    try{
      new Rscc(new SystemCommander());
      new Rscc(mockProcessExecutor);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }


  /**
   * Test for {@link Rscc#keyServerSetup(String, String)}.
   */
  @Test
  public void testPublicKeyServerSetup() {
    model.keyServerSetup(KEY_SERVER_IP, KEY_SERVER_HTTP_PORT);
    assertNotNull(model.getKeyServerHttpPort());
    assertNotNull(model.getKeyServerIp());
    assertEquals(model.getKeyServerIp(), KEY_SERVER_IP);
    assertEquals(model.getKeyServerHttpPort(), KEY_SERVER_HTTP_PORT);
  }


  /**
   * Test for {@link Rscc#keyServerSetup()}.
   * Not marked with a @Test annotation because it is indirectly called in other tests.
   */
  public void testPrivateKeyServerSetup() throws Exception {
    verify(mockProcessExecutor).executeTerminalCommand(
        argThat(script -> script.contains("use.sh") &&
            script.contains(KEY_SERVER_IP) &&
            script.contains(KEY_SERVER_HTTP_PORT)));
  }


  /**
   * Test for {@link Rscc#killConnection()}.
   */
  @Test
  public void testKillConnection() throws Exception {
    model.setKey(KEY);
    model.killConnection();
    verify(mockProcessExecutor).executeTerminalCommand(
        argThat(script -> script.contains("port_stop.sh") &&
            script.endsWith(KEY)));
  }


  /**
   * Test for {@link Rscc#requestTokenFromServer()}.
   */
  @Test
  public void testRequestTokenFromServer() throws Exception {
    model.requestTokenFromServer();
    testPrivateKeyServerSetup();
    // make sure the script was executed
    verify(mockProcessExecutor).executeTerminalCommand(
        argThat(script -> script.contains("start_x11vnc.sh")));
    // make sure the key which is being returned is right
    assertEquals(KEY, model.getKey());
  }


  /**
   * Test for {@link Rscc#connectToUser()}.
   */
  @Test
  public void testConnectToUser() throws Exception {
    model.setKey(KEY);
    model.connectToUser();
    // make sure the scripts were executed
    this.testPrivateKeyServerSetup();
    verify(mockProcessExecutor).executeTerminalCommand(
        argThat(script -> script.contains("start_vncviewer.sh") &&
            script.endsWith(KEY)));
  }

  /**
   * Test for {@link Rscc#refreshKey()}.
   */
  @Test
  public void testRefreshKey() {
    model.refreshKey();
    // make sure the scripts were executed
    verify(mockProcessExecutor).executeTerminalCommand(
        argThat(script -> script.contains("port_stop.sh")));
    verify(mockProcessExecutor).executeTerminalCommand(
        argThat(script -> script.contains("start_x11vnc.sh")));
    // make sure the key which is being returned is right
    assertEquals(KEY, model.getKey());
  }
}

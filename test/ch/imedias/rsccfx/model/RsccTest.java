package ch.imedias.rsccfx.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
  SystemCommander mockSystemCommander;

  /**
   * Initializes test fixture before each test.
   */
  @Before
  public void setUp() throws Exception {
    mockSystemCommander = mock(SystemCommander.class);
    model = new Rscc(mockSystemCommander);
    model.setKeyServerIp(KEY_SERVER_IP);
    model.setKeyServerHttpPort(KEY_SERVER_HTTP_PORT);
    when(mockSystemCommander.executeTerminalCommand(
        argThat(string -> string.contains("port_share.sh")))).thenReturn(KEY);
  }

  /**
   * Test for Constructor {@link Rscc#Rscc(SystemCommander)}.
   */
  @Test
  public void testRsccConstructorIllegalArguments() {
    try {
      new Rscc(null);
      fail("IllegalArgumentException was expected when SystemCommander is null");
    } catch (IllegalArgumentException e) {
      // expected behavior
    }
  }

  /**
   * Test for Constructor {@link Rscc#Rscc(SystemCommander)}.
   */
  @Test
  public void testRsccConstructor() {
    try {
      new Rscc(mockSystemCommander);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }


  /**
   * Test for {@link Rscc#keyServerSetup()}.
   * Not marked with a @Test annotation because it is indirectly called in other tests.
   */
  public void testKeyServerSetup() throws Exception {
    verify(mockSystemCommander).executeTerminalCommand(
        argThat(script -> script.contains("use.sh")
            && script.contains(KEY_SERVER_IP)
            && script.contains(KEY_SERVER_HTTP_PORT)));
  }


  /**
   * Test for {@link Rscc#killConnection()}.
   */
  @Test
  public void testKillConnection() throws Exception {
    model.setKey(KEY);
    model.killConnection();
    verify(mockSystemCommander).executeTerminalCommand(
        argThat(script -> script.contains("port_stop.sh")
            && script.endsWith(KEY)));
  }


  /**
   * Test for {@link Rscc#requestKeyFromServer()}.
   */
  @Ignore
  @Test
  public void testRequestKeyFromServer() throws Exception {
    model.requestKeyFromServer();
    testKeyServerSetup();
    // make sure the script was executed
    verify(mockSystemCommander).executeTerminalCommand(
        argThat(script -> script.contains("port_share.sh")));
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
    this.testKeyServerSetup();
    verify(mockSystemCommander).executeTerminalCommand(
        argThat(script -> script.contains("port_connect.sh")
            && script.endsWith(KEY)));
  }

  /**
   * Test for {@link Rscc#refreshKey()}.
   */
  @Ignore
  @Test
  public void testRefreshKey() {
    model.refreshKey();
    // make sure the scripts were executed
    verify(mockSystemCommander).executeTerminalCommand(
        argThat(script -> script.contains("port_stop.sh")));
    verify(mockSystemCommander).executeTerminalCommand(
        argThat(script -> script.contains("port_share.sh")));
    // make sure the key which is being returned is right
    assertEquals(KEY, model.getKey());
  }

  /**
   * Test for {@link Rscc#validateKey(String)}.
   */
  @Test
  public void testValidateKey() {
    final String[] invalidKeys = {"123123", "0", "12345678", "1234567890", "abcdefghi"};
    final String[] validKeys = {"123456789", "000000000", "999999999"};

    assertFalse(model.validateKey(null));

    for (String invalidKey : invalidKeys) {
      assertFalse(model.validateKey(invalidKey));
    }

    for (String validKey : validKeys) {
      assertTrue(model.validateKey(validKey));
    }
  }

  /**
   * Test for {@link Rscc#startVncServer()}.
   */
  @Test
  public void testStartVncServer() {
    model.startVncServer();
    // make sure the scripts were executed
    verify(mockSystemCommander).executeTerminalCommand(
        argThat(script -> script.contains("x11vnc")));
  }

  /**
   * Test for {@link Rscc#startVncViewer(String, Integer)}.
   */
  @Test
  public void testStartVncViewer() {
    String hostAddress = "localhost";
    int vncPort = 5900;
    model.startVncViewer(hostAddress, vncPort);
    // make sure the scripts were executed
    verify(mockSystemCommander).executeTerminalCommand(
        argThat(script -> script.contains("vncviewer")
            && script.contains(hostAddress)));
  }

  /**
   * Test for {@link Rscc#startVncViewer(String, Integer)}.
   */
  @Test
  public void testStartVncViewerIllegalArgument() {
    int vncPort = 5900;
    try {

      model.startVncViewer(null, 5900);
      fail("IllegalArgumentException was expected when HostAddress is null");
    } catch (IllegalArgumentException e) {
      // expected behavior
    }
  }
}

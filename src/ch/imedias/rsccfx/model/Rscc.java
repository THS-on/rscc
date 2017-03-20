package ch.imedias.rsccfx.model;

import ch.imedias.rscc.ProcessExecutor;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Stores the key and keyserver connection details.
 * Handles communication with the keyserver.
 */
public class Rscc {
  /**
   * Points to the "docker-build_p2p" folder inside resources, relative to the build path.
   * Important: Make sure to NOT include a / in the beginning or the end.
   */
  private final String pathToResourceDocker;
  private final String scriptShell;
  private final ProcessExecutor processExecutor;
  private StringProperty key = new SimpleStringProperty();
  private String keyServerIp;
  private String keyServerHttpPort;

  /**
   * Initializes the Rscc model class.
   */
  public Rscc(ProcessExecutor processExecutor) {
    this.processExecutor = processExecutor;
    pathToResourceDocker = getClass().getClassLoader().getResource("docker-build_p2p")
        .getFile().toString().replaceFirst("file:", "");
    scriptShell = "bash" + " " + pathToResourceDocker + "/";
  }

  /**
   * Sets the IP and HTTP port of the keyserver in the model.
   */
  public void keyServerSetup(String keyServerIp, String keyServerHttpPort) {
    this.keyServerIp = keyServerIp;
    this.keyServerHttpPort = keyServerHttpPort;
  }

  private void keyServerSetup() {
    // Setup the server with use.sh
    executeP2pScript("use.sh", keyServerIp, keyServerHttpPort);
  }

  /**
   * Kills the connection to the keyserver.
   */
  public void killConnection(String key) {
    // Execute port_stop.sh with the generated key to kill the connection
    executeP2pScript("port_stop.sh", key);
  }

  /**
   * Requests a token from the key server.
   */
  public String requestTokenFromServer() {
    keyServerSetup();

    // Execute port_share.sh and get a key as output
    String key = executeP2pScript("start_x11vnc.sh", keyServerIp, keyServerHttpPort);
    this.key.set(key); // update key in model
    return key;
  }

  /**
   * Starts connection to the user.
   */
  public void connectToUser(String key) {
    keyServerSetup();

    // Executes start_vncviewer.sh and connects to the user.
    executeP2pScript("start_vncviewer.sh", key);
  }

  /**
   * Refreshes the key by killing the connection, requesting a new key and starting the server
   * again.
   */
  public String refreshKey() {
    killConnection(key.toString());
    return requestTokenFromServer();
  }

  private String executeP2pScript(String fileName, String... parameters) {
    String output = "";
    try {
      processExecutor.executeScript(true, true, scriptShell + fileName, parameters);
    } catch (IOException writingError) {
      System.out.println("script could not be written to a temp file!");
      writingError.printStackTrace();
    }
    output = processExecutor.getOutput();
    output = output.replace("OUTPUT>", "").trim(); // get rid of OUTPUT> in the beginning
    return output;
  }

  public StringProperty keyProperty() {
    return key;
  }

  public String getKey() {
    return key.get();
  }

  public String getKeyServerIp() {
    return keyServerIp;
  }

  public String getKeyServerHttpPort() {
    return keyServerHttpPort;
  }
}

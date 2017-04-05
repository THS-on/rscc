package ch.imedias.rsccfx.model;

import ch.imedias.rscc.ProcessExecutor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
  private static final String DOCKER_FOLDER_NAME = "docker-build_p2p";
  private String pathToResourceDocker;
  private final ProcessExecutor processExecutor;
  private StringProperty key = new SimpleStringProperty();
  private String keyServerIp;
  private String keyServerHttpPort;

  private ClassLoader cl;

  /**
   * Initializes the Rscc model class.
   */
  public Rscc(ProcessExecutor processExecutor) {
    this.processExecutor = processExecutor;

    jarOrNotToJar();

    keyServerSetup("localhost", "800");
    keyServerSetup();
  }

  private void jarOrNotToJar() {
    URL location = this.getClass().getProtectionDomain().getCodeSource().getLocation();
    File f = new File(location.getFile());
    if (f.isDirectory()) {
      pathToResourceDocker =
              getClass().getClassLoader().getResource(DOCKER_FOLDER_NAME)
                      .getFile().toString().replaceFirst("file:", "");

    } else {
      pathToResourceDocker = "/tmp/" + DOCKER_FOLDER_NAME;
      extractJarContents("/tmp/", DOCKER_FOLDER_NAME);
    }

    System.out.println(pathToResourceDocker);
  }

  private void extractJarContents(String destinationDirectory, String filter) {
    URL location = this.getClass().getProtectionDomain().getCodeSource().getLocation();
    JarFile jarfile = null;
    try {
      jarfile = new JarFile(new File(location.getFile()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Enumeration<JarEntry> enu = jarfile.entries();
    while (enu.hasMoreElements()) {
      JarEntry je = enu.nextElement();
      if (je.getName().contains(filter)) {
        System.out.println(je.getName());
        File fl = new File(destinationDirectory, je.getName());
        if (!fl.exists()) {
          fl.getParentFile().mkdirs();
          fl = new File(destinationDirectory, je.getName());
        }
        if (je.isDirectory()) {
          continue;
        }
        try {
          InputStream is = jarfile.getInputStream(je);
          FileOutputStream fo = new FileOutputStream(fl);
          while (is.available() > 0) {
            fo.write(is.read());
          }
          fl.setExecutable(true);
          fo.close();
          is.close();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
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
    //executeP2pScript("", keyServerIp, keyServerHttpPort);
    try {
      Process p = new ProcessBuilder(pathToResourceDocker + "/use.sh",
              keyServerIp, keyServerHttpPort).start();
    } catch (IOException e) {
      e.printStackTrace();
    }

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
      processExecutor.executeScript(true, true, pathToResourceDocker + fileName, parameters);
    } catch (IOException writingError) {
      System.out.println("script could not be written to a temp file!");
      writingError.printStackTrace();
    }
    output = processExecutor.getOutput();
    output = output.replace("OUTPUT>", "").trim(); // get rid of OUTPUT> in the beginning
    System.out.println(output);
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

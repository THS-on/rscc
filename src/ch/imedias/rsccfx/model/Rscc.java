package ch.imedias.rsccfx.model;

import ch.imedias.rsccfx.model.util.KeyUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Stores the key and keyserver connection details.
 * Handles communication with the keyserver.
 */
public class Rscc {
  private static final Logger LOGGER =
      Logger.getLogger(Rscc.class.getName());
  /**
   * Points to the "docker-build_p2p" folder inside resources, relative to the build path.
   * Important: Make sure to NOT include a / in the beginning or the end.
   */
  private static final String DOCKER_FOLDER_NAME = "docker-build_p2p";
  /**
   * sh files can not be executed in the JAR file and therefore must be extracted.
   * ".rscc" is a hidden folder in the user's home directory (e.g. /home/user)
   */
  private static final String RSCC_FOLDER_NAME = ".rscc";
  private static final String STUN_DUMP_FILE_NAME = "ice4jDemoDump.ice";
  private final SystemCommander systemCommander;

  private final StringProperty keyServerIp = new SimpleStringProperty("86.119.39.89");
  private final StringProperty keyServerHttpPort = new SimpleStringProperty("800");
  private final StringProperty vncPort = new SimpleStringProperty("5900");
  private final BooleanProperty vncOptionViewOnly = new SimpleBooleanProperty(false);
  private final BooleanProperty vncOptionWindow = new SimpleBooleanProperty(false);
  private final StringProperty connectionStatusText = new SimpleStringProperty();
  private final StringProperty connectionStatusStyle = new SimpleStringProperty();

  private final String[] connectionStatusStyles = {
      "statusBox", "statusBoxInitialize", "statusBoxSuccess", "statusBoxFail"};

  //TODO: Replace when the StunFileGeneration is ready
  private final String pathToStunDumpFile = this.getClass()
      .getClassLoader().getResource(STUN_DUMP_FILE_NAME)
      .toExternalForm().replace("file:", "");
  private final KeyUtil keyUtil;

  private String pathToResourceDocker;

  /**
   * Initializes the Rscc model class.
   *
   * @param systemCommander a SystemComander-object that executes shell commands.
   * @param keyUtil a KeyUtil-object which stores the key, validates and formats it.
   */
  public Rscc(SystemCommander systemCommander, KeyUtil keyUtil) {
    if (systemCommander == null) {
      throw new IllegalArgumentException("Parameter SystemCommander is NULL");
    }
    if (keyUtil == null) {
      throw new IllegalArgumentException("Parameter KeyUtil is NULL");
    }
    this.systemCommander = systemCommander;
    this.keyUtil = keyUtil;
    defineResourcePath();
    readServerConfig();
  }

  /**
   * Sets resource path, according to the application running either as a JAR or in the IDE.
   */
  private void defineResourcePath() {
    String userHome = System.getProperty("user.home");
    URL theLocationOftheRunningClass = this.getClass().getProtectionDomain()
        .getCodeSource().getLocation();
    File actualClass = new File(theLocationOftheRunningClass.getFile());
    if (actualClass.isDirectory()) {
      pathToResourceDocker =
          getClass().getClassLoader().getResource(DOCKER_FOLDER_NAME)
              .getFile().replaceFirst("file:", "");

    } else {
      pathToResourceDocker = userHome + "/" + RSCC_FOLDER_NAME + "/" + DOCKER_FOLDER_NAME;
      extractJarContents(theLocationOftheRunningClass,
          userHome + "/" + RSCC_FOLDER_NAME, DOCKER_FOLDER_NAME);
    }
  }

  /**
   * Extracts files from running JAR to folder.
   *
   * @param filter filters the files that will be extracted by this string.
   */
  private void extractJarContents(URL sourceLocation, String destinationDirectory, String filter) {
    JarFile jarFile = null;
    try {
      jarFile = new JarFile(new File(sourceLocation.getFile()));
    } catch (IOException e) {
      LOGGER.severe("Exception thrown when trying to get file from: "
          + sourceLocation
          + "\n Exception Message: " + e.getMessage());
    }
    Enumeration<JarEntry> contentList = jarFile.entries();
    while (contentList.hasMoreElements()) {
      JarEntry item = contentList.nextElement();
      if (item.getName().contains(filter)) {
        LOGGER.fine(item.getName());
        File targetFile = new File(destinationDirectory, item.getName());
        if (!targetFile.exists()) {
          targetFile.getParentFile().mkdirs();
          targetFile = new File(destinationDirectory, item.getName());
        }
        if (item.isDirectory()) {
          continue;
        }
        try (
            InputStream fromStream = jarFile.getInputStream(item);
            FileOutputStream toStream = new FileOutputStream(targetFile)
        ) {
          while (fromStream.available() > 0) {
            toStream.write(fromStream.read());
          }

        } catch (FileNotFoundException e) {
          LOGGER.severe("Exception thrown when reading from file: "
              + targetFile.getName()
              + "\n Exception Message: " + e.getMessage());
        } catch (IOException e) {
          LOGGER.severe("Exception thrown when trying to copy jar file contents to local"
              + "\n Exception Message: " + e.getMessage());
        }
        targetFile.setExecutable(true);
      }
    }
  }

  /**
   * Sets up the server with use.sh.
   */
  private void keyServerSetup() {
    String command = systemCommander.commandStringGenerator(
        pathToResourceDocker, "use.sh", getKeyServerIp(), getKeyServerHttpPort());
    systemCommander.executeTerminalCommand(command);
  }

  /**
   * Kills the connection to the keyserver.
   */
  public void killConnection() {
    // Execute port_stop.sh with the generated key to kill the connection
    String command = systemCommander.commandStringGenerator(
        pathToResourceDocker, "port_stop.sh", keyUtil.getKey());
    systemCommander.executeTerminalCommand(command);
    keyUtil.setKey("");
  }

  /**
   * Requests a key from the key server.
   */
  public void requestKeyFromServer() {
    setConnectionStatus("Setting keyserver...", 1);

    keyServerSetup();

    setConnectionStatus("Requesting key from server...", 1);

    String command = systemCommander.commandStringGenerator(
        pathToResourceDocker, "port_share.sh", getVncPort(), pathToStunDumpFile);
    String key = systemCommander.executeTerminalCommand(command);

    setConnectionStatus("Starting VNC-Server...", 1);
    keyUtil.setKey(key); // update key in model
    startVncServer();
    setConnectionStatus("VNC-Server awaits connection", 2);
  }

  /**
   * Sets the Status of the connection establishment.
   *
   * @param text             Text to show for the connection status.
   * @param statusStyleIndex Index of the connectionStatusStyles.
   */
  public void setConnectionStatus(String text, int statusStyleIndex) {
    if (statusStyleIndex < 0 || statusStyleIndex >= connectionStatusStyles.length || text == null) {
      throw new IllegalArgumentException();
    }
    setConnectionStatusText(text);
    setConnectionStatusStyle(getConnectionStatusStyles(statusStyleIndex));
  }

  /**
   * Starts connection to the user.
   */
  public void connectToUser() {
    setConnectionStatus("Setting keyserver...", 1);

    keyServerSetup();
    String command = systemCommander.commandStringGenerator(pathToResourceDocker,
        "port_connect.sh", getVncPort(), keyUtil.getKey());

    setConnectionStatus("Connect to keyserver...", 1);

    systemCommander.executeTerminalCommand(command);

    setConnectionStatus("Starting VNC-Viewer...", 1);

    startVncViewer("localhost");

    setConnectionStatus("Connection Established", 2);
  }

  /**
   * Starts the VNC Server.
   */
  public void startVncServer() {
    StringBuilder vncServerAttributes = new StringBuilder("-bg -nopw -q -localhost");

    if (getVncOptionViewOnly()) {
      vncServerAttributes.append(" -viewonly");
    }
    if (isVncOptionWindow()) {
      vncServerAttributes.append(" -sid pick");
    }
    vncServerAttributes.append(" -rfbport ").append(getVncPort());

    String command = systemCommander.commandStringGenerator(null,
        "x11vnc", vncServerAttributes.toString());
    systemCommander.executeTerminalCommand(command);
  }

  /**
   * Starts the VNC Viewer.
   */
  public void startVncViewer(String hostAddress) {
    if (hostAddress == null) {
      throw new IllegalArgumentException();
    }
    String vncViewerAttributes = "-encodings copyrect " + " " + hostAddress;
    //TODO: Encodings are missing: "tight zrle hextile""

    String command = systemCommander.commandStringGenerator(null,
        "vncviewer", vncViewerAttributes);
    systemCommander.executeTerminalCommand(command);
  }

  /**
   * Refreshes the key by killing the connection, requesting a new key and starting the server
   * again.
   */
  public void refreshKey() {
    setConnectionStatus("Refreshing key...", 1);
    killConnection();
    requestKeyFromServer();
  }

  /**
   * Reads the docker server configuration from file ssh.rc under "/pathToResourceDocker".
   */
  private void readServerConfig() {
    String configFilePath = pathToResourceDocker + "/ssh.rc";
    try {
      List<String> lines = Files.readAllLines(Paths.get(configFilePath), Charset.forName("UTF-8"));
      for (String line : lines) {
        if (line.contains("p2p_server=") && !line.endsWith("=")) {
          setKeyServerIp(line.split("=")[1]);
        } else if (line.contains("http_port=") && !line.endsWith("=")) {
          setKeyServerHttpPort(line.split("=")[1]);
        }
      }
      LOGGER.fine("Set serverIP to: " + getKeyServerIp()
          + "\n Set serverHTTP-port to: " + getKeyServerHttpPort());
    } catch (IOException e) {
      LOGGER.severe("Exception thrown when reading from file: "
          + configFilePath
          + "\n Exception Message: " + e.getMessage());
    }
  }

  public String getKeyServerIp() {
    return keyServerIp.get();
  }

  public StringProperty keyServerIpProperty() {
    return keyServerIp;
  }

  public void setKeyServerIp(String keyServerIp) {
    this.keyServerIp.set(keyServerIp);
  }

  public String getKeyServerHttpPort() {
    return keyServerHttpPort.get();
  }

  public StringProperty keyServerHttpPortProperty() {
    return keyServerHttpPort;
  }

  public void setKeyServerHttpPort(String keyServerHttpPort) {
    this.keyServerHttpPort.set(keyServerHttpPort);
  }

  public String getVncPort() {
    return vncPort.get();
  }

  public StringProperty vncPortProperty() {
    return vncPort;
  }

  public void setVncPort(String vncPort) {
    this.vncPort.set(vncPort);
  }

  public boolean getVncOptionViewOnly() {
    return vncOptionViewOnly.get();
  }

  public BooleanProperty vncOptionViewOnlyProperty() {
    return vncOptionViewOnly;
  }

  public void setVncOptionViewOnly(boolean vncOptionViewOnly) {
    this.vncOptionViewOnly.set(vncOptionViewOnly);
  }

  public boolean isVncOptionWindow() {
    return vncOptionWindow.get();
  }

  public BooleanProperty vncOptionWindowProperty() {
    return vncOptionWindow;
  }

  public void setVncOptionWindow(boolean vncOptionWindow) {
    this.vncOptionWindow.set(vncOptionWindow);
  }

  public KeyUtil getKeyUtil() {
    return keyUtil;
  }

  public String getConnectionStatusText() {
    return connectionStatusText.get();
  }

  public StringProperty connectionStatusTextProperty() {
    return connectionStatusText;
  }

  public void setConnectionStatusText(String connectionStatusText) {
    this.connectionStatusText.set(connectionStatusText);
  }

  public String getConnectionStatusStyle() {
    return connectionStatusStyle.get();
  }

  public StringProperty connectionStatusStyleProperty() {
    return connectionStatusStyle;
  }

  public void setConnectionStatusStyle(String connectionStatusStyle) {
    this.connectionStatusStyle.set(connectionStatusStyle);
  }

  public String getConnectionStatusStyles(int i) {
    return connectionStatusStyles[i];
  }
}

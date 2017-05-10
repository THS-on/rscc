package ch.imedias.rsccfx.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

  private final StringProperty key = new SimpleStringProperty();
  private final StringProperty keyServerIp = new SimpleStringProperty("86.119.39.89");
  private final StringProperty keyServerHttpPort = new SimpleStringProperty("800");
  private final StringProperty vncPort = new SimpleStringProperty("5900");
  private final BooleanProperty vncOptionViewOnly = new SimpleBooleanProperty();
  private final DoubleProperty vncOptionQualitySliderValue = new SimpleDoubleProperty();
  private final DoubleProperty vncOptionCompressionSliderValue = new SimpleDoubleProperty();
  private final BooleanProperty vncOptionBgr233 = new SimpleBooleanProperty();
  private final BooleanProperty vncOptionWindow = new SimpleBooleanProperty(false);

  //TODO: Replace when the StunFileGeneration is ready
  private final String pathToStunDumpFile = this.getClass()
      .getClassLoader().getResource(STUN_DUMP_FILE_NAME)
      .toExternalForm().replace("file:", "");

  private String pathToResourceDocker;

  /**
   * Initializes the Rscc model class.
   *
   * @param systemCommander a SystemComander-object that executes shell commands.
   */
  public Rscc(SystemCommander systemCommander) {
    if (systemCommander == null) {
      throw new IllegalArgumentException("Parameter SystemCommander is NULL");
    }
    this.systemCommander = systemCommander;
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
        System.out.println(item.getName());
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
    String command = commandStringGenerator(
        pathToResourceDocker, "use.sh", getKeyServerIp(), getKeyServerHttpPort());
    systemCommander.executeTerminalCommand(command);
  }

  /**
   * Kills the connection to the keyserver.
   */
  public void killConnection() {
    // Execute port_stop.sh with the generated key to kill the connection
    String command = commandStringGenerator(pathToResourceDocker, "port_stop.sh", getKey());
    systemCommander.executeTerminalCommand(command);
    setKey("");
  }

  public void stopVncServer(){
    String command = commandStringGenerator(null, "killall", "x11vnc");
    systemCommander.executeTerminalCommand(command);

  }

  /**
   * Requests a key from the key server.
   */
  public void requestKeyFromServer() {
    keyServerSetup();

    String command = commandStringGenerator(
        pathToResourceDocker, "port_share.sh", getVncPort(), pathToStunDumpFile);
    String key = systemCommander.executeTerminalCommand(command);
    setKey(key); // update key in model
    startVncServer();
  }

  /**
   * Starts connection to the user.
   */
  public void connectToUser() {
    keyServerSetup();
    String command = commandStringGenerator(pathToResourceDocker,
        "port_connect.sh", getVncPort(), getKey(), getSupporterSettings());

    systemCommander.executeTerminalCommand(command);
    startVncViewer("localhost");
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

    String command = commandStringGenerator(null,
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

    String command = commandStringGenerator(null,
        "vncviewer", vncViewerAttributes);
    systemCommander.executeTerminalCommand(command);
  }


  /**
   * Refreshes the key by killing the connection, requesting a new key and starting the server
   * again.
   */
  public void refreshKey() {
    killConnection();
    requestKeyFromServer();
  }

  /**
   * Generates String to run command.
   */
  private String commandStringGenerator(
      String pathToScript, String scriptName, String... attributes) {
    StringBuilder commandString = new StringBuilder();

    if (pathToScript != null) {
      commandString.append(pathToScript).append("/");
    }
    commandString.append(scriptName);
    Arrays.stream(attributes)
        .forEach((s) -> commandString.append(" ").append(s));

    return commandString.toString();
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

  private String getSupporterSettings() {
    StringBuilder settings = new StringBuilder();

    settings.append(" -quality ");
    settings.append(vncOptionQualitySliderValue.get());

    return  settings.toString();
  }

  /**
   * Determines if a key is valid or not.
   * The key must not be null and must be a number with exactly 9 digits.
   *
   * @param key the string to validate.
   * @return true when key has a valid format.
   */
  public boolean validateKey(String key) {
    return key != null && key.matches("\\d{9}");
  }

  public StringProperty keyProperty() {
    return key;
  }

  public String getKey() {
    return key.get();
  }

  public void setKey(String key) {
    this.key.set(key);
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

  public double getVncOptionQualitySliderValue() {
    return vncOptionQualitySliderValue.get();
  }

  public DoubleProperty vncOptionQualitySliderValueProperty() {
    return vncOptionQualitySliderValue;
  }

  public void setVncOptionQualitySliderValue(double vncOptionQualitySliderValue) {
    this.vncOptionQualitySliderValue.set(vncOptionQualitySliderValue);
  }

  public boolean isVncOptionViewOnly() {
    return vncOptionViewOnly.get();
  }

  public double getVncOptionCompressionSliderValue() {
    return vncOptionCompressionSliderValue.get();
  }

  public DoubleProperty vncOptionCompressionSliderValueProperty() {
    return vncOptionCompressionSliderValue;
  }

  public void setVncOptionCompressionSliderValue(double vncOptionCompressionSliderValue) {
    this.vncOptionCompressionSliderValue.set(vncOptionCompressionSliderValue);
  }

  public boolean isVncOptionBgr233() {
    return vncOptionBgr233.get();
  }

  public BooleanProperty vncOptionBgr233Property() {
    return vncOptionBgr233;
  }

  public void setVncOptionBgr233(boolean vncOptionBgr233) {
    this.vncOptionBgr233.set(vncOptionBgr233);
  }
}

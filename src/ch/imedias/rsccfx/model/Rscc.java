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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
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
  private String pathToResourceDocker;
  private final StringProperty keyServerIp = new SimpleStringProperty("86.119.39.89");
  private final StringProperty keyServerHttpPort = new SimpleStringProperty("800");
  //TODO: Replace when the StunFileGeneration is ready
  private final String pathToStunDumpFile = this.getClass()
          .getClassLoader().getResource(STUN_DUMP_FILE_NAME)
          .toExternalForm().replace("file:","");
  private final KeyUtil keyUtil;

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
    String command = commandStringGenerator(pathToResourceDocker, "port_stop.sh", keyUtil.getKey());
    systemCommander.executeTerminalCommand(command);
    keyUtil.setKey("");
  }

  /**
   * Requests a key from the key server.
   */
  public void requestKeyFromServer() {
    keyServerSetup();

    String command = commandStringGenerator(
        pathToResourceDocker, "start_x11vnc.sh", pathToStunDumpFile);
    String key = systemCommander.executeTerminalCommand(command);
    keyUtil.setKey(key); // update key in model
  }

  /**
   * Starts connection to the user.
   */
  public void connectToUser() {
    keyServerSetup();

    String command = commandStringGenerator(pathToResourceDocker, "start_vncviewer.sh",
        keyUtil.getKey());
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

    commandString.append(pathToScript).append("/").append(scriptName);
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

  public KeyUtil getKeyUtil() {
    return keyUtil;
  }
}

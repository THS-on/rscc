package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Rscc {
  /**
   * Points to the "docker-build_p2p" folder inside resources, relative to the build path.
   * Important: Make sure to NOT include a / in the beginning or the end.
   */
  private static final String PATH_TO_RESOURCE_DOCKER = "resources/docker-build_p2p";

  public Rscc() {
    //TODO required constructor(s)
  }

  /**
   * Requests a token from the key server.
   */
  public String requestTokenFromServer(int forwardingPort, String keyServerIp, int keyServerSshPort,
                                       int keyServerHttpPort, boolean isCompressionEnabled) {
    StringBuilder command = new StringBuilder();

    // First, setup the server with use.sh
    command.append("bash" + " " + PATH_TO_RESOURCE_DOCKER + "/");
    command.append("use.sh" + " ");
    command.append(keyServerIp + " ");
    command.append(keyServerHttpPort);
    executeTerminalCommand(command.toString());

    command = new StringBuilder();
    // Execute port_share.sh and get a key as output
    command.append("bash" + " " + PATH_TO_RESOURCE_DOCKER + "/");
    command.append("port_share.sh" + " ");
    command.append("--p2p_server=" + keyServerIp + " ");
    command.append("--p2p_port=" + keyServerSshPort + " ");
    command.append("--compress=" + (isCompressionEnabled ? "yes" : "no") + " ");
    command.append(forwardingPort);

    return executeTerminalCommand(command.toString());
  }

  /**
   * Kills the connection to the keyserver.
   */
  public void killConnection(String key) {

    StringBuilder command = new StringBuilder();

    // Execute port_stop.sh with the generated key to kill the connection
    command.append("bash" + " " + PATH_TO_RESOURCE_DOCKER + "/");
    command.append("port_stop.sh" + " ");
    command.append(key);

    System.out.println(command.toString());

    executeTerminalCommand(command.toString());
  }

  private String executeTerminalCommand(String command) {
    Process process;

    try {
      StringBuffer output = new StringBuffer();
      // Execute Command
      process = Runtime.getRuntime().exec(command);
      process.waitFor();
      // read the output from the command
      BufferedReader outputReader = new BufferedReader(new
          InputStreamReader(process.getInputStream()));
      String line = null;
      while ((line = outputReader.readLine()) != null) {
        output.append(line + "\n");
      }
      return output.toString().trim();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return "";
  }

}

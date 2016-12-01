package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Rscc {
  private static final String PATH_TO_RESOURCE_DOCKER = "resources/docker-build_p2p";

  public Rscc() {
    //TODO required constructor(s)
  }

  /** Requests a token from the key server. */
  public String requestTokenFromServer(int forwardingPort, String keyServerIp, int keyServerSshPort, int keyServerHttpPort ,boolean isCompressionEnabled){
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
    command.append("--p2p_port=" + keyServerSshPort +" ");
    command.append("--compress=" + (isCompressionEnabled ? "yes" : "no") + " ");
    command.append(forwardingPort);

    return executeTerminalCommand(command.toString());
  }

  private String executeTerminalCommand(String command) {
    Process p;

    try {
      StringBuffer output = new StringBuffer();
      // Execute Command
      p = Runtime.getRuntime().exec(command);
      p.waitFor();
      // read the output from the command
      BufferedReader stdInput = new BufferedReader(new
              InputStreamReader(p.getInputStream()));
      String line = null;
      while ((line = stdInput.readLine()) != null) {
        output.append(line + "\n");
      }
      return output.toString().trim();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

}

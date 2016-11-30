package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Rscc {
  public Rscc() {
    //TODO required constructor(s)
  }


  /** Requests a token from the key server. */

  public String requestTokenFromServer(int forwardingPort, String keyServerIp, int keyServerPort,boolean isCompressionEnabled){
    String currentPath = (new File("")).getAbsolutePath();
    String workingPath = currentPath + "/resources/docker-build_p2p/";
    System.out.println(workingPath);
    File workingDirectory = new File(workingPath);

    StringBuilder command = new StringBuilder();
    // execute first command to import key server setup

    command.append("port_share.sh" + " ");
    command.append("--p2p_server=" + keyServerIp + " ");
    command.append("--p2p_port=" + keyServerPort +" ");
    command.append("--compress=" + (isCompressionEnabled ? "yes" : "no") + " ");
    command.append(forwardingPort);

    System.out.println(command.toString());

    String key = executeTerminalCommand(workingDirectory, command.toString());



    return key;
  }

  private String executeTerminalCommand(File directory, String command) {
    StringBuffer output = new StringBuffer();
    Process p;
    ProcessBuilder processBuilder = new ProcessBuilder();

    try {
      // Execute Command
      processBuilder.directory(directory);
      processBuilder.command(command);
      p = processBuilder.start();
      p.waitFor();

      // Gather output from Terminal
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line = "";
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output.toString();
  }

}

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
    StringBuilder command = new StringBuilder();
    // execute first command to import key server setup

    command.append(currentPath + "/" + "port_share.sh" + " ");
    command.append("--p2p_server=" + keyServerIp + " ");
    command.append("--p2p_port=" + keyServerPort +" ");
    command.append("--compress=" + (isCompressionEnabled ? "yes" : "no") + " ");
    command.append(forwardingPort);

    String key = executeTerminalCommand(command.toString());

    System.out.println(key);

    return key;
  }

  private String executeTerminalCommand(String command) {
    StringBuffer output = new StringBuffer();
    Process p;

    try {
      // Execute Command
      p = Runtime.getRuntime().exec(command);
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

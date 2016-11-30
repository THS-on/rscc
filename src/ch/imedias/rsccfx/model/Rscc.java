package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Rscc {
  public Rscc() {
    //TODO required constructor(s)
  }


  /** Requests a token from the key server. */

  public String requestTokenFromServer(int forwardingPort, String keyServerIp, int keyServerPort,boolean isCompressionEnabled){
    // generate key/create.key




    // generate tempfile


    //


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

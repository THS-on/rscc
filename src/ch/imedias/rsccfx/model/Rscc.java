package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Rscc {
  public Rscc() {
    //TODO required constructor(s)

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

package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class SystemCommander {
  private static final Logger LOGGER =
      Logger.getLogger(SystemCommander.class.getName());

  /**
   * Executes a command in the Linux terminal.
   * More detailed instructions concerning this command can be found here:
   * https://www.cs.technik.fhnw.ch/confluence16/pages/viewpage.action?pageId=6929334
   *
   * @param command terminal command to be executed.
   * @return String trimmed output of the terminal without whitespaces at beginning / end.
   */
  public String executeTerminalCommand(String command) {
    Process process;
    String outputString = ""; // standard return value
    try {
      StringBuilder output = new StringBuilder();
      // Execute Command
      process = Runtime.getRuntime().exec(command);
      process.waitFor();
      // read the output from the command
      BufferedReader outputReader = new BufferedReader(new
          InputStreamReader(process.getInputStream()));
      String line;
      while ((line = outputReader.readLine()) != null) {
        output.append(line).append("\n");
      }
      outputReader.close();
      outputString = output.toString().trim();
    } catch (Exception exception) {
      LOGGER.severe("Exception thrown when running the command: "
          + command
          + "\n Exception Message: " + exception.getMessage());
      if(command == null){
        throw new IllegalArgumentException();
      }
    }
    return outputString;
  }

}

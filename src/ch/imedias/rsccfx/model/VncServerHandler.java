package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * This Class handles a VNC Server.
 * The Thread keeps running as long as the VNCServer is running.
 * Created by jp on 11/05/17.
 */
public class VncServerHandler {
  private static final Logger LOGGER =
      Logger.getLogger(VncServerHandler.class.getName());
  private final Rscc model;
  private final String vncServerName = "x11vnc";
  private BooleanProperty isRunning = new SimpleBooleanProperty(false);
  private String hostAddress;
  private Integer vncViewerPort;
  private boolean reverseMode;
  private Process process;


  /**
   * Constructor to instantiate a VNCViewer.
   *
   * @param model The one and only Model.
   */
  public VncServerHandler(Rscc model) {
    this.model = model;
  }


  public void config(boolean isReverse, String hostAddress,
                     Integer vncViewerPort) {
    this.reverseMode = reverseMode;
    this.hostAddress = hostAddress;
    this.vncViewerPort = vncViewerPort;
  }


  /**
   * Starts the VNCServer in the given mode (Reverse or normal).
   */
  public void startVncServer() {
    if (reverseMode) {
     // startVncServerReverse();
    } else {
      startVncServerListening();
    }
  }


  /**
   * Starts this VNCServer listening on localhost.
   */
  public void startVncServerListening() {

    StringBuilder output = new StringBuilder();
    String whatTerminalNeedsToShow = "connection from client";


    Thread startServerProcessThread = new Thread() {
      public void run() {
        isRunning.set(true);
        try {
          process = Runtime.getRuntime().exec("x11vnc -once");

        } catch (IOException e) {
          e.printStackTrace();
        }
        isRunning.set(false);
      }
    };
    startServerProcessThread.start();


    Thread listenForOutputThread = new Thread() {
      public void run() {
        final InputStream errorStream = process.getErrorStream();
        final InputStream inputStream = process.getInputStream();

        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        try {
          while ((line = errorReader.readLine()) != null)
          {
            if (line.contains(whatTerminalNeedsToShow)) {
             // model.setIsVncSessionRunning(true);
              System.out.println("true");
            }
            output.append(line);
          }
          while ((line = inputReader.readLine()) != null)

          {
            if (line.contains(whatTerminalNeedsToShow)) {
             // model.setIsVncSessionRunning(true);
              System.out.println("true");

            }
            output.append(line);
          }

        } catch (IOException e) {
          System.out.println(e);
        }
      }
    };

    listenForOutputThread.start();


  }



  public void killVncServer() {
    process.destroy();
  }


/*


  private void startVncServerReverse() {
    StringBuilder vncServerAttributes = new StringBuilder(
        "-connect " + hostAddress + ":" + vncViewerPort);

    String command = systemCommander.commandStringGenerator(null,
        vncServerName, vncServerAttributes.toString());
    isRunning.setValue(true);
    systemCommander.startProcessAndUpdate(command,
        "OK", model.isVncSessionRunningProperty(), vncServerPid);
    //"OK"
    isRunning.setValue(false);
  }






  public boolean isRunning() {
    return isRunning.get();
  }

  public BooleanProperty isRunningProperty() {
    return isRunning;
  }

  public void setIsRunning(boolean isRunning) {
    this.isRunning.set(isRunning);
  }
*/

}

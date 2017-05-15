package ch.imedias.rsccfx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;


/**
 * This Class handles a VNC Viewer.
 * Created by jp on 11/05/17.
 */
public class VncViewerHandler {
  private static final Logger LOGGER =
      Logger.getLogger(VncViewerHandler.class.getName());
  private final Rscc model;
  private final String vncViewerName = "vncviewer";
  private Process process;

  /**
   * Constructor to instantiate a VNCViewer.
   *
   * @param model The one and only model.
   */
  public VncViewerHandler(Rscc model) {
    this.model = model;
  }


  /**
   * Starts VNC Viewer and tries to connect to a Server. (active connecting mode)
   * Thread lives as long as connection is established.
   *
   * @param hostAddress Address to connect to.
   * @param vncViewerPort Port to connect to.
   * @return true
   */
  public boolean startVncViewerConnecting(String hostAddress, Integer vncViewerPort) {
    final BooleanProperty connectionSucceed = new SimpleBooleanProperty(true);
    Thread startViewerProcessThread = new Thread() {
      public void run() {
        try {
          LOGGER.info("Starting VNC Viewer Connection");

          process = Runtime.getRuntime().exec(
              vncViewerName +" "+ hostAddress + "::" + vncViewerPort);
          model.setIsVncViewerRunning(true);

          InputStream errorStream = process.getErrorStream();
          BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
          String errorString;
          while (process.isAlive()) {
            errorString = errorReader.readLine();

            if (errorString != null && errorString.contains("Connection refused")) {
              LOGGER.info("Detected: Viewer failed to connect");
              connectionSucceed.setValue(false);
              killVncViewerProcess();
            }

            if (errorString != null && errorString.contains("Connected to RFB server")) {
              LOGGER.info("Detected: Viewer connected sucessfully");
              connectionSucceed.setValue(true);
              model.setIsVncSessionRunning(true);
            }
          }

          LOGGER.info("VNC - Viewer process has ended");
          model.setIsVncSessionRunning(false);
          model.setIsVncViewerRunning(false);

          errorStream.close();

        } catch (IOException e) {
          e.getStackTrace();
        }

        LOGGER.info("Ending VNC Server Thread ");
      }
    };

    startViewerProcessThread.start();
    //TODO
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return connectionSucceed.getValue();
  }


  /**
   * Starts this VNCViewer listening on localhost.
   */
  public void startVncViewerListening() {

    Thread startViewerProcessThread = new Thread() {
      public void run() {
        LOGGER.info("Starting VNC Viewer listening Thread ");
        model.setIsVncViewerRunning(true);
        try {
          process = Runtime.getRuntime().exec("vncviewer -listen");

          InputStream errorStream = process.getErrorStream();
          BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
          String errorString;

          while (process.isAlive()) {
            errorString = errorReader.readLine();

            if (errorString != null && errorString.contains("Connected to RFB server")) {
              LOGGER.info("Server has connected");
              model.setIsVncSessionRunning(true);
            }

          }

          LOGGER.info("VNC - Viewer process has ended");
          errorStream.close();
          model.setIsVncSessionRunning(false);
          model.setIsVncViewerRunning(false);

        } catch (IOException e) {
          e.getStackTrace();
        }
      }
    };
    startViewerProcessThread.start();
  }


  public void killVncViewerProcess() {
    LOGGER.info("Stopping VNC-Viewer Process");
    process.destroy();
  }

}

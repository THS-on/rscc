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
 * Created by jp on 11/05/17.
 */
public class VncServerHandler {
  private static final Logger LOGGER =
      Logger.getLogger(VncServerHandler.class.getName());
  private final Rscc model;
  private final String vncServerName = "x11vnc";
  private BooleanProperty isRunning = new SimpleBooleanProperty(false);
  private Process process;

  /**
   * Constructor to instantiate a VNCServer.
   *
   * @param model The one and only Model.
   */
  public VncServerHandler(Rscc model) {
    this.model = model;
  }


  /**
   * Starts VNC Server in Reverse Mode.
   * Thread Live as long as connection is established.
   *
   * @param hostAddress Address to connect to.
   * @param vncViewerPort Port to connect to.
   * @return true
   */
  public boolean startVncServerReverse(String hostAddress, Integer vncViewerPort) {
    final BooleanProperty connectionSucceed = new SimpleBooleanProperty(true);
    Thread startServerProcessThread = new Thread() {
      public void run() {
        try {
          LOGGER.info("Starting VNC Server Reverse Thread");

          process = Runtime.getRuntime().exec(
              vncServerName + " -connect " + hostAddress + ":" + vncViewerPort);
          isRunning.set(true);

          InputStream errorStream = process.getErrorStream();
          BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
          String errorString;
          while (process.isAlive()) {
            errorString = errorReader.readLine();

            if (errorString != null && errorString.contains("failed to connect")) {
              LOGGER.info("Detected: failed to connect");
              connectionSucceed.setValue(false);
              killVncServerProcess();
            }

            if (errorString != null && errorString.contains("reverse_connect") && errorString.contains("OK")) {
              LOGGER.info("Detected: Reverse connect OK");
              connectionSucceed.setValue(true);
              model.setIsVncSessionRunning(true);
            }
          }

          LOGGER.info("VNC - Server process has ended");
          model.setIsVncSessionRunning(false);
          isRunning.set(false);

          errorStream.close();

        } catch (IOException e) {
          e.getStackTrace();
        }

        LOGGER.info("Ending VNC Server Thread ");
      }
    };

    startServerProcessThread.start();

    try {
      startServerProcessThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return connectionSucceed.getValue();
  }


  /**
   * Starts this VNCServer listening on localhost.
   */
  public void startVncServerListening() {

    Thread startServerProcessThread = new Thread() {
      public void run() {
        LOGGER.info("Starting VNC Server Thread");
        isRunning.set(true);
        try {
          process = Runtime.getRuntime().exec("x11vnc -localhost");

          InputStream errorStream = process.getErrorStream();
          BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
          String errorString;

          while (process.isAlive()) {
            errorString = errorReader.readLine();

            if (errorString != null && errorString.contains("connection from client")) {
              LOGGER.info("Client has connected");
              model.setIsVncSessionRunning(true);
            }

          }

          LOGGER.info("VNC - Server process has ended");
          errorStream.close();
          model.setIsVncSessionRunning(false);
          isRunning.set(false);

        } catch (IOException e) {
          e.getStackTrace();
        }
      }
    };
    startServerProcessThread.start();
  }


  public void killVncServerProcess() {
    LOGGER.info("Stopping VNC-Server Process");
    process.destroy();
  }


  public boolean isRunning() {
    return isRunning.get();
  }

  public BooleanProperty isRunningProperty() {
    return isRunning;
  }
}

package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


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
   * @param hostAddress   Address to connect to.
   * @param vncViewerPort Port to connect to.
   * @return true
   */
  public boolean startVncViewerConnecting(String hostAddress, Integer vncViewerPort) {
    final BooleanProperty connectionSucceed = new SimpleBooleanProperty(true);
    Thread startViewerProcessThread = new Thread() {
      public void run() {
        try {
          LOGGER.info("Starting VNC Viewer Connection");

          String[] commandArray = {
              "vncviewer",

              "-compresslevel",
              Integer.toString((int) model.getVncCompression()),
              "-quality",
              Integer.toString((int) model.getVncQuality()), hostAddress + "::" + vncViewerPort,
              // (model.getVncBgr233() ? "-bgr233" : "")
          };

          System.out.println(Arrays.toString(commandArray));
          System.out.println("vncviewer 127.0.0.1::5900");
          try {
            Thread.sleep(4000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          process = Runtime.getRuntime().exec("vncviewer 127.0.0.1::5900");
          try {
            process.waitFor();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          model.setVncViewerProcessRunning(true);

          InputStream errorStream = process.getErrorStream();
          BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
          String errorString;
          while (process.isAlive()) {
            errorString = errorReader.readLine();
            System.out.println(errorString);

            //TODO -> readline!= null

            if (errorString != null && errorString.contains("Connection refused")) {
              LOGGER.info("Detected: Viewer failed to connect");
              connectionSucceed.setValue(false);
              killVncViewerProcess();
            }

            if (errorString != null && errorString.contains("Connected to RFB server")) {
              LOGGER.info("Detected: Viewer connected sucessfully");
              connectionSucceed.setValue(true);
              model.setVncSessionRunning(true);
            }
          }

          LOGGER.info("VNC - Viewer process has ended");
          model.setVncSessionRunning(false);
          model.setVncViewerProcessRunning(false);

          errorStream.close();

        } catch (IOException e) {
          e.getStackTrace();
        }

        LOGGER.info("Ending VNC Server Thread ");
      }
    };

    startViewerProcessThread.start();

    //TODO: find alternative for sleepingmode
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
        model.setVncViewerProcessRunning(true);
        try {
          String[] commandArray = {
              "xtightvncviewer",
              "-listen",
              "-compresslevel",
              Integer.toString((int) model.getVncCompression()),
              "-quality",
              Integer.toString((int) model.getVncQuality()),
              (model.getVncBgr233() ? "-bgr233" : "")
          };

          process = Runtime.getRuntime().exec(commandArray);

          InputStream errorStream = process.getErrorStream();
          BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
          String errorString;

          while (process.isAlive()) {
            errorString = errorReader.readLine();

            if (errorString != null && errorString.contains("Connected to RFB server")) {
              LOGGER.info("Server has connected");
              model.setVncSessionRunning(true);
            }
          }

          model.setVncViewerProcessRunning(false);
          model.setVncSessionRunning(false);
          errorStream.close();
          LOGGER.info("VNC - Viewer process has ended");

        } catch (IOException e) {
          e.getStackTrace();
        }
      }
    };
    startViewerProcessThread.start();
  }

  /**
   * Kills the VNC Viewer process.
   */

  public void killVncViewerProcess() {
    LOGGER.info("Stopping VNC-Viewer Process");
    process.destroy();
    model.setVncViewerProcessRunning(false);
    model.setVncSessionRunning(false);
  }

}

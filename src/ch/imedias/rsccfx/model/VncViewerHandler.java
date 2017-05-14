package ch.imedias.rsccfx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import java.util.logging.Logger;

/**
 * This Class handles a VNC viewer.
 * The Thread keeps running as long as the VNCViewer is running.
 * Created by jp on 11/05/17.
 */
public class VncViewerHandler extends Thread {
  private static final Logger LOGGER =
      Logger.getLogger(VncViewerHandler.class.getName());
  private final SystemCommander systemCommander;
  private final Rscc model;
  private final String vncViewerName = "vncviewer";
  private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
  private LongProperty vncViewerPid = new SimpleLongProperty(-1);
  private String hostAddress;
  private Integer vncViewerPort;
  private boolean listeningMode;

  /**
   * Constructor to instantiate a VNCViewer.
   * @param model The one and only Model.
   * @param hostAddress Address to connect to.
   * @param vncViewerPort Port to connect to.
   */
  public VncViewerHandler(Rscc model, String hostAddress,
                          Integer vncViewerPort, boolean listeningMode) {

    this.listeningMode = listeningMode;
    this.model = model;
    this.hostAddress = hostAddress;
    this.vncViewerPort = vncViewerPort;
    this.systemCommander = model.getSystemCommander();
  }


  /**
   * Starts the VNCViewer in the given mode (Reverse or normal).
   */
  public void run() {
    if (listeningMode) {
      startVncViewerReverse();
    } else {
      startVncViewer();
    }
  }


  /**
   * Starts this VNCViewer connecting to <code>hostAddress</code> and <code>vncViewerPort</code>.
   * Loops until the VNCViewer is connected to the VNCServer.
   */
  private void startVncViewer() {
    String vncViewerAttributes = "-bgr233 " + " " + hostAddress + "::" + vncViewerPort;

    String command = systemCommander.commandStringGenerator(null,
        vncViewerName, vncViewerAttributes);

    String connectionStatus = null;

    int cycle = 0;
    do {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      connectionStatus = systemCommander.startProcessAndUpdate(
          command, "Connected",model.isVncSessionRunningProperty(), vncViewerPid);
      LOGGER.info("VNCviewer: " + connectionStatus);
      cycle++;
    } while (!connectionStatus.contains("Connected") && cycle < 8);

  }


  /**
   * Starts this VNCViewer in Listening mode.
   */
  private void startVncViewerReverse() {

    //Correct weird vncviewer behaviour: it adds the portnumber to 5500 and starts
    // service on this port (0=5500, 1=5501)
    int recalculatedPort;
    if (vncViewerPort == null) {
        vncViewerPort=0;
    }

    if(vncViewerPort<5500){
        LOGGER.info("Cannot run below 5500, runs now on 5500");
        vncViewerPort=5500;
    }
      if(vncViewerPort>65535){
          LOGGER.info("Valid ports only from 5500-65535, runs now on 5500");
          vncViewerPort=5500;
      }

      recalculatedPort = vncViewerPort-5500;
      System.out.println("orig port="+vncViewerPort+"  Cmdport: "+recalculatedPort);

    String vncViewerAttributes = "-listen " + recalculatedPort;

    String command = systemCommander.commandStringGenerator(null,
        vncViewerName, vncViewerAttributes);
      isRunning.setValue(true);
    systemCommander.startProcessAndUpdate(
        command, "Connected", model.isVncSessionRunningProperty(), vncViewerPid);
      isRunning.setValue(false);
  }


  /**
   * Kills all processes with the Name of the VNCViewer.
   */
  public void killVncViewer() {
    if(isRunning.get()) {
    systemCommander.executeTerminalCommandAndReturnOutput("kill "+ vncViewerPid.get());
    LOGGER.info("Killed vncViewer with PID "+ vncViewerPid.get());
    isRunning.setValue(false);
    }
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
}

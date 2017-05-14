package ch.imedias.rsccfx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;

/**
 * This Class handles a VNC Server.
 * The Thread keeps running as long as the VNCServer is running.
 * Created by jp on 11/05/17.
 */
public class VncServerHandler extends Thread {
  private final SystemCommander systemCommander;
  private final Rscc model;
  private final String vncServerName = "x11vnc";
  private BooleanProperty isRunning = new SimpleBooleanProperty(false);
  private LongProperty vncServerPid = new SimpleLongProperty(-1);
  private String hostAddress;
  private Integer vncViewerPort;
  private boolean reverseMode;


  /**
   * Constructor to instantiate a VNCViewer.
   * @param model The one and only Model.
   * @param hostAddress Address to connect to.
   * @param vncViewerPort Port to connect to.
   */
  public VncServerHandler(Rscc model, String hostAddress,
                          Integer vncViewerPort, boolean reverseMode) {
    this.reverseMode = reverseMode;
    this.model = model;
    this.hostAddress = hostAddress;
    this.vncViewerPort = vncViewerPort;
    this.systemCommander = model.getSystemCommander();
  }


  /**
   * Starts the VNCServer in the given mode (Reverse or normal).
   */
  public void run() {
    if (reverseMode) {
      startVncServerReverse();
    } else {
      startVncServer();
    }
  }


  /**
   * Starts this VNCServer listening on localhost.
   */
  public void startVncServer() {
    StringBuilder vncServerAttributes = new StringBuilder("-bg -localhost");

    if (model.getVncViewOnly()) {
      vncServerAttributes.append(" -viewonly");
    }
    vncServerAttributes.append(" -rfbport ").append(model.getVncPort());

    String command = systemCommander.commandStringGenerator(null,
        vncServerName, vncServerAttributes.toString());
   vncServerPid.set(systemCommander.startProcessAndReturnPid(command));
    //"connection from client"
  }


  /**
   * Starts this VNCServer and connects to listen VNCViewer.
   */
  private void startVncServerReverse() {
    StringBuilder vncServerAttributes = new StringBuilder(
        "-connect " + hostAddress + ":" + vncViewerPort);

    String command = systemCommander.commandStringGenerator(null,
        vncServerName, vncServerAttributes.toString());
    systemCommander.startProcessAndUpdate(command,
        "OK",model.isVncSessionRunningProperty(),vncServerPid);
    //"OK"
  }


  /**
   * Kills all processes with the Name of the VNCServer.
   */
  public void killVncServer() {
    String command = systemCommander.commandStringGenerator(null,
        "killall", vncServerName);
    systemCommander.executeTerminalCommandAndReturnOutput(command);
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

package ch.imedias.rsccfx.model.iceutils;

import ch.imedias.rsccfx.model.Rscc;
import java.io.File;
import java.net.InetAddress;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;
import org.ice4j.ice.harvest.StunCandidateHarvester;

/**
 * Created by pwigger on 01.05.17.
 */
public class IceProcess extends Thread {

  private Rscc model;
  private Agent agent;
  private StateListener stateListener;


  /**
   * Initiates a new IceProcess, either active: with connectivity checks, or passive.
   *
   * @param model the one and only model.
   */
  public IceProcess(Rscc model) {
    this.agent = new Agent(); // A simple ICE Agent
    this.model = model;
    agent.setControlling(true);
  }

  /**
   * Runs the STUN / ICE stuff.
   */
  public void run() {
    try {
      startStun();
      model.setMySdp(createSdp());

      while (!this.isInterrupted() && model.getOtherSdp() == null) {
        Thread.sleep(1000);
        System.out.println("ICE Process: waiting for other SDP");
      }

      SdpUtils.parseSdp(agent, model.getOtherSdp());

      stateListener = new StateListener();
      agent.addStateChangeListener(stateListener);

      Component iceComponent = startIceConnectivityEstablishment();
      if (iceComponent != null) {
        model.setForeignPort(iceComponent
            .getSelectedPair().getRemoteCandidate().getTransportAddress().getPort());
        model.setForeignIpAddress(iceComponent
            .getSelectedPair().getRemoteCandidate().getTransportAddress().getAddress());
        model.setMyIceProcessingState("Succeed");
      } else {
        model.setMyIceProcessingState("Failed");
      }

    } catch (InterruptedException ie) {
      agent.free();
      System.out.println("ICE Process: Chavely woken");
      return;
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  /**
   * Ask public Stunservers for its own public IP and Port,
   * both active and passive have to do this.
   */
  private void startStun() throws Throwable {
    for (String hostname : model.getSTUNSERVERS()) {
      try {
        InetAddress address = InetAddress.getByName(hostname);
        TransportAddress ta = new TransportAddress(
            address, model.getSTUNSERVERPORT(), Transport.UDP);
        agent.addCandidateHarvester(new StunCandidateHarvester(ta));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    IceMediaStream stream = agent.createMediaStream("data");
    agent.createComponent(
        stream, Transport.UDP, model.getIcePort(),
        model.getIcePort(), model.getIcePort() + 100);

  }

  /**
   * creates SDP and uploads it to the public server.
   * (only passive part)
   */

  private String createSdp() throws Throwable {
    return SdpUtils.createSdp(agent);
  }


  /**
   * Checks if a connection is possible (only active part does it).
   *
   * @return Component contains all Information to build a UDP socket, null if ICE did not manage
   * to establish a connection.
   */

  private Component startIceConnectivityEstablishment() throws Throwable {
    // You need to listen for state change so that once connected you can then use the socket.
    agent.startConnectivityEstablishment(); // This will do all the work for you to connect

    while (!this.isInterrupted() && agent.getState() == IceProcessingState.RUNNING) {
      Thread.sleep(1000);
      System.out.println("ICE Process running");
    }

    if (agent.getState() == IceProcessingState.FAILED) {
      System.out.println("ICE Failed");
      agent.free();
      return null;
    }

    System.out.println("Got a working socket");
    Component rtpComponent = stateListener.rtpComponent;
    agent.free();
    return rtpComponent;
  }
}

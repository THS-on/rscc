package ch.imedias.rsccfx.model.iceutils;

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
public class IceProcess {

  private static final String STUNSERVER1 = "numb.viagenie.ca";
  private static final String STUNSERVER2 = "stun.ekiga.net";
  private static final int STUNPORT = 3478;

  private Agent agent;
  private int port;
  private String key;
  private StateListener stateListener;

  /**
   * Initiates a new IceProcess, either active: with connectivity checks, or passive.
   *
   * @param port defines the port which ice uses for holepunching and connectivity tests
   * @param key  handed over via phone
   */

  public IceProcess(int port, String key) {
    this.agent = new Agent(); // A simple ICE Agent
    agent.setControlling(true);
    this.port = port;
    this.key = key;
  }

  /**
   * @return Component which contains all necessary objects to build a socket.
   */
  public Component startIceProcessAsSupporter() throws Throwable {
    startStun();
    createSdp("Supporter"+key);
    recieveSdp("Requester"+key);
    Component iceComponent = startIceConnectivityEstablishment("Supporter"+key);
    waitForOtherSideToBeFinished("Requester"+key);
    return iceComponent;
  }

  /**
   * Only starts the STUN Process, generates and uploads the SDP.
   */

  public Component startIceProcessAsRequester() throws Throwable {
    startStun();
    createSdp("Requester"+key);
    recieveSdp("Supporter"+key);
    Component iceComponent = startIceConnectivityEstablishment("Requester"+key);
    waitForOtherSideToBeFinished("Supporter"+key);
    return iceComponent;  }


  /**
   * Ask public Stunservers for its own public IP and Port,
   * both active and passive have to do this.
   */
  private void startStun() throws Throwable {
    String[] hostnames = new String[] {STUNSERVER1, STUNSERVER2};

    for (String hostname : hostnames) {
      try {
        InetAddress address = InetAddress.getByName(hostname);
        TransportAddress ta = new TransportAddress(address, STUNPORT, Transport.UDP);
        agent.addCandidateHarvester(new StunCandidateHarvester(ta));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    IceMediaStream stream = agent.createMediaStream("data");
    agent.createComponent(stream, Transport.UDP, port, port, port + 100);
  }



  /**
   * creates SDP and uploads it to the public server.
   * (only passive part)
   */

  private void createSdp(String sdpName) throws Throwable {
    String toSend = SdpUtils.createSdp(agent);
    File file = new File("resources/IceSDP/sdp" + sdpName + ".txt");
    SdpUtils.saveToFile(toSend, file);
    SdpUtils.uploadFile(file);
  }


  /**
   * downloads the Requester sdp (Session description Protocol) from a public Server
   * or waits until a file is available. (only for active part)
   * TODO: how to get out of it??
   */
  private void recieveSdp(String sdpName) throws Throwable {

    String toSend = SdpUtils.createSdp(agent);
    String remoteReceived = null;
    while (remoteReceived == null) {
      try {
        String url = "http://www.pwigger.ch/rbp/sdp";
        remoteReceived = SdpUtils.downloadFile(url + sdpName + ".txt");
      } catch (Exception e) {
        Thread.sleep(1000);
        System.out.println("no File yet!");
      }
    }
      SdpUtils.parseSdp(agent, remoteReceived);
      stateListener = new StateListener();
      agent.addStateChangeListener(stateListener);

  }




  /**
   * waits until the sdp is removed = sign that the other side has a working connection,
   * then kills the agent.
   * (only passive part)
   */

  private void waitForOtherSideToBeFinished(String sdpName) throws Throwable {
    String otherSdp = "notnullyet";
    while (otherSdp != null) {

      try {
        otherSdp = SdpUtils.downloadFile("http://www.pwigger.ch/rbp/sdp" + sdpName + ".txt");
        // This information was grabbed from the server, and shouldn't be empty.
        System.out.println("File still present!");
        Thread.sleep(1000);

      } catch (Exception e) {
        otherSdp = null;
      }
    }
    agent.free();
  }

  /**
   * Checks if a connection is possible (only active part does it).
   * @return Component contains all Information to build a UDP socket,
   *         null if ICE did not manage to establish a connection.
   */

  private Component startIceConnectivityEstablishment(String sdpName) throws Throwable {
    // You need to listen for state change so that once connected you can then use the socket.
    agent.startConnectivityEstablishment(); // This will do all the work for you to connect

    while (agent.getState() != IceProcessingState.TERMINATED) {
      Thread.sleep(1000);
      System.out.println("no working socket yet");
      if (agent.getState() == IceProcessingState.FAILED) {
        System.out.println("ICE Failed");
        agent.free();
        return null;
      }
    }

    System.out.println("Got a working socket");
    Component rtpComponent = stateListener.rtpComponent;
    SdpUtils.deleteFile("sdp" + sdpName + ".txt");
    return rtpComponent;
  }
}

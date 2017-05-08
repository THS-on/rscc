package ch.imedias.rsccfx.model;

import ch.imedias.rsccfx.model.iceutils.SdpUtils;
import ch.imedias.rsccfx.model.iceutils.StateListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;
import org.ice4j.ice.harvest.StunCandidateHarvester;

/**
 * Created by jp on 08/05/17.
 */
public class Rscccfp extends Thread {

  private final Rscc model;

  private Socket connectionSocket;
  private DataOutputStream outputStream;
  private BufferedReader inputStream;
  private boolean isServer;

  private Agent agent;
  private StateListener stateListener;

  public Rscccfp(Rscc model, boolean isServer) {
    this.model = model;
    this.isServer = isServer;
    this.agent = new Agent(); // A simple ICE Agent
    agent.setControlling(true);
  }

  /**
   * sets type for threading.
   */
  public void run() {
    if (isServer) {
      try {
        startRscccfpServer();
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    } else {
      try {
        startRscccfpClient("127.0.0.1");
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
  }


  /**
   * Starts the TCP - Server.
   */
  public void startRscccfpServer() throws Throwable {

    //Start TCP-Server
    System.out.println("RSCCCFP: start server");
    ServerSocket serverSocket;
    serverSocket = new ServerSocket(model.getVncPort());

    //Start ICE Agent
    startStun();

    //Wait for connection
    System.out.println("RSCCCFP: wait for client");
    connectionSocket = serverSocket.accept();
    inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    outputStream = new DataOutputStream(connectionSocket.getOutputStream());
    System.out.println("RSCCCFP: Client connected");

    //create SDP dump
    model.setMySdp(createSdp());
    System.out.println("RSCCCFP: my sdp:");
    System.out.println(model.getMySdp());

    //send my SDP
    sendMySdp(model.getMySdp());

    //receive results
    receiveOtherIceProcessingState();

    //receive other SDP
    receiveOtherSdp();

    //Stun Magic
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

    //send results
    sendMyIceProcessingState();

    elaborateResults();
  }


  /**
   * Starts the TCP-Client.
   */
  public void startRscccfpClient(String host) throws Throwable {

    //Start ICE Agent
    startStun();

    //create SDP dump
    model.setMySdp(createSdp());

    //connect to server
    System.out.println("start client");
    connectionSocket = new Socket(host, model.getVncPort());
    System.out.println("RSCCCFP: Connected to server");
    outputStream = new DataOutputStream(connectionSocket.getOutputStream());
    inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

    //receive other SDP
    receiveOtherSdp();

    //Stun Magic
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


    //send results
    sendMyIceProcessingState();

    //send my sdp
    sendMySdp(model.getMySdp());

    //receive results
    receiveOtherIceProcessingState();

    closeConnection();

    elaborateResults();
  }

  private void elaborateResults() {
    System.out.println("My Process State: " + model.getMyIceProcessingState());
    System.out.println("Other Process State: " + model.getOtherIceProcessingState());
  }

  /**
   * Receive other state.
   */
  private void receiveOtherIceProcessingState() {
    try {
      String OtherIceProcessingStat = inputStream.readLine();
      model.setOtherIceProcessingState(OtherIceProcessingStat);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * creates SDP.
   */
  private String createSdp() throws Throwable {
    return SdpUtils.createSdp(agent);
  }


  /**
   * reads SDP-Dump from opposite.
   */
  private void receiveOtherSdp() {
    StringBuilder receivedSdp = new StringBuilder();
    try {
      //wait for starting line
      String sdpLine = inputStream.readLine();

      while (!sdpLine.equals("sdpStart")) {
        sdpLine = inputStream.readLine();
      }
      sdpLine = inputStream.readLine();

      while (!sdpLine.equals("sdpEnd")) {

        receivedSdp.append(sdpLine);
        receivedSdp.append('\n');
        sdpLine = inputStream.readLine();
      }

      System.out.println("RSCCCFP: received sdp:");
      System.out.println("RSCCCFP:" + receivedSdp.toString());

      model.setOtherSdp(receivedSdp.toString());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Closes TCP-Connections.
   */
  public void closeConnection() {
    try {
      connectionSocket.close();
      outputStream.close();
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Sends SDP-Dump to opposite.
   */
  public void sendMySdp(String sdpDump) {
    System.out.println("RSCCCFP: Sending this SDP:");
    System.out.println(model.getMySdp());

    try {
      outputStream.writeBytes("sdpStart" + '\n');
      outputStream.writeBytes(sdpDump + '\n');
      outputStream.writeBytes("sdpEnd" + '\n');
      outputStream.flush();
      System.out.println("RSCCCFP: sent sdp");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Sends ICE-Result to opposite.
   */
  public void sendMyIceProcessingState() {
    System.out.println("RSCCCFP: Send myIceProcessingState: " + model.getMyIceProcessingState());
    try {
      outputStream.writeBytes(model.getMyIceProcessingState());
    } catch (Exception e) {
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

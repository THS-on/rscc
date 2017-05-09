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
import java.net.SocketException;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;
import org.ice4j.ice.harvest.StunCandidateHarvester;

/**
 * Remote Support Client Connection Control Flow Protocol
 * This is the Protocol to see if a UPD Connection between the Clients is possible.
 * It uses a TCP-connection between the clients to exchange SDP-Dumps and run the ICE-Framework
 *
 * Created by jp on 08/05/17.
 */

public class Rscccfp extends Thread {

  private final Rscc model;

  private Socket connectionSocket;
  private ServerSocket serverSocket;
  private DataOutputStream outputStream;
  private BufferedReader inputStream;
  private boolean isServer;
  private String mySdp;
  private String remoteSdp;
  private Agent agent;
  private StateListener stateListener;

  /**
   * This is the Protocol to see if a UPD Connection  between the Clients is possible.
   *
   * @param model The one and only Model.
   * @param isServer Specifies if protocol is startet in server or client mode.
   */
  public Rscccfp(Rscc model, boolean isServer) {
    this.model = model;
    this.isServer = isServer;
    this.agent = new Agent(); // A simple ICE Agent
    agent.setControlling(true);
  }


  /**
   * Starts the Protocol as Server or Client depending on isServer.
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
    serverSocket = new ServerSocket(model.getVncPort());

    //Wait for connection
    System.out.println("RSCCCFP: wait for client");
    connectionSocket = serverSocket.accept();
    inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    outputStream = new DataOutputStream(connectionSocket.getOutputStream());
    System.out.println("RSCCCFP: Client connected");

    runIceMagic();
  }

  /**
   * Starts the TCP-Client.
   */
  public void startRscccfpClient(String host) throws Throwable {

    //connect to server
    System.out.println("start client");
    connectionSocket = new Socket(host, model.getVncPort());
    connectionSocket.setTcpNoDelay(false);
    System.out.println("RSCCCFP: Connected to server");
    outputStream = new DataOutputStream(connectionSocket.getOutputStream());
    inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

    runIceMagic();

  }


  /**
   * Runs SDP exchange and ICE Magic over the established TCP-connection.
   *
   * @throws Throwable to be removed.
   */
  private void runIceMagic() throws Throwable {
    //Start ICE Agent
    startStun();

    //create SDP dump
    mySdp = createSdp();

    //send my SDP
    sendMySdp(mySdp);

    //receive other SDP
    receiveRemoteSdp();

    //Stun Magic
    SdpUtils.parseSdp(agent, remoteSdp);
    stateListener = new StateListener();
    agent.addStateChangeListener(stateListener);
    Component iceComponent = startIceConnectivityEstablishment();
    if (iceComponent != null) {
      model.setRemoteClientPort(iceComponent
          .getSelectedPair().getRemoteCandidate().getTransportAddress().getPort());
      model.setRemoteClientIpAddress(iceComponent
          .getSelectedPair().getRemoteCandidate().getTransportAddress().getAddress());
      model.setLocalIceSuccessful(true);
    } else {
      model.setLocalIceSuccessful(false);
    }

    agent.free();

    //send results
    sendMyIceProcessingState();

    //receive results
    receiveOtherIceProcessingState();

    closeConnection();

    System.out.println("myState: " + model.isLocalIceSuccessful());
    System.out.println("remoteState: " + model.isRemoteIceSuccessful());
  }


  /**
   * Receive other state.
   */
  private void receiveOtherIceProcessingState() {
    System.out.println("RSCCCFP: wait for other state");
    try {
      int remoteIceProcessState = inputStream.read();
      if (remoteIceProcessState == 1) {
        model.setRemoteIceSuccessful(true);
      } else {
        model.setRemoteIceSuccessful(false);
      }
      System.out.println("RSCCCFP: received other state");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sends ICE-Result to opposite.
   */
  public void sendMyIceProcessingState() {
    System.out.println("RSCCCFP: Send myIceProcessingState");
    try {
      outputStream.writeBoolean(model.isLocalIceSuccessful());
      outputStream.flush();
    } catch (Exception e) {
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
   * Sends SDP-Dump to opposite.
   */
  public void sendMySdp(String sdpDump) {
    System.out.println("RSCCCFP: Sending this SDP:");
    System.out.println(mySdp);

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
   * reads SDP-Dump from opposite.
   */
  private void receiveRemoteSdp() {

    System.out.println("RSCCCFP: wait for other sdp");
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

      remoteSdp = receivedSdp.toString();
      System.out.println("RSCCCFP: received other sdp");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Closes All Connections.
   */
  public void closeConnection() {
    try {
      if (serverSocket != null && !serverSocket.isClosed()) {
        try {
          serverSocket.close();
        } catch (SocketException e) {
          System.out.println("RSCCCFP: Server Socket closed heavly");
        }
      }
      if (connectionSocket != null && !connectionSocket.isClosed()) {
        connectionSocket.close();
      }
      if (outputStream != null) {
        outputStream.close();
      }
      if (inputStream != null) {
        inputStream.close();
      }
      agent.free();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  /**
   * Ask public Stunservers for its own public IP and Port,
   * both active and passive have to do this.
   */
  private void startStun() throws Throwable {
    for (String hostname : model.getStunServers()) {
      try {
        InetAddress address = InetAddress.getByName(hostname);
        TransportAddress ta = new TransportAddress(
            address, model.getStunServerPort(), Transport.UDP);
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
   * @return Component contains all Information to build a UDP socket, null if ICE did not manage to establish a connection.
   */

  private Component startIceConnectivityEstablishment() throws Throwable {
    // You need to listen for state change so that once connected you can then use the socket.
    agent.startConnectivityEstablishment(); // This will do all the work for you to connect

    while (agent.getState() != IceProcessingState.TERMINATED) {
      Thread.sleep(1000);
      System.out.println("ICE Process running");

      if (agent.getState() == IceProcessingState.FAILED) {
        System.out.println("ICE Failed");

        return null;
      }
    }

    System.out.println("Got a working socket");
    Component rtpComponent = stateListener.rtpComponent;

    return rtpComponent;
  }

}

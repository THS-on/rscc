package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by jp on 08/05/17.
 */
public class Rscccfp extends Thread {

  private final Rscc model;

  private Socket connectionSocket;
  private DataOutputStream outputStream;
  private BufferedReader inputStream;
  private boolean isServer;

  public Rscccfp(Rscc model, boolean isServer) {
    this.model = model;
    this.isServer = isServer;
  }

  /**
   * sets type for threading.
   */
  public void run() {
    if (isServer) {
      startRscccfpServer();
    } else {
      startRscccfpClient("127.0.0.1");
    }
  }


  /**
   * Starts the TCP - Server.
   */
  public void startRscccfpServer() {

    System.out.println("RSCCCFP: start server");
    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(model.getVncPort());
      serverSocket.setSoTimeout(1000);
      while (!this.isInterrupted()) {
        System.out.println("wait for client");
        try {
          connectionSocket = serverSocket.accept();
        } catch (SocketTimeoutException e) {

        }
      }

      if (this.isInterrupted()) {
        serverSocket.close();
        System.out.println("RSCCCFP: closing and return");
        return;

      }
      System.out.println("RSCCCFP: connection accepted");

      inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      outputStream = new DataOutputStream(connectionSocket.getOutputStream());

      receiveOtherSdp();
      sendMySdp(model.getMySdp());

      //Wait until STUN Magic happend
      while (!this.isInterrupted() && model.getMyIceProcessingState() == null) {
        System.out.println("RSCCCFP: Wait for STUN Magic");
        Thread.sleep(1000);
      }

      receiveOtherIceProcessingState();
      sendMyIceProcessingState();


    } catch (InterruptedException ie) {
      closeConnection();
      System.out.println("ICE Process: Chavely woken");
    } catch (Exception e) {

      e.printStackTrace();
    }
  }

  /**
   * Starts the TCP-Client.
   */
  public void startRscccfpClient(String host) {
    System.out.println("start client");
    try {
      connectionSocket = new Socket(host, model.getVncPort());
      System.out.println("RSCCCFP: Connected to server");

      outputStream = new DataOutputStream(connectionSocket.getOutputStream());
      inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

      sendMySdp(model.getMySdp());
      receiveOtherSdp();

      //Wait until STUN Magic happend
      while (!this.isInterrupted() && model.getMyIceProcessingState() == null) {
        Thread.sleep(1000);
      }

      sendMyIceProcessingState();
      receiveOtherIceProcessingState();

      closeConnection();

    } catch (InterruptedException ie) {
      closeConnection();
      System.out.println("ICE Process: Chavely woken");
    } catch (Exception e) {
      closeConnection();
      e.printStackTrace();
    }
  }

  private void receiveOtherIceProcessingState() {
    try {
      String OtherIceProcessingStat = inputStream.readLine();
      model.setOtherIceProcessingState(OtherIceProcessingStat);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * reads SDP-Dump from opposite.
   */
  private void receiveOtherSdp() {
    StringBuilder receivedSdp = new StringBuilder();
    try {
      //wait for starting line
      String sdpLine = inputStream.readLine();

      while (!sdpLine.equals("sdpStart")){
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
      outputStream.writeBytes("sdpStart"+ '\n');
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
}

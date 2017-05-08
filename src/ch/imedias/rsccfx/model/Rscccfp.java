package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jp on 08/05/17.
 */
public class Rscccfp {

  private final Rscc model;

  private Socket connectionSocket;

  private DataOutputStream outToServer;
  private BufferedReader inFromServer;
  private BufferedReader inFromClient;



  private DataOutputStream outToClient;


  public Rscccfp(Rscc model) {
    this.model = model;
  }

  public void startRscccfpServer() {
    String clientSentence;
    String capitalizedSentence;

    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(5900);
      connectionSocket = serverSocket.accept();

      inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      outToClient = new DataOutputStream(connectionSocket.getOutputStream());

      clientSentence = inFromClient.readLine();
      System.out.println("Received: " + clientSentence);
      capitalizedSentence = clientSentence.toUpperCase() + '\n';
      outToClient.writeBytes(capitalizedSentence);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void startRscccfpClient(String host, int port) {
    String modifiedSentence;
    try {

      connectionSocket = new Socket("127.0.0.1", 5900);
      outToServer = new DataOutputStream(connectionSocket.getOutputStream());
      inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      outToServer.writeBytes(content + '\n');

      modifiedSentence = inFromServer.readLine();
      connectionSocket.close();
      outToServer.close();
      inFromServer.close();

    } catch (Exception exc) {
      modifiedSentence = "";
    }
  }

  public void sendSDP() {

  }

  public void sendResult() {

  }

}

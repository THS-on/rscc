package ch.imedias.rsccfx.model.iceutils.rudp;

/**
 * Created by pwg on 20.04.17.
 */

import ch.imedias.rsccfx.model.iceutils.IceProcess;
import ch.imedias.rsccfx.model.iceutils.rudp.src.ReliableSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

import org.ice4j.TransportAddress;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;

public class SimpleProxySupporterRudp {

  public static final String KEY = "0102034";
  public static final int LOCALFORWARDINGPORT = 2601;
  public static final int ICEPORT = 5060;

  /**
   * TODO: needs to be implemented into the model,
   * maybe two classes Requester and supporter can even be merged into one?
   *
   * @param args probably Key and local VNC port to connect to + Port Ice should start.
   */
  public static void main(String[] args) throws Throwable {
    try {
      IceProcess process = new IceProcess(ICEPORT, KEY);
      Component rtpComponent = process.startIceProcessAsSupporter();
      System.out.println("Ice done, starting rudp");
      runServer(rtpComponent); // never returns
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  /**
   * runs a single-threaded proxy server on
   * the specified port. It never returns.
   */
  public static void runServer(Component rtpComponent)
      throws IOException {

    ServerSocket tcpServerSocket = new ServerSocket(LOCALFORWARDINGPORT);
    Socket tcpSocket;

    ReliableSocket rudpClientSocket;

    DatagramSocket udpSocket = rtpComponent.getSocket();
    CandidatePair candidatePair = rtpComponent.getSelectedPair();
    TransportAddress transportAddress = candidatePair.getRemoteCandidate().getTransportAddress();
    InetAddress remoteAddress = transportAddress.getAddress();
    String remoteAddressAsString = remoteAddress.getHostAddress();
    int remotePort = transportAddress.getPort();

    final byte[] request = new byte[1024];
    byte[] reply = new byte[16384];


    while (true) {


      try {
        System.out.println("connect to " + remoteAddressAsString + ":" + remotePort);
        rudpClientSocket = new ReliableSocket();
        rudpClientSocket.connect(new InetSocketAddress(remoteAddress,remotePort));


        final InputStream streamFromServer = rudpClientSocket.getInputStream();
        final OutputStream streamToServer = rudpClientSocket.getOutputStream();


        tcpSocket = tcpServerSocket.accept();
        tcpSocket.setTcpNoDelay(true);

        System.out.println("connected");
        final InputStream streamFromClient = tcpSocket.getInputStream();
        final OutputStream streamToClient = tcpSocket.getOutputStream();

        // a thread to read the client's requests and pass them
        // to the server. A separate thread for asynchronous.
        Thread t = new Thread() {
          public void run() {
            int bytesRead;
            try {
              while ((bytesRead = streamFromClient.read(request)) != -1) {
                streamToServer.write(request, 0, bytesRead);
                streamToServer.flush();
              }
            } catch (IOException e) {
              System.out.println(e);
            }

            // the client closed the connection to us, so close
            // connection to the server.
            try {
              streamToServer.close();
            } catch (IOException e) {
              System.out.println(e);
            }
          }
        };

        // Start the client-to-server request thread running
        t.start();

        // Read the server's responses
        // and pass them back to the client.
        int bytesRead;
        try {
          while ((bytesRead = streamFromServer.read(reply)) != -1) {
            streamToClient.write(reply, 0, bytesRead);
            streamToClient.flush();
          }
        } catch (IOException e) {
          System.out.println(e);
        }

        // The server closed its connection to us, so we close our
        // connection to our client.
        streamToClient.close();
      } catch (Exception e) {
        System.err.println(e);
      } finally {
        try {

          if (udpSocket != null) {
            udpSocket.close();

            /*if (tcpSocket != null) {
            tcpSocket.close();
            }*/
          }

        } catch (Exception e) {
          System.out.println(e);
        }

      }
    }
  }
}




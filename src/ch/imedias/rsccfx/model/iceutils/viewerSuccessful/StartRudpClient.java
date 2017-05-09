package ch.imedias.rsccfx.model.iceutils.viewerSuccessful;

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

public class StartRudpClient {


  /**
   *
   * @param remoteAddress defines the address the client should connect to: given by IceProcess
   * @param remotePort defines the port the client should connoct to: given by IceProcess
   * @param localForwardingPort vncViewer connects to this port locally: xtightvncviewer 127.0.0.1::port
   * @param icePort defines the Port ice did run on, needs to be holepunched by ice
   * @param bufferSize maxsize of a UDP pack TODO: what is max size that works?
   * @throws IOException
   */

  public static void runClient(InetAddress remoteAddress, int remotePort, int localForwardingPort, int icePort, int bufferSize)
      throws IOException {

    ServerSocket tcpServerSocket = new ServerSocket(localForwardingPort);
    Socket tcpSocket;

    ReliableSocket rudpClientSocket;
    String remoteAddressAsString = remoteAddress.getHostAddress();

    final byte[] request = new byte[bufferSize];
    byte[] reply = new byte[bufferSize];


    while (true) {


      try {
        System.out.println("connect to " + remoteAddressAsString + ":" + remotePort);
        //possibly it can be run on any port? should at least. -> TODO try out to remove null and iceport
        rudpClientSocket = new ReliableSocket(remoteAddressAsString,remotePort,null,icePort);
        System.out.println("connected Rudp");


        final InputStream inFromRudpVncVideo = rudpClientSocket.getInputStream();
        final OutputStream outViaRudpVncCommands = rudpClientSocket.getOutputStream();


        tcpSocket = tcpServerSocket.accept();
        tcpSocket.setTcpNoDelay(true);

        System.out.println("connected TCP");
        final InputStream inFromTcpVncCommands = tcpSocket.getInputStream();
        final OutputStream outViaTcpVncVideo = tcpSocket.getOutputStream();

        // a thread to read the client's requests and pass them
        // to the server. A separate thread for asynchronous.
        Thread t = new Thread() {
          public void run() {
            int bytesRead;
            try {
              while ((bytesRead = inFromTcpVncCommands.read(request)) != -1) {
                outViaRudpVncCommands.write(request, 0, bytesRead);
                System.out.println("wrote1:"+bytesRead);
                outViaRudpVncCommands.flush();
              }
            } catch (IOException e) {
              System.out.println(e);
            }

            // the client closed the connection to us, so close
            // connection to the server.
            try {
              outViaRudpVncCommands.close();
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
          while ((bytesRead = inFromRudpVncVideo.read(reply)) != -1) {
            outViaTcpVncVideo.write(reply, 0, bytesRead);
            outViaTcpVncVideo.flush();
          }
        } catch (IOException e) {
          System.out.println(e);
        }

        // The server closed its connection to us, so we close our
        // connection to our client.
        outViaTcpVncVideo.close();
      } catch (Exception e) {
        System.err.println(e);
      } finally {
        //TODO
        //Close all connections


      }
    }
  }
}




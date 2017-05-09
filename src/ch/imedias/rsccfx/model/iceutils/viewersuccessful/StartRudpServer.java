package ch.imedias.rsccfx.model.iceutils.viewersuccessful;
/**
 * Created by pwg on 20.04.17.
 */

import ch.imedias.rsccfx.model.iceutils.rudp.src.ReliableServerSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class StartRudpServer {


  /**
   *
   * @param vncPort the port vnc runs on. Standard:5900
   * @param icePort the port ice did run (holepunched by ice)
   * @param bufferSize maxsize of a UDP pack TODO: what is max size that works?
   */

  public static void runServer(int vncPort, int icePort, int bufferSize)
      throws IOException {

    Socket tcpLocalhostSocket = null;
    ReliableServerSocket rudpServerSocket = new ReliableServerSocket(icePort);
    Socket rudpSocket = null;
    final byte[] request = new byte[bufferSize];
    byte[] reply = new byte[bufferSize];

    /*
    TODO: does not work yet
    SystemCommander startx11vnc=new SystemCommander();
    startx11vnc.executeTerminalCommand("x11vnc -forever:");
    */

    while (true) {

      try {
        rudpSocket = rudpServerSocket.accept();
        System.out.println("working RUDP");
        final InputStream inViaRudpVncCommands = rudpSocket.getInputStream();
        final OutputStream outViaRudpVncVideo = rudpSocket.getOutputStream();

        try {
          tcpLocalhostSocket = new Socket(InetAddress.getLocalHost(), vncPort);
        } catch (Exception e) {
          PrintWriter out = new PrintWriter(outViaRudpVncVideo);
          System.out.print("Proxy server cannot connect to " + ":");
          out.flush();
          tcpLocalhostSocket.close();
          continue;
        }

        final InputStream inFromTcpLocalhostVncVideo = tcpLocalhostSocket.getInputStream();
        final OutputStream outViaTcpLocalhostVncCommands = tcpLocalhostSocket.getOutputStream();

        // a thread to read the, udtClient's requests and pass them
        // to the server. A separate thread for asynchronous.
        Thread t = new Thread() {
          public void run() {
            int bytesRead;
            try {
              while ((bytesRead = inFromTcpLocalhostVncVideo.read(request)) != -1) {
                outViaRudpVncVideo.write(request, 0, bytesRead);
                System.out.println("wrote outviarudp:" + bytesRead);
                outViaRudpVncVideo.flush();
              }
            } catch (IOException e) {
              System.out.println(e);
            }

            // the udtClient closed the connection to us, so close our
            // connection to the server.
            try {
              outViaRudpVncVideo.close();
            } catch (IOException e) {
              System.out.println(e);
            }
          }
        };

        // Start the udtClient-to-server request thread running
        t.start();

        // Read the server's responses
        // and pass them back to the udtClient.
        int bytesRead;
        try {
          while ((bytesRead = inViaRudpVncCommands.read(reply)) != -1) {
            outViaTcpLocalhostVncCommands.write(reply, 0, bytesRead);
            outViaTcpLocalhostVncCommands.flush();
          }
        } catch (IOException e) {
          System.out.println(e);
        }


        // The server closed its connection to us, so we close our
        // connection to our udtClient.
        outViaTcpLocalhostVncCommands.close();
      } catch (Exception e) {
        System.out.println(e);
      } finally {
        {
          try {
            if (tcpLocalhostSocket != null) {
              tcpLocalhostSocket.close();
            }
            if (rudpSocket != null) {
              rudpSocket.close();
            }
          } catch (IOException e) {
            System.out.println(e);
          }
        }
      }
    }
  }

}





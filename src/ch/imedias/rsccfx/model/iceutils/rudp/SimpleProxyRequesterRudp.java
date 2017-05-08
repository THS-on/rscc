package ch.imedias.rsccfx.model.iceutils.rudp;
/**
 * Created by pwg on 20.04.17.
 */

import ch.imedias.rsccfx.model.iceutils.IceProcess;
import ch.imedias.rsccfx.model.iceutils.rudp.src.ReliableServerSocket;
import ch.imedias.rsccfx.model.iceutils.rudp.src.ReliableSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

//Working Solution 22. Apr
//This is based on http://www.java2s.com/Code/Java/Network-Protocol/Asimpleproxyserver.htm
public class SimpleProxyRequesterRudp {

  //Started on the machine which runs x11vnc -forever which is run by the person who wants to Help
  //Starts the iceprocess passive!

  public static final String KEY = "0102034";
  public static final int VNCPORT = 5900;
  public static final int ICEPORT = 5050;

  /**
   * TODO: needs to be implemented into the model
   *
   * @param args probably Key and local VNC port to connect to + Port Ice should start.
   */

  public static void main(String[] args) throws Throwable {
    IceProcess process = new IceProcess(ICEPORT, KEY);
    process.startIceProcessAsRequester();
    System.out.println("Ice done, starting rudp");


    try {
      runServer(); // never returns
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  /**
   * runs a single-threaded proxy server on the port Ice used for connection establishment.
   */
  public static void runServer()
      throws IOException {

    Socket tcpLocalhostSocket = null;
    ReliableServerSocket rudpServerSocket = new ReliableServerSocket(ICEPORT);
    Socket rudpSocket = null;
    final byte[] request = new byte[1024];
    byte[] reply = new byte[16384];

    /*
    TODO: does not work yet
    SystemCommander startx11vnc=new SystemCommander();
    startx11vnc.executeTerminalCommand("x11vnc -forever:");
    */

    while (true) {

      try {
        rudpSocket = rudpServerSocket.accept();
        final InputStream inViaRudpVncCommands = rudpSocket.getInputStream();
        final OutputStream outViaRudpVncVideo = rudpSocket.getOutputStream();

        try {
          tcpLocalhostSocket = new Socket(InetAddress.getLocalHost(), VNCPORT);
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





package ch.imedias.rsccfx.model.connectionutils;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.model.connectionutils.rudp.src.ReliableServerSocket;
import ch.imedias.rsccfx.model.connectionutils.rudp.src.ReliableSocket;
import ch.imedias.rsccfx.model.connectionutils.rudp.src.ReliableSocketProfile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Establishes a RUDP connection between two clients, can be run as server or client.
 * Created by pwg on 09.05.17.
 */
public class RunRudp extends Thread {
  private boolean isOngoing = true;
  private Rscc model;
  private boolean viewerIsRudpClient;
  private boolean callAsViewer;
  private ReliableSocketProfile profile;

  /**
   * Constructor.
   *
   * @param model              the one and only Model.
   * @param viewerIsRudpClient defines if the object is RUDP Client or Server.
   * @param callAsViewer       defines if the caller is vnc server or Client (vnc-viewer).
   */
  public RunRudp(Rscc model, boolean viewerIsRudpClient, boolean callAsViewer) {
    this.model = model;
    this.viewerIsRudpClient = viewerIsRudpClient;
    this.callAsViewer = callAsViewer;
  }

  /**
   * Starts the TCP and RUDP socket and routes the Packages in between over a Proxy.
   */
  public void run() {
    try {

      String remoteAddressAsString = model.getRemoteClientIpAddress().getHostAddress();

      ReliableSocket rudpSocket;
      Socket rudpSocket2;
      ReliableServerSocket rudpServerSocket;
      Socket tcpSocket;
      ServerSocket tcpServerSocket;

      if (viewerIsRudpClient && callAsViewer) {
        //TCP Server & RUDP Client

        // RUDP Client
        System.out.println("Connect rudp to " + model.getRemoteClientIpAddress().getHostAddress()
            + ":" + model.getRemoteClientPort());

        //possibly it can be run on any port? should at least.
        // -> TODO try out to remove null and iceport
        rudpSocket = new ReliableSocket(model.getRemoteClientIpAddress().getHostAddress(),
            model.getRemoteClientPort(), null, model.getIcePort());

        final InputStream rudpInputStream = rudpSocket.getInputStream();
        final OutputStream rudpOutputStream = rudpSocket.getOutputStream();

        System.out.println("Sucessfully connected rudp to " + model.getRemoteClientIpAddress()
            .getHostAddress() + ":" + model.getRemoteClientPort());

        //TCP Server
        System.out.println("Create new tcp-server on " + model.getLocalForwardingPort());
        tcpServerSocket = new ServerSocket(model.getLocalForwardingPort());
        tcpSocket = tcpServerSocket.accept();
        tcpSocket.setTcpNoDelay(true);

        final InputStream tcpInputStream = tcpSocket.getInputStream();
        final OutputStream tcpOutputStream = tcpSocket.getOutputStream();

        System.out.println("Accepted incoming tcp connection from" + tcpSocket.getInetAddress()
            .getHostAddress());

        startProxy(tcpInputStream, tcpOutputStream, rudpInputStream, rudpOutputStream,
            model.getPackageSize());

        rudpSocket.close();
        tcpSocket.close();
        tcpServerSocket.close();
      }


      if (viewerIsRudpClient && !callAsViewer) {
        //RUDP Server & TCP Client

        //RUDP Server
        System.out.println("Create new rudp-server on " + model.getIcePort());
        rudpServerSocket = new ReliableServerSocket(model.getIcePort());
        rudpSocket2 = rudpServerSocket.accept();

        final InputStream rudpInputStream = rudpSocket2.getInputStream();
        final OutputStream rudpOutputStream = rudpSocket2.getOutputStream();
        System.out.println("Accepted incoming rudp connection from" + rudpSocket2.getInetAddress()
            .getHostAddress());

        //TCP Client
        System.out.println("Connect tcp to " + InetAddress.getLocalHost() + ":"
            + model.getVncPort());

        tcpSocket = new Socket(InetAddress.getByName("127.0.0.1"), model.getVncPort());

        final InputStream tcpInputStream = tcpSocket.getInputStream();
        final OutputStream tcpOutputStream = tcpSocket.getOutputStream();

        System.out.println("Sucessful tcp connection");

        startProxy(tcpInputStream, tcpOutputStream, rudpInputStream, rudpOutputStream,
            model.getPackageSize());

        rudpSocket2.close();
        tcpSocket.close();
        rudpServerSocket.close();
      }

      if (!viewerIsRudpClient && callAsViewer) {
        //TCP Server & RUDP Server


        //RUDP Server
        System.out.println("Create new rudp-server on " + model.getIcePort());
        rudpServerSocket = new ReliableServerSocket(model.getIcePort());
        rudpSocket2 = rudpServerSocket.accept();
        System.out.println("Accepted incoming rudp connection from" + rudpSocket2.getInetAddress()
            .getHostAddress());

        final InputStream rudpInputStream = rudpSocket2.getInputStream();
        final OutputStream rudpOutputStream = rudpSocket2.getOutputStream();

        //TCP Server
        tcpServerSocket = new ServerSocket(model.getLocalForwardingPort());
        tcpSocket = tcpServerSocket.accept();
        tcpSocket.setTcpNoDelay(true);
        System.out.println("TCP connected");

        final InputStream tcpInputStream = tcpSocket.getInputStream();
        final OutputStream tcpOutputStream = tcpSocket.getOutputStream();

        System.out.println("Accepted incoming tcp connection from" + tcpSocket.getInetAddress()
            .getHostAddress());

        startProxy(tcpInputStream, tcpOutputStream, rudpInputStream, rudpOutputStream,
            model.getPackageSize());

        rudpSocket2.close();
        tcpSocket.close();
        rudpServerSocket.close();
      }


      if (!viewerIsRudpClient && !callAsViewer) {
        //TCP Client & RUDP Client

        // RUDP Client
        System.out.println("Connect rudp to " + model.getRemoteClientIpAddress().getHostAddress()
            + ":" + model.getRemoteClientPort());

        //possibly it can be run on any port? should at least.
        // -> TODO try out to remove null and iceport
        rudpSocket = new ReliableSocket(model.getRemoteClientIpAddress().getHostAddress(),
            model.getRemoteClientPort(), null, model.getIcePort());

        final InputStream rudpInputStream = rudpSocket.getInputStream();
        final OutputStream rudpOutputStream = rudpSocket.getOutputStream();

        System.out.println("Sucessfully connected rudp to " + model.getRemoteClientIpAddress()
            .getHostAddress() + ":" + model.getRemoteClientPort());

        //TCP Client
        System.out.println("Connect tcp to " + InetAddress.getByName("127.0.0.1").getHostAddress() + ":"
            + model.getVncPort());

        tcpSocket = new Socket(InetAddress.getByName("127.0.0.1"), model.getVncPort());

        final InputStream tcpInputStream = tcpSocket.getInputStream();
        final OutputStream tcpOutputStream = tcpSocket.getOutputStream();

        System.out.println("Sucessful tcp connection");

        startProxy(tcpInputStream, tcpOutputStream, rudpInputStream, rudpOutputStream,
            model.getPackageSize());

        rudpSocket.close();
        tcpSocket.close();

      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }


  /**
   * Starts the Proxy.
   *
   * @param tcpInput   InputStream on TCP Socket.
   * @param tcpOutput  OutputSocket on TCP Socket.
   * @param rudpInput  InputStream on RUDP Socket.
   * @param rudpOutput OutputStream on RUDP Socket.
   * @param bufferSize Size of the Buffer per package.
   */
  private void startProxy(InputStream tcpInput, OutputStream tcpOutput, InputStream
      rudpInput, OutputStream rudpOutput, int bufferSize) {

    final byte[] request = new byte[bufferSize];
    byte[] reply = new byte[bufferSize];

    // a thread to read the client's requests and pass them
    // to the server. A separate thread for asynchronous.
    Thread t1 = new Thread() {
      public void run() {
        int bytesRead;
        try {
          while ((bytesRead = tcpInput.read(request)) != -1 && isOngoing) {
            rudpOutput.write(request, 0, bytesRead);
            //System.out.println("wrote1:" + bytesRead);
            rudpOutput.flush();
          }
        } catch (IOException e) {
          System.out.println(e);
        }

        // the client closed the connection to us, so close
        // connection to the server.
        try {
          rudpOutput.close();
        } catch (IOException e) {
          System.out.println(e);
        } finally {
          try {
            tcpInput.close();
            rudpOutput.close();
          } catch (Exception e) {
            System.out.println(e);
          }
        }
      }

    };

    // Start the client-to-server request thread running
    t1.start();

    // Read the server's responses
    // and pass them back to the client.

    int bytesRead;
    try {
      while ((bytesRead = rudpInput.read(reply)) != -1 && isOngoing) {
        tcpOutput.write(reply, 0, bytesRead);
        tcpOutput.flush();
      }
    } catch (IOException e) {
      System.out.println(e);
    } finally {
      try {
        tcpOutput.close();
        rudpInput.close();
      } catch (Exception e) {
        System.out.println(e);
      }
    }


    // The server closed its connection to us, so we close our
    // connection to our client.
  }

  public boolean isIsOngoing() {
    return isOngoing;
  }

  public void setIsOngoing(boolean isOngoing) {
    this.isOngoing = isOngoing;
  }
}









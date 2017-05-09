package ch.imedias.rsccfx.model.iceutils.viewersuccessful;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.model.iceutils.rudp.src.ReliableServerSocket;
import ch.imedias.rsccfx.model.iceutils.rudp.src.ReliableSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by pwg on 09.05.17.
 */
public class runRudp extends Thread {
  private static boolean isOngoing = true;

  private Rscc model;
  private boolean viewerIsRudpClient;
  private boolean callAsViewer;

  public runRudp(Rscc model, boolean viewerIsRudpClient, boolean callAsViewer) {
    this.model = model;
    this.viewerIsRudpClient = viewerIsRudpClient;
    this.callAsViewer = callAsViewer;
  }

  public void run() {

    try {
      if (viewerIsRudpClient && callAsViewer) {
        //TCP Server & RUDP Client

        ServerSocket tcpServerSocket = new ServerSocket(model.getLocalForwardingPort());
        Socket tcpSocket;

        ReliableSocket rudpClientSocket;
        String remoteAddressAsString = model.getRemoteClientIpAddress().getHostAddress();


        System.out.println("connect to " + model.getRemoteClientIpAddress().getHostAddress() + ":"
            + model.getRemoteClientPort());
        //possibly it can be run on any port? should at least.
        // -> TODO try out to remove null and iceport
        rudpClientSocket = new ReliableSocket(model.getRemoteClientIpAddress().getHostAddress(),
            model.getRemoteClientPort(), null, model.getIcePort());
        System.out.println("connected Rudp");

        final InputStream rudpInputStream = rudpClientSocket.getInputStream();
        final OutputStream rudpOutputStream = rudpClientSocket.getOutputStream();

        tcpSocket = tcpServerSocket.accept();
        tcpSocket.setTcpNoDelay(true);

        System.out.println("connected TCP");
        final InputStream tcpInputStream = tcpSocket.getInputStream();
        final OutputStream tcpOutputStream = tcpSocket.getOutputStream();

        startProxy(tcpInputStream, tcpOutputStream, rudpInputStream, rudpOutputStream,
            model.getBufferSize());

        rudpClientSocket.close();
        tcpSocket.close();
        tcpServerSocket.close();
      }


      if (viewerIsRudpClient && !callAsViewer) {
        //TCP Client & RUDP Server

        Socket tcpSocket = null;
        ReliableServerSocket rudpServerSocket = new ReliableServerSocket(model.getIcePort());
        Socket rudpSocket = null;

        rudpSocket = rudpServerSocket.accept();
        System.out.println("working RUDP");
        final InputStream rudpInputStream = rudpSocket.getInputStream();
        final OutputStream rudpOutputStream = rudpSocket.getOutputStream();

        tcpSocket = new Socket(InetAddress.getLocalHost(), model.getVncPort());

        final InputStream tcpInputStream = tcpSocket.getInputStream();
        final OutputStream tcpOutputStream = tcpSocket.getOutputStream();


        startProxy(tcpInputStream, tcpOutputStream, rudpInputStream, rudpOutputStream,
            model.getBufferSize());

        rudpSocket.close();
        tcpSocket.close();
        rudpServerSocket.close();
      }

      if (!viewerIsRudpClient && callAsViewer) {
        //TCP Server & RUDP Server
        while (true) {
          //TODO not implemented yet
          // startProxy();
        }
      }


      if (!viewerIsRudpClient && !callAsViewer) {
        //TCP Client & RUDP Client
        while (true) {
          //TODO not implemented yet
          // startProxy();
          System.out.println("not implemented yet");
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }


  public static void startProxy(InputStream tcpInput, OutputStream tcpOutput, InputStream
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
    } catch (
        IOException e) {
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
    runRudp.isOngoing = isOngoing;
  }
}









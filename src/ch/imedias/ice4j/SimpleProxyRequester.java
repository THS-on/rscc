package ch.imedias.ice4j;
/**
 * Created by pwg on 20.04.17.
 */

import org.ice4j.TransportAddress;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;
import udt.UDPEndPoint;
import udt.UDTServerSocket;
import udt.UDTSocket;
import udt.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//Working Solution 22. Apr
//This
public class SimpleProxyRequester {

    public static final String OWNNAME = "PwgVirtualUbuntuServer";
    public static final String REMOTECOMPUTERNAME = "PwgVirtualUbuntuClient";
    public static final int localPort =5500;

    public static void main(String[] args) throws Throwable {

        Component rtpComponent = IceProcessPassive.startIce(5060, OWNNAME,REMOTECOMPUTERNAME);


        try {
            runServer(rtpComponent, localPort); // never returns
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * runs a single-threaded proxy server on
     * the specified local localPort. It never returns.
     */
    public static void runServer(Component rtpComponent,int VNCPort)
            throws IOException {


        final byte[] request = new byte[2048];
        byte[] reply = new byte[2048];

        //Extract rtp Component
        DatagramSocket udpSocket = rtpComponent.getSocket();
        CandidatePair candidatePair=rtpComponent.getSelectedPair();
        TransportAddress transportAddress = candidatePair.getRemoteCandidate().getTransportAddress();
        InetAddress remoteAddress = transportAddress.getAddress();
        String remoteAddressAsString = remoteAddress.getHostAddress();
        int remotePort = transportAddress.getPort();

        // Create a ServerSocket to listen for connections
      //  ServerSocket serverSocket = new ServerSocket(2601);
      //  System.out.println("Starting xtightvnc now and connect to localhost:"+localPort);
/*
        ProcessExecutor startx11vnc=new ProcessExecutor();
        startx11vnc.executeScript("x11vnc -connect 127.0.0.1:"+localPort);
*/
        while (true) {
            Socket tcpClientSocket=null;
            UDTSocket udtSocket = null;

            UDPEndPoint endPoint = new UDPEndPoint(udpSocket);
            UDTServerSocket udtServerSocket = new UDTServerSocket(endPoint);

           try {
               udtSocket = udtServerSocket.accept();
               tcpClientSocket=new Socket(InetAddress.getLocalHost(),VNCPort);

           }
           catch(Exception e){}



            final InputStream inFromUDTVNCVideoStream = udtSocket.getInputStream();
            final OutputStream outViaUDTVNCCommands = udtSocket.getOutputStream();
            final InputStream inFromTCPLocalhostVNCCommands = tcpClientSocket.getInputStream();
            final OutputStream outViaTCPLocalhostVNCVideoStream = tcpClientSocket.getOutputStream();


            try {


                } catch (Exception e) {
                    PrintWriter out = new PrintWriter(outViaTCPLocalhostVNCVideoStream);
                    System.out.print("Proxy server cannot connect to " + ":");
                    out.flush();
                    tcpClientSocket.close();
                    continue;
                }


                // Get server streams.


                // a thread to read the client's requests and pass them
                // to the server. A separate thread for asynchronous.
                Thread t = new Thread() {
                    public void run() {
                        int bytesRead;
                        try {
                            while ((bytesRead = inFromTCPLocalhostVNCCommands.read(request)) != -1) {
                                outViaUDTVNCCommands.write(request, 0, bytesRead);
                                outViaUDTVNCCommands.flush();
                            }
                        } catch (IOException e) {
                        }

                        // the client closed the connection to us, so close our
                        // connection to the server.
                        try {
                            outViaUDTVNCCommands.close();
                        } catch (IOException e) {
                        }
                    }
                };

                // Start the client-to-server request thread running
                t.start();

                // Read the server's responses
                // and pass them back to the client.
                int bytesRead;
                try {
                    while ((bytesRead = inFromUDTVNCVideoStream.read(reply)) != -1) {
                        outViaTCPLocalhostVNCVideoStream.write(reply, 0, bytesRead);
                        outViaTCPLocalhostVNCVideoStream.flush();
                    }
                } catch (IOException e) {
                }

                // The server closed its connection to us, so we close our
                // connection to our client.
                outViaTCPLocalhostVNCVideoStream.close();
            }
        }
    }







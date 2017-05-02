package ch.imedias.ice4j;

/**
 * Created by pwg on 20.04.17.
 */

import ch.imedias.rsccfx.model.SystemCommander;
import org.ice4j.TransportAddress;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;
import udt.UDPEndPoint;
import udt.UDTClient;
import udt.UDTServerSocket;
import udt.UDTSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleProxyServer {

    public static final String OWNNAME = "PwgVirtualUbuntuClient";
    public static final String REMOTECOMPUTERNAME = "PwgVirtualUbuntuServer";
    public static final int VNCPort=5900;

    public static void main(String[] args) throws Throwable {
        try {
            Component rtpComponent= IceProcessActive.startIce(5050, OWNNAME,REMOTECOMPUTERNAME);
            System.out.println("Ice done, starting UDT");



            runServer(rtpComponent, VNCPort); // never returns

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * runs a single-threaded proxy server on
     * the specified local localPort. It never returns.
     */
    public static void runServer(Component rtpComponent, int VNCPort)
            throws IOException {

        final byte[] request = new byte[2048];
        byte[] reply = new byte[2048];
/*
        SystemCommander startVNCServer = new SystemCommander();
        startVNCServer.executeTerminalCommand("x11vnc -forever");
*/
        // Create a ServerSocket to listen for connections with
        // ServerSocket ss = new ServerSocket(localPort);

        while (true) {
            ServerSocket tcpServerSocket= null;
           UDTServerSocket udtServerSocket =null;
            final InputStream streamFromClient;
            final OutputStream streamToClient;
            final InputStream streamFromServer;
            final OutputStream streamToServer;

            try {


                try {
                    //Extract rtp Component
                    DatagramSocket udpSocket= rtpComponent.getSocket();
                    CandidatePair candidatePair=rtpComponent.getSelectedPair();
                    TransportAddress transportAddress = candidatePair.getRemoteCandidate().getTransportAddress();
                    InetAddress remoteAddress=transportAddress.getAddress();
                    String remoteAddressAsString=remoteAddress.getHostAddress();
                    int remotePort= transportAddress.getPort();

                    //now create a UDT Client and establish UDT connection
                     udtServerSocket= new UDTServerSocket(new UDPEndPoint(udpSocket));
                     tcpServerSocket= new ServerSocket(2601);
                  UDTSocket udtSocket= udtServerSocket.accept();
                   Socket tcpSocket = tcpServerSocket.accept();


                    //create VNC receiving Data
                    streamFromClient = tcpSocket.getInputStream();
                    streamToClient = tcpSocket.getOutputStream();

                    streamFromServer = udtSocket.getInputStream();
                    streamToServer = udtSocket.getOutputStream();


                } catch (Exception e) {

                    continue;
                }


                // Get server streams.


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
                        }

                        // the client closed the connection to us, so close our
                        // connection to the server.
                        try {
                            streamToServer.close();
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
                    while ((bytesRead = streamFromServer.read(reply)) != -1) {
                        streamToClient.write(reply, 0, bytesRead);
                        streamToClient.flush();
                    }
                } catch (IOException e) {
                }

                // The server closed its connection to us, so we close our
                // connection to our client.
                streamToClient.close();
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                
            }
        }
    }
}




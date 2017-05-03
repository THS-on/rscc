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
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleProxyViewer {

    //Used by this person who gives support and runs xtightvncclient 127.0.0.1::2601

    public static final String OWNNAME = "RSCCViewer";
    public static final String REMOTECOMPUTERNAME = "RSCCRequester";
    public static final int LOCALFORWARDINGPORT = 2601;
    public static final int ICEPORT = 5060;

    public static void main(String[] args) throws Throwable {
        try {
            Component rtpComponent = IceProcessActive.startIce(ICEPORT, OWNNAME, REMOTECOMPUTERNAME);
            System.out.println("Ice done, starting UDT");


            runServer(rtpComponent); // never returns

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * runs a single-threaded proxy server o16384n
     * the specified local VNCPORT. It never returns.
     */
    public static void runServer(Component rtpComponent)
            throws IOException {

        ServerSocket tcpServerSocket = new ServerSocket(LOCALFORWARDINGPORT);
        Socket tcpSocket;

        UDTServerSocket udtServerSocket = new UDTServerSocket(ICEPORT);
        UDTSocket udtSocket;


        //Extract rtp Component
        DatagramSocket udpSocket = rtpComponent.getSocket();
        CandidatePair candidatePair = rtpComponent.getSelectedPair();
        TransportAddress transportAddress = candidatePair.getRemoteCandidate().getTransportAddress();
        InetAddress remoteAddress = transportAddress.getAddress();
        String remoteAddressAsString = remoteAddress.getHostAddress();
        int remotePort = transportAddress.getPort();

        final byte[] request = new byte[1024];
        byte[] reply = new byte[16384];
/*
        SystemCommander startVNCServer = new SystemCommander();
        startVNCServer.executeTerminalCommand("x11vnc -forever");
*/
        // Create a ServerSocket to listen for connections with
        // ServerSocket ss = new ServerSocket(VNCPORT);

        while (true) {


            try {

                udtSocket = udtServerSocket.accept();


                final InputStream streamFromServer = udtSocket.getInputStream();
                final OutputStream streamToServer = udtSocket.getOutputStream();

                /*TODO: does not work yet: maybe needs multithreading??
                     SystemCommander startxTightVncViewer=new SystemCommander();
                     startxTightVncViewer.executeTerminalCommand("xtightvncviewer 127.0.0.1::2601");
                   Problem: accept lässt warten? anschliessend kann Kommando evt nicht mehr ausgeführt werden?
                    */
                tcpSocket = tcpServerSocket.accept();
                tcpSocket.setTcpNoDelay(true);





                    //create VNC receiving Data





                // Get server streams.

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

                }catch(Exception e){}

            }
        }
    }
}




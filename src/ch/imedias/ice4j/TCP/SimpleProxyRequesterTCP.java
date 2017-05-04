package ch.imedias.ice4j.TCP;
/**
 * Created by pwg on 20.04.17.
 */

import ch.imedias.ice4j.IceProcess;
import org.ice4j.TransportAddress;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;
import udt.UDTClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

//Working Solution 22. Apr
//This is based on http://www.java2s.com/Code/Java/Network-Protocol/Asimpleproxyserver.htm
public class SimpleProxyRequesterTCP {

    //Started on the machine which runs x11vnc -forever which is run by the person who wants to Help

    public static final String OWNNAME = "RSCCRequester";
    public static final String REMOTECOMPUTERNAME = "RSCCViewer";
    public static final int VNCPORT = 5900;
    public static final int ICEPORT = 5050;

    public static void main(String[] args) throws Throwable {

        // start ICE and get all things needed in the rtpComponent
        Component rtpComponent = IceProcess.startIce(ICEPORT, OWNNAME, REMOTECOMPUTERNAME);
        System.out.println("Ice done, starting Proxy with TCP");

        try {
            runServer(rtpComponent, VNCPORT); // never returns
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * runs a single-threaded proxy server on
     * the specified local VNCPORT. It never returns.
     */
    public static void runServer(Component rtpComponent, int VNCPort)
            throws IOException {

        Socket tcpClientSocket = null;
        Socket tcpClient2Socket  = null;

        final byte[] request = new byte[1024];
        byte[] reply = new byte[16384];

        //Extract rtp Component
        CandidatePair candidatePair = rtpComponent.getSelectedPair();
        TransportAddress transportAddress = candidatePair.getRemoteCandidate().getTransportAddress();
        InetAddress remoteAddress = transportAddress.getAddress();
        String remoteAddressAsString = remoteAddress.getHostAddress();
        int remotePort = transportAddress.getPort();

        /*TODO: does not work yet
        SystemCommander startx11vnc=new SystemCommander();
        startx11vnc.executeTerminalCommand("x11vnc -forever:");
        */
        while (true) {

            try {
                // udtClient.connect("10.0.2.6",2020);
                // udtClient.connect("fe80::c7db:a5f3:2b79:d301",2020);
                System.out.println("connect to " + remoteAddressAsString + ":" + remotePort);

                tcpClient2Socket=new Socket(remoteAddress, remotePort);

                final InputStream inFromUDTVNCVideoStream = tcpClient2Socket.getInputStream();
                final OutputStream outViaUDTVNCCommands = tcpClient2Socket.getOutputStream();

                try {
                    tcpClientSocket = new Socket(InetAddress.getLocalHost(), VNCPort);
                } catch (Exception e) {
                    PrintWriter out = new PrintWriter(outViaUDTVNCCommands);
                    System.out.print("Proxy server cannot connect to " + ":");
                    out.flush();
                    tcpClientSocket.close();
                    continue;
                }

                final InputStream inFromTCPLocalhostVNCCommands = tcpClientSocket.getInputStream();
                final OutputStream outViaTCPLocalhostVNCVideoStream = tcpClientSocket.getOutputStream();

                // a thread to read the udtClient's requests and pass them
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

                        // the udtClient closed the connection to us, so close our
                        // connection to the server.
                        try {
                            outViaUDTVNCCommands.close();
                        } catch (IOException e) {
                        }

                    }
                };

                // Start the udtClient-to-server request thread running
                t.start();

                // Read the server's responses
                // and pass them back to the udtClient.
                int bytesRead;
                try {
                    while ((bytesRead = inFromUDTVNCVideoStream.read(reply)) != -1) {
                        outViaTCPLocalhostVNCVideoStream.write(reply, 0, bytesRead);
                        outViaTCPLocalhostVNCVideoStream.flush();
                    }
                } catch (IOException e) {
                }


                // The server closed its connection to us, so we close our
                // connection to our udtClient.
                outViaTCPLocalhostVNCVideoStream.close();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                {
                    try {
                        if (tcpClientSocket != null) {
                            tcpClientSocket.close();
                        }
                        if (tcpClient2Socket != null) {
                            tcpClient2Socket.close();
                        }
                    } catch (IOException e) {
                    }

                }
            }
        }
    }

}





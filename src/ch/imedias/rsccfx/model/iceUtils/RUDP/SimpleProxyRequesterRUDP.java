package ch.imedias.rsccfx.model.iceUtils.RUDP;
/**
 * Created by pwg on 20.04.17.
 */
import ch.imedias.rsccfx.model.iceUtils.IceProcess;
import ch.imedias.rsccfx.model.iceUtils.RUDP.src.ReliableServerSocket;
import ch.imedias.rsccfx.model.iceUtils.RUDP.src.ReliableSocket;
import org.ice4j.TransportAddress;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

//Working Solution 22. Apr
//This is based on http://www.java2s.com/Code/Java/Network-Protocol/Asimpleproxyserver.htm
public class SimpleProxyRequesterRUDP {

    //Started on the machine which runs x11vnc -forever which is run by the person who wants to Help

    public static final String KEY = "0102034";
    public static final int VNCPORT = 5900;
    public static final int ICEPORT = 5050;

    public static void main(String[] args) throws Throwable {
        IceProcess.startIcePassive(ICEPORT, KEY);

        try {
            runServer(); // never returns
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * runs a single-threaded proxy server on
     * the specified local VNCPORT. It never returns.
     */
    public static void runServer()
            throws IOException {

        Socket tcpClientSocket = null;
        ReliableServerSocket rudpServerSocket=new ReliableServerSocket(ICEPORT);
        Socket rudpSocket=null;
        final byte[] request = new byte[1024];
        byte[] reply = new byte[16384];


        /*TODO: does not work yet
        SystemCommander startx11vnc=new SystemCommander();
        startx11vnc.executeTerminalCommand("x11vnc -forever:");
        */
        while (true) {

            try {
                rudpSocket=rudpServerSocket.accept();
                final InputStream inFromUDTVNCVideoStream = rudpSocket.getInputStream();
                final OutputStream outViaUDTVNCCommands = rudpSocket.getOutputStream();

                try {
                    tcpClientSocket = new Socket(InetAddress.getLocalHost(), VNCPORT);
                } catch (Exception e) {
                    PrintWriter out = new PrintWriter(outViaUDTVNCCommands);
                    System.out.print("Proxy server cannot connect to " + ":");
                    out.flush();
                    tcpClientSocket.close();
                    continue;
                }

                final InputStream inFromTCPLocalhostVNCCommands = tcpClientSocket.getInputStream();
                final OutputStream outViaTCPLocalhostVNCVideoStream = tcpClientSocket.getOutputStream();

                // a thread to read the, udtClient's requests and pass them
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
                        if (rudpSocket != null) {
                            rudpSocket.close();
                        }
                    } catch (IOException e) {
                    }

                }
            }
        }
    }

}





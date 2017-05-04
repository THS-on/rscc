package ch.imedias.rsccfx.model.iceUtils;


import org.apache.xpath.SourceTree;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;
import org.ice4j.ice.harvest.StunCandidateHarvester;

import java.io.File;
import java.net.InetAddress;


/**
 * Created by pwigger on 01.05.17.
 */
public class IceProcess {

    private static final String STUNSERVER1 = "numb.viagenie.ca";
    private static final String STUNSERVER2 = "stun.ekiga.net";
    private static final int STUNPORT = 3478;

    private Agent agent;
    private int port;
    private String key;
    private StateListener stateListener;

    /**
     * Initiates a new IceProcess, either active: with connectivity checks, or passive
     *
     * @param port defines the port which ice uses for holepunching and connectivity tests
     * @param key  handed over via phone
     */

    public IceProcess(int port, String key) {
        this.agent = new Agent(); // A simple ICE Agent
        agent.setControlling(true);
        this.port = port;
        this.key = key;
    }

    public Component StartIceProcessAsSupporter() throws Throwable {
        startStun();
        recieveSdp();
        return startIceConnectivityEstablishment();
    }


    public void StartIceProcessAsRequester() throws Throwable {
        startStun();
        createSDP();
        waitForOtherSideToBeFinished();
    }


    /**
     * Asks a public Stunserver for its own public IP and Port, both active and passive have to do this
     *
     * @throws Throwable
     */
    private void startStun() throws Throwable {
        String[] hostnames = new String[]{STUNSERVER1, STUNSERVER2};
// Look online for actively working public STUN Servers. You can find free servers.
// Now add these URLS as Stun Servers with standard 3478 VNCPORT for STUN servrs.
        for (String hostname : hostnames) {
            try {
                // InetAddress qualifies a url to an IP Address, if you have an error here, make sure the url is reachable and correct
                TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), STUNPORT, Transport.UDP);
                // Currently Ice4J only supports UDP and will throw an Error otherwise
                agent.addCandidateHarvester(new StunCandidateHarvester(ta));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IceMediaStream stream = agent.createMediaStream("data");
        agent.createComponent(stream, Transport.UDP, port, port, port + 100);
// The three last arguments are: preferredPort, minPort, maxPort


    }

    /**
     * downloads the Requester sdp (Session description Protocol) from a public Server
     * or waits until a file is available. (only for active part)
     * TODO: how to get out of it??
     *
     * @throws Throwable
     */
    private void recieveSdp() throws Throwable {

        String toSend = SdpUtils.createSDPDescription(agent);//Each computer sends this information
        String remoteReceived = null;
        while (remoteReceived == null) {
            try {
                remoteReceived = SdpUtils.downloadFile("http://www.pwigger.ch/rbp/sdp" + key + ".txt"); // This information was grabbed from the server, and shouldn't be empty.
            } catch (Exception e) {
                Thread.sleep(1000);
                System.out.println("no File yet!");
            }

            SdpUtils.parseSDP(agent, remoteReceived); // This will add the remote information to the agent.

// This information describes all the possible IP addresses and ports
            stateListener = new StateListener();
            agent.addStateChangeListener(stateListener);
        }
    }

    /**
     * creates SDP and uploads it to the public server
     * (only passive part)
     *
     * @throws Throwable
     */

    private void createSDP() throws Throwable {
        String toSend = SdpUtils.createSDPDescription(agent);
        File file = new File("resources/IceSDP/sdp" + key + ".txt");
        SdpUtils.saveToFile(toSend, file);
        SdpUtils.uploadFile(file);
    }


    /**
     * waits until the sdp is removed = sign that the other side has a working connection, then kills the agent
     * (only passive part)
     *
     * @throws Throwable
     */

    private void waitForOtherSideToBeFinished() throws Throwable {
        String remoteReceived = "hello";
        while (remoteReceived != null) {

            try {
                remoteReceived = SdpUtils.downloadFile("http://www.pwigger.ch/rbp/sdp" + key + ".txt");
                // This information was grabbed from the server, and shouldn't be empty.
                System.out.println("File still present!");
                Thread.sleep(1000);

            } catch (Exception e) {
                remoteReceived = null;
            }
        }
        agent.free();
    }

    /**
     * Checks if a connection is possible (only active part does it)
     *
     * @return Component contains all Information to build a UDP socket, null if ICE did not manage to establish a connection.
     * @throws Throwable
     */

    private Component startIceConnectivityEstablishment() throws Throwable {
        // You need to listen for state change so that once connected you can then use the socket.
        agent.startConnectivityEstablishment(); // This will do all the work for you to connect

        while (agent.getState() != IceProcessingState.TERMINATED) {
            Thread.sleep(1000);
            System.out.println("no working socket yet");
            if (agent.getState() == IceProcessingState.FAILED) {
                System.out.println("ICE Failed");
                agent.free();
                return null;
            }
        }
        System.out.println("Got a working socket");
        Component rtpComponent = stateListener.rtpComponent;
        SdpUtils.deleteFile("sdp" + key + ".txt");
        agent.free();
        return rtpComponent;
    }


}

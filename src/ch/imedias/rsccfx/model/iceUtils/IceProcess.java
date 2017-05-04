package ch.imedias.rsccfx.model.iceUtils;


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


    public static Component startIce(int port, String ownName, String remoteComputername) throws Throwable {
        Agent agent = new Agent(); // A simple ICE Agent
        //    agent.setControlling(true);
/*** Setup the STUN servers: ***/
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
        String toSend = SdpUtils.createSDPDescription(agent);//Each computer sends this information


        File file = new File("resources/IceSDP/sdp" + ownName + ".txt");
        SdpUtils.saveToFile(toSend, file);
        // SdpUtils.uploadFile(file);
        String remoteReceived = null;
        while (remoteReceived == null) {
            try {
                remoteReceived = SdpUtils.downloadFile("http://www.pwigger.ch/rbp/sdp" + remoteComputername + ".txt"); // This information was grabbed from the server, and shouldn't be empty.
            } catch (Exception e) {
                Thread.sleep(1000);
                System.out.println("no File yet!");
            }
        }
        SdpUtils.parseSDP(agent, remoteReceived); // This will add the remote information to the agent.

// This information describes all the possible IP addresses and ports

        StateListener stateListener = new StateListener();
        agent.addStateChangeListener(stateListener);

        // You need to listen for state change so that once connected you can then use the socket.
        agent.startConnectivityEstablishment(); // This will do all the work for you to connect

        while (agent.getState() != IceProcessingState.TERMINATED) {
            Thread.sleep(1000);
            System.out.println("no working socket yet");
        }
        System.out.println("Got a working socket");
        Component rtpComponent = stateListener.rtpComponent;


        SdpUtils.deleteFile("sdp" + ownName + ".txt");
        agent.free();
        return rtpComponent;
    }

    public static void startIcePassive(int port, String ownName, String remoteComputername) throws Throwable {
        Agent agent = new Agent();
        agent.setControlling(false);

        String[] hostnames = new String[]{STUNSERVER1, STUNSERVER2};

        for (String hostname : hostnames) {
            try {
                TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), STUNPORT, Transport.UDP);
                agent.addCandidateHarvester(new StunCandidateHarvester(ta));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IceMediaStream stream = agent.createMediaStream("data");
        agent.createComponent(stream, Transport.UDP, port, port, port + 100);
        String toSend = SdpUtils.createSDPDescription(agent);
        File file = new File("resources/IceSDP/sdp" + ownName + ".txt");
        SdpUtils.saveToFile(toSend, file);
        SdpUtils.uploadFile(file);
        StateListener stateListener = new StateListener();
        agent.addStateChangeListener(stateListener);
        Thread.sleep(2000);
        System.out.println("Got a working socket");

        String remoteReceived = "hello";
        while (remoteReceived != null) {
            try {
                remoteReceived = SdpUtils.downloadFile("http://www.pwigger.ch/rbp/sdp" + ownName + ".txt");

            } catch (Exception e) {
                Thread.sleep(1000);
                System.out.println("Waiting for File to be deleted");
            }
            System.out.println("hello");
            agent.free();
        }


    }
}

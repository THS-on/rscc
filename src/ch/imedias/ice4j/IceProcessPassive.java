package ch.imedias.ice4j;


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
public class IceProcessPassive {

    private static final String STUNSERVER1 = "numb.viagenie.ca";
    private static final String STUNSERVER2 = "stun.ekiga.net";
    private static final int STUNPORT = 3478;


    public static Component startIce(int port, String ownName, String remoteComputername) throws Throwable {
        Agent agent = new Agent(); // A simple ICE Agent
        agent.setControlling(false);
/*** Setup the STUN servers: ***/
        String[] hostnames = new String[]{STUNSERVER1, STUNSERVER2};
// Look online for actively working public STUN Servers. You can find free servers.
// Now add these URLS as Stun Servers with standard 3478 localPort for STUN servrs.
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
        SdpUtils.uploadFile(file);

        Component rtpComponent = agent.getStream("data").getComponent(org.ice4j.ice.Component.RTP);

        // Component rtpComponent= stateListener.rtpComponent;


        // SdpUtils.deleteFile("sdp" + ownName + ".txt");
        return rtpComponent;

    }
}
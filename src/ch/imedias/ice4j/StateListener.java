package ch.imedias.ice4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;
import org.ice4j.socket.IceSocketWrapper;


public class StateListener implements PropertyChangeListener {


    public Component rtpComponent;

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getSource() instanceof Agent){


      Agent agent = (Agent) evt.getSource();

      if(agent.getState().equals(IceProcessingState.TERMINATED)) {


        // Your agent is connected. Terminated means ready to communicate
        for (IceMediaStream stream: agent.getStreams()) {
          if (stream.getName().contains("data")) {

              rtpComponent = stream.getComponent(org.ice4j.ice.Component.RTP);


            // Get information about remote address for packet settings
            TransportAddress ta = rtpComponent.getSelectedPair().getRemoteCandidate().getTransportAddress();
            System.out.println(ta.getAddress().getHostAddress());








          }
        }
      }
    }
  }

}
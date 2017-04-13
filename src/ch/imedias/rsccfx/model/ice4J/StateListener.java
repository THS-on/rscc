package ch.imedias.rsccfx.model.ice4J;

import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StateListener implements PropertyChangeListener {



  DatagramSocket socket=null;

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() instanceof Agent) {
      Agent agent = (Agent) evt.getSource();
      if (agent.getState().equals(IceProcessingState.TERMINATED)) {
        // Your agent is connected. Terminated means ready to communicate
        System.out.println("Sucessfully Connected!!! Yay");

        for (IceMediaStream stream : agent.getStreams()) {
          if (stream.getName().contains("audio")) {
            Component rtpComponent = stream.getComponent(org.ice4j.ice.Component.RTP);
            CandidatePair rtpPair = rtpComponent.getSelectedPair();
            // We use IceSocketWrapper, but you can just use the UDP socket
            // The advantage is that you can change the protocol from UDP to TCP easily
            // Currently only UDP exists so you might not need to use the wrapper.
             socket = rtpComponent.getSocket();
            // Get information about remote address for packet settings
            TransportAddress ta = rtpPair.getRemoteCandidate().getTransportAddress();
            hostname = ta.getAddress();
            port = ta.getPort();

          }
        }
      }
    }
  }

  private InetAddress hostname;
  int port;

  public InetAddress getHostname() {
    return hostname;
  }

  public int getPort() {
    return port;
  }

  public DatagramSocket getSocket() {
    return socket;
  }
}
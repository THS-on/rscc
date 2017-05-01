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

 private DatagramSocket socket;
  private InetAddress hostname;
  int port;
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getSource() instanceof Agent){


      Agent agent = (Agent) evt.getSource();
      System.out.println("State Changed" + evt.toString() + " from " + evt.getOldValue()+" to" +evt.getNewValue()+ "Source "+ agent.getState());

      if(agent.getState().equals(IceProcessingState.TERMINATED)) {


        // Your agent is connected. Terminated means ready to communicate
        for (IceMediaStream stream: agent.getStreams()) {
          if (stream.getName().contains("data")) {
            Component rtpComponent = stream.getComponent(org.ice4j.ice.Component.RTP);
            CandidatePair rtpPair = rtpComponent.getSelectedPair();
            // We use IceSocketWrapper, but you can just use the UDP socket
            // The advantage is that you can change the protocol from UDP to TCP easily
            // Currently only UDP exists so you might not need to use the wrapper.
            DatagramSocket sock= rtpPair.getDatagramSocket();

            IceSocketWrapper wrapper  = rtpPair.getIceSocketWrapper();
            socket = rtpComponent.getSocket(); //wrapper.getUDPSocket()

           // DatagramSocket ds=agent.getStunStack().sendUdpMessage();
            // Connect to UTP
          //  SwingNetworkConnectionWorker.setSocket(wrapper);

            try {
              for(int i=0; i<25; i++){
                wrapper.send(new DatagramPacket("hello from Macbook".getBytes(),"hello from Macbook".length()));}
              Thread.sleep(4000);
              for(int i=0; i<25; i++){
                wrapper.send(new DatagramPacket("hello from Macbook 1".getBytes(),"hello from Macbook 1".length()));}
              Thread.sleep(4000);
              for(int i=0; i<25; i++){
                wrapper.send(new DatagramPacket("hello from Macbook 2".getBytes(),"hello from Macbook 2".length()));}




          //    IcePseudoTcp.RemotePseudoTcpJob job= new IcePseudoTcp.RemotePseudoTcpJob(wrapper.getUDPSocket(), new InetSocketAddress(port));
            //  FIXME How should ice4j, UDT and VNC interact?
            }
            catch(Exception e){}
            //Socket server=wrapper.getTCPSocket();







            // Get information about remote address for packet settings
            TransportAddress ta = rtpPair.getRemoteCandidate().getTransportAddress();
            hostname = ta.getAddress();
            port = ta.getPort();







          }
        }
      }
    }
  }

  public DatagramSocket getSocket() {
    return socket;
  }
}
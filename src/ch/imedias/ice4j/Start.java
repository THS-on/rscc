package ch.imedias.ice4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Scanner;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpClientProvider;

/**
 * Created by pwg on 25.04.17.
 */
public class Start {


  public static final String COMPUTERNAME = "PwgMacbook";
  public static final String REMOTECOMPUTERNAME = "PwgRaspberryPie";



  public static void main(String[] args) throws Throwable{
    Agent agent = new Agent(); // A simple ICE Agent
    agent.setControlling(true);

/*** Setup the STUN servers: ***/
    String[] hostnames = new String[] {"numb.viagenie.ca","stun.ekiga.net"};
// Look online for actively working public STUN Servers. You can find free servers.
// Now add these URLS as Stun Servers with standard 3478 port for STUN servrs.
    for(String hostname: hostnames){
      try {
        // InetAddress qualifies a url to an IP Address, if you have an error here, make sure the url is reachable and correct
        TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), 3478, Transport.UDP);
        // Currently Ice4J only supports UDP and will throw an Error otherwise
        agent.addCandidateHarvester(new StunCandidateHarvester(ta));
      } catch (Exception e) { e.printStackTrace();}
    }
    IceMediaStream stream = agent.createMediaStream("data");
    int port = 5000; // Choose any port
    agent.createComponent(stream, Transport.UDP, port, port, port+100);
// The three last arguments are: preferredPort, minPort, maxPort
    String toSend = SdpUtils.createSDPDescription(agent);//Each computer sends this information
    File file = new File("resources/IceSDP/sdp" + COMPUTERNAME + ".txt");
    saveToFile(toSend, file);
    uploadFile(file);

   // String remoteReceived = downloadFile("http://www.pwigger.ch/rbp/sdp" + REMOTECOMPUTERNAME + ".txt"); // This information was grabbed from the server, and shouldn't be empty.
   // SdpUtils.parseSDP(agent, remoteReceived); // This will add the remote information to the agent.

// This information describes all the possible IP addresses and ports

    StateListener stateListener = new StateListener();
    agent.addStateChangeListener(stateListener);

    // You need to listen for state change so that once connected you can then use the socket.
  //  agent.startConnectivityEstablishment(); // This will do all the work for you to connect


  }

  /**
   * saves a SDP into a file.
   *
   * @param localSdp Spd-String
   * @param file     the file to store the sdp in.
   */
  public static void saveToFile(String localSdp, File file) throws Throwable {
    file.getParentFile().mkdirs();
    file.createNewFile();
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(localSdp.getBytes());
  }

  /**
   * uploads the file to a predefined server.
   *
   * @param file the file to be uploaded
   */
  public static void uploadFile(File file) throws Exception {
    FtpClientProvider ftpClientProvider = FtpClientProvider.provider();
    FtpClient ftp = ftpClientProvider.createFtpClient();
    ftp.connect(new InetSocketAddress(InetAddress.getByName("94.126.16.19"), 21));
    //Git: Ask patrick for Password: This is only for testing reasons!
    ftp = ftp.login("rbp", "".toCharArray());
    ftp.putFile(file.getName(), new FileInputStream(file));
  }

  /**
   * checks for the sdp of the remote Computer and downloads it.
   *
   * @param urlAsString url in form "http://www.pwigger.ch/rbp/sdp"
   * @return the remoteSDP as String
   */
  public static String downloadFile(String urlAsString) throws Throwable {
    URL url = new URL(urlAsString);
    Scanner s = new Scanner(url.openStream());
    StringBuilder remoteSdp = new StringBuilder("");
    while (s.hasNext()) {
      String line = s.nextLine();
      line = line.replace("[java]", "");
      line = line.trim();
      if (line.length() == 0) {
        break;
      }
      remoteSdp.append(line);
      remoteSdp.append("\r\n");
    }
    System.out.println(remoteSdp.toString());

    return remoteSdp.toString();
  }
}

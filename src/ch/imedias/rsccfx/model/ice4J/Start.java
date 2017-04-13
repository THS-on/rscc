package ch.imedias.rsccfx.model.ice4J;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;

/**
 * Created by pwg on 13.04.17.
 */
public class Start {

  public static void main(String[] args) throws Throwable {

    IceProcess iceProcess = new IceProcess();
    String localSDP=iceProcess.generateLocalSDP();
    iceProcess.printSDP();
  DatagramSocket socket = iceProcess.tryConnect(readSDP());
//  startListener(UDPsocket.getPort());
    sendTestPackages(socket);
  }

  private static String getComputerName()
  {
    Map<String, String> env = System.getenv();
    if (env.containsKey("COMPUTERNAME"))
      return env.get("COMPUTERNAME");
    else if (env.containsKey("HOSTNAME"))
      return env.get("HOSTNAME");
    else
      return "Unknown Computer";
  }

  private static void startListener(int port){
    PortListener portListener= new PortListener(port);
    portListener.start();
  }

  private static void sendTestPackages(DatagramSocket socket) throws Throwable{
    for(int i=0; i<100; i++) {
      String message = "Testpackage"+i+" from"+ getComputerName();
      DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length());
      socket.send(packet);
    }
  }

  /**
   * Reads an SDP description from the standard input. We expect descriptions
   * provided to this method to be originating from instances of this
   * application running on remote computers.
   *
   * @return whatever we got on stdin (hopefully an SDP description.
   * @throws Throwable if something goes wrong with console reading.
   */
  static String readSDP() throws Throwable {
    System.out.println("Paste remote SDP here. Enter an empty "
        + "line to proceed:");
    System.out.println("(we don't mind the [java] prefix in SDP intput)");
    BufferedReader reader
        = new BufferedReader(new InputStreamReader(System.in));

    StringBuffer buff = new StringBuffer();
    String line;

    while ((line = reader.readLine()) != null) {
      line = line.replace("[java]", "");
      line = line.trim();
      if (line.length() == 0) {
        break;
      }

      buff.append(line);
      buff.append("\r\n");
    }
    return buff.toString();
  }


}


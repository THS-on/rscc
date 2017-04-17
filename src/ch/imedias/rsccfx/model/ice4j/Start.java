package ch.imedias.rsccfx.model.ice4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Scanner;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpClientProvider;

/**
 * Created by pwg on 13.04.17.
 */
public class Start {

  public static final String COMPUTERNAME = "PwgMacbook";
  public static final String REMOTECOMPUTERNAME = "PwgRaspberryPie";
  public static final int PORT = 2020;

  /**
   * starts the IceProcess.
   *
   */
  public static void main(String[] args) throws Throwable {
    IceProcess iceProcess = new IceProcess(PORT);
    String localSdp = iceProcess.generateLocalSdp();
    iceProcess.printSdp();
    File file = new File("resources/IceSDP/sdp" + COMPUTERNAME + ".txt");
    saveToFile(localSdp, file);
    uploadFile(file);
    String remoteSdp = null;

    while (remoteSdp == null) {
      remoteSdp = downloadFile("http://www.pwigger.ch/rbp/sdp" + REMOTECOMPUTERNAME + ".txt");
      System.out.println("trying to get remote sdp...");
    }
    System.out.println("Got remote SDP from server");

    iceProcess.tryConnect(remoteSdp);


    DatagramSocket socket = iceProcess.getSocket();
    if (socket == null) {
      throw new Exception("not sucessful connecting");
    }

    sendTestPackages(socket);

    //Start TCP Server - Person die Remote Support will
    IcePseudoTcp.LocalPseudoTcpJob server = new IcePseudoTcp.LocalPseudoTcpJob(socket);
    server.start();


    //Start TCP Client - Person die Support gibt
    IcePseudoTcp.RemotePseudoTcpJob client = new IcePseudoTcp.RemotePseudoTcpJob(
        socket, new InetSocketAddress(socket.getInetAddress(), PORT));
    client.start();


    Thread.sleep(10000);

  }

  /**
   * starts listening on the specified port and prints all incoming packets.
   *
   * @param port >1024<65535
   */
  private static void startListener(int port) {
    PortListener portListener = new PortListener(port);
    portListener.start();
  }

  /**
   * sends UDP test-packets over the argument socket.
   *
   * @socket
   */
  private static void sendTestPackages(DatagramSocket socket) throws Throwable {
    for (int i = 0; i < 100; i++) {
      String message = "Testpackage" + i + " from " + COMPUTERNAME;
      DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length());
      socket.send(packet);
    }
  }

  /**
   * saves a SDP into a file.
   *
   * @param localSdp Spd-String
   * @param file the file to store the sdp in.
   */
  private static void saveToFile(String localSdp, File file) throws Throwable {
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(localSdp.getBytes());
  }

  /**
   * uploads the file to a predefined server.
   *
   * @param file the file to be uploaded
   */
  private static void uploadFile(File file) throws Exception {
    FtpClientProvider ftpClientProvider = FtpClientProvider.provider();
    FtpClient ftp = ftpClientProvider.createFtpClient();
    ftp.connect(new InetSocketAddress(InetAddress.getByName("94.126.16.19"), 21));
    ftp = ftp.login("rbp", "qJ4bu_12".toCharArray());
    ftp.putFile(file.getName(), new FileInputStream(file));
  }

  /**
   * checks for the sdp of the remote Computer and downloads it.
   *
   * @param urlAsString url in form "http://www.pwigger.ch/rbp/sdp"
   * @return the remoteSDP as String
   */
  private static String downloadFile(String urlAsString) throws Throwable {
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


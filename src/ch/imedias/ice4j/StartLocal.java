package ch.imedias.ice4j;

import java.net.DatagramSocket;

/**
 * Created by pwg on 25.04.17.
 */
public class StartLocal {


  public static final String OWNNAME = "PwgVirtualUbuntu";
  public static final String REMOTECOMPUTERNAME = "PwgMacbook";



  public static void main(String[] args) throws Throwable{
    DatagramSocket udpSocket=IceProcess.startIce(5050, OWNNAME,REMOTECOMPUTERNAME);
  }


}

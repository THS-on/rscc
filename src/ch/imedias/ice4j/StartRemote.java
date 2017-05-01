package ch.imedias.ice4j;

import org.ice4j.ice.Component;

import java.net.DatagramSocket;

/**
 * Created by pwg on 25.04.17.
 */
public class StartRemote {


    public static final String OWNNAME = "PwgMacbook";
    public static final String REMOTECOMPUTERNAME = "PwgVirtualUbuntu";


    public static void main(String[] args) throws Throwable {

            Component rtpComponent=IceProcess.startIce(5060, OWNNAME,REMOTECOMPUTERNAME);



    }


}

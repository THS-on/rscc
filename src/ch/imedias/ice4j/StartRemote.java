package ch.imedias.ice4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramSocket;
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
public class StartRemote {


    public static final String COMPUTERNAME = "PwgMacbook";
    public static final String REMOTECOMPUTERNAME = "PwgVirtualUbuntu";


    public static void main(String[] args) throws Throwable {

            DatagramSocket udpSocket=IceProcess.startIce(5060, COMPUTERNAME,REMOTECOMPUTERNAME);



    }


}

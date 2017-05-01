package ch.imedias.ice4j;

import udt.*;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by pwigger on 01.05.17.
 */
public class RsccUDTServerSocket extends UDTServerSocket {
    private final UDPEndPoint endpoint;

    public RsccUDTServerSocket(UDPEndPoint endPoint) throws SocketException, UnknownHostException {
        super(InetAddress.getLocalHost(), 1500);
        this.endpoint=endPoint;
    }

    @Override
    public UDPEndPoint getEndpoint() {
        return endpoint;
    }
}

package ch.imedias.rsccfx.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by jp on 08/05/17.
 */
public class RscccfpTest {
  Rscc clientModel;
  Rscc serverModel;
  Rscccfp client;
  Rscccfp server;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }


  /**
   * Test Client
   */
  @Test
  public void testServer() {
    serverModel = new Rscc(new SystemCommander());
    server = new Rscccfp(serverModel);
    server.isServer = true;

    Thread srv = server;

    server.start();

  }

  /**
   * TestClient
   */
  public void testClient() {
    clientModel = new Rscc(new SystemCommander());
    client = new Rscccfp(clientModel);
    client.isServer = false;

    Thread cln = client;

    cln.start();
  }

  /**
   * Test for {@link }
   */
  @Test
  public void testStartRscccfpServer() {
  }

  /**
   * Test for {@link }
   */
  @Test
  public void testStartRscccfpClient() {
  }

  /**
   * Test for {@link }
   */
  @Test
  public void testCloseConnection() {
  }

  /**
   * Test for {@link }
   */
  @Test
  public void testSendSdp() {
  }

  /**
   * Test for {@link }
   */
  @Test
  public void testSendResult() {
  }

}
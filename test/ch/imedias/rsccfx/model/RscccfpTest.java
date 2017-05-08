package ch.imedias.rsccfx.model;

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
   * Test Server.
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
   * TestClient.
   */
  @Test
  public void testClient() {
    clientModel = new Rscc(new SystemCommander());
    client = new Rscccfp(clientModel);
    client.isServer = false;

    Thread cln = client;

    cln.start();
  }


}
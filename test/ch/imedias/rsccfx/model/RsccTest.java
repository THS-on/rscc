package ch.imedias.rsccfx.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;



/**
 * Tests the rscc model class.
 */
public class RsccTest {
  Rscc model;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testKillConnection() throws Exception {
    final String key = "jhd65g7fs2";
    SystemCommander mockCommander =  mock(SystemCommander.class);
    Rscc model = new Rscc(mockCommander);

    //actual test
    model.killConnection(key);
    verify(mockCommander).executeTerminalCommand("bash resources/docker-build_p2p/"
        + "port_stop.sh " + key);
  }
}
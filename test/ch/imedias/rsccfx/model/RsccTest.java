package ch.imedias.rsccfx.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the rscc model class.
 */
public class RsccTest {
  Rscc model;

  @Before
  public void setUp() throws Exception {
    model = new Rscc();
  }

  @Ignore
  @Test
  public void requestTokenFromServer() throws Exception {

    String key = model.requestTokenFromServer(4999, "86.119.39.89", 2201, 800, true);

    assertEquals(10, key.trim().length());

  }

  @Test
  public void sampleTest() {

  }

}
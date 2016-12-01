package ch.imedias.rsccfx.model;

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
  public void killConnection() throws Exception {
    model.killConnection("98e22c98aa");
  }

  @Test
  public void sampleTest() {

  }

}
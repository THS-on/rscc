package ch.imedias.rsccfx.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 *
 */
public class RsccTest {
    Rscc model;
  @Before
  public void setUp() throws Exception {
    model = new Rscc();

  }

  @Test
  public void requestTokenFromServer() throws Exception {

    String key = model.requestTokenFromServer(4999, "86.119.39.89", 2201, true);

    assertEquals(10,key.trim().length());

  }

  @Test
  public void sampleTest() {

  }

}
package ch.imedias.rsccfx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.model.SystemCommander;
import ch.imedias.rsccfx.view.RsccHomePresenter;
import ch.imedias.rsccfx.view.RsccHomeView;
import ch.imedias.rsccfx.view.RsccRequestPresenter;
import ch.imedias.rsccfx.view.RsccRequestView;
import javafx.scene.Node;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;



/**
 * Tests the ViewController class.
 */
public class ViewControllerTest {
  ViewController viewController;
  Node mockRequestView;
  ControlledPresenter mockRequestPresenter;
  Node mockHomeView;
  ControlledPresenter mockHomePresenter;

  /**
   * Initializes test fixture before each test.
   */
  @Before
  public void setUp() {
    viewController = new ViewController();
    mockHomeView =  mock(RsccHomeView.class);
    mockHomePresenter = mock(RsccHomePresenter.class);
    mockRequestView =  mock(RsccRequestView.class);
    mockRequestPresenter = mock(RsccRequestPresenter.class);
  }

  /**
   * Test for {@link ViewController#getPresenter(String)}.
   */
  @Test
  public void testGetPresenter() {
    viewController.loadView("test", mockHomeView, mockHomePresenter);
    ControlledPresenter returnedPresenter = viewController.getPresenter("test");
    assertEquals(mockHomePresenter, returnedPresenter);
  }

  /**
   * Test for {@link ViewController#loadView(String, Node, ControlledPresenter)}.
   */
  @Test
  public void testLoadView() {
    viewController.loadView("test", mockHomeView, mockHomePresenter);
    verify(mockHomePresenter, times(1)).setViewParent(any(ViewController.class));
  }

  /**
   * Test for {@link ViewController#setView(String)}.
   */
  @Test
  public void testSetView() {
    final String requestViewName = "testRequestView";
    final String homeViewName = "testHomeView";
    viewController.loadView(requestViewName, mockRequestView, mockRequestPresenter);
    viewController.loadView(homeViewName, mockHomeView, mockHomePresenter);
    assertTrue(viewController.setView(homeViewName));
  }

  /**
   * Test for {@link ViewController#setView(String)}.
   */
  @Test
  public void testSetViewNotLoadedView() {
    String viewName = "testView";
    String wrongViewName = "test";
    viewController.loadView(viewName, mockHomeView, mockHomePresenter);
    assertFalse(viewController.setView(wrongViewName));
  }

  /**
   * Test for {@link ViewController#unloadView(String)}.
   */
  @Test
  public void testUnloadView() {
    String viewName = "testView";
    viewController.loadView(viewName, mockHomeView, mockHomePresenter);
    assertTrue(viewController.unloadView(viewName));
  }

  /**
   * Test for {@link ViewController#unloadView(String)}.
   */
  @Test
  public void testUnloadViewIllegalArgument() {
    String viewName = "testView";
    String wrongViewName = "test";
    viewController.loadView(viewName, mockHomeView, mockHomePresenter);
    assertFalse(viewController.unloadView(wrongViewName));
  }
}

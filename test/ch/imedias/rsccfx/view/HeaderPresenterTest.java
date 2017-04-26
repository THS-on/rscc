package ch.imedias.rsccfx.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import ch.imedias.rsccfx.model.Rscc;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by jp on 20/04/17.
 */
public class HeaderPresenterTest {
  HeaderPresenter headerPresenter;

  @Mock
  EventHandler<ActionEvent> mockEventHandler;

  @Mock
  Rscc mockModel;

  @Mock
  HeaderView mockHeaderView;

  /**
   * Initializes test fixture before each test .
   */
  @Before
  public void setUp() throws Exception {
    mockModel = mock(Rscc.class);
    headerPresenter = new HeaderPresenter(mockModel, mockHeaderView);
  }

  @Test
  public void testSetBackBtnAction() throws Exception {
    headerPresenter.setBackBtnAction(mockEventHandler);
    EventHandler retunedEventHandler = mockHeaderView.backBtn.getOnAction();
    assertEquals(mockEventHandler, retunedEventHandler);
  }

  @Test
  public void testSetHelpBtnAction() throws Exception {
    headerPresenter.setHelpBtnAction(mockEventHandler);
    EventHandler retunedEventHandler = mockHeaderView.helpBtn.getOnAction();
    assertEquals(mockEventHandler, retunedEventHandler);
  }

  @Test
  public void testSetSettingsBtnAction() throws Exception {
    headerPresenter.setSettingsBtnAction(mockEventHandler);
    EventHandler retunedEventHandler = mockHeaderView.settingsBtn.getOnAction();
    assertEquals(mockEventHandler, retunedEventHandler);
  }

  @Test
  public void initSize() throws Exception {
    Group root = new Group();
    Scene scene = new Scene(root, 300, 300);

    headerPresenter.initSize(scene);

    double viewWitdh = mockHeaderView.prefWidthProperty().getValue();
    double sceneWidth = scene.getWidth();

    assertEquals(sceneWidth, viewWitdh, 1);
  }

  @Test
  public void testSetBackBtnVisibilityTrue() throws Exception {
    headerPresenter.setBackBtnVisibility(true);
    assertTrue(mockHeaderView.backBtn.isVisible());
    headerPresenter.setBackBtnVisibility(false);
    assertFalse(mockHeaderView.backBtn.isVisible());

  }

}

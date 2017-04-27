package ch.imedias.rsccfx.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ch.imedias.rsccfx.model.Rscc;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by jp on 20/04/17.
 */
public class HeaderPresenterTest {
  HeaderPresenter headerPresenter;

  @Mock
  EventHandler<ActionEvent> mockEventHandler;

  @Mock
  Rscc mockModel;


  HeaderView headerView;

  /**
   * Initializes JavaFX to test with JAvaFXElemnts in view.
   */
  @BeforeClass
  public static void initJavaFx() {
    PlatformImpl.startup(() -> {
      //nothing to do
    });
    Platform.setImplicitExit(false);
  }

  /**
   * Initializes test fixture before each test .
   */
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    headerView = new HeaderView(mockModel);
    headerPresenter = new HeaderPresenter(mockModel, headerView);
  }

  @Test
  public void testSetBackBtnAction() throws Exception {

    headerPresenter.setBackBtnAction(mockEventHandler);

    //verify(mockHeaderView, times(1)).setOnAction(any(EventHandler.class));
  }

  @Test
  public void testSetHelpBtnAction() throws Exception {
    headerPresenter.setHelpBtnAction(mockEventHandler);
    EventHandler retunedEventHandler = headerView.helpBtn.getOnAction();
    assertEquals(mockEventHandler, retunedEventHandler);
  }

  @Test
  public void testSetSettingsBtnAction() throws Exception {
    headerPresenter.setSettingsBtnAction(mockEventHandler);
    EventHandler retunedEventHandler = headerView.settingsBtn.getOnAction();
    assertEquals(mockEventHandler, retunedEventHandler);
  }

  @Test
  public void initSize() throws Exception {
    Group root = new Group();
    Scene scene = new Scene(root, 300, 300);

    headerPresenter.initSize(scene);

    double viewWitdh = headerView.prefWidthProperty().getValue();
    double sceneWidth = scene.getWidth();

    assertEquals(sceneWidth, viewWitdh, 1);
  }

  @Test
  public void testSetBackBtnVisibilityTrue() throws Exception {
    headerPresenter.setBackBtnVisibility(true);
    assertTrue(headerView.backBtn.isVisible());
    headerPresenter.setBackBtnVisibility(false);
    assertFalse(headerView.backBtn.isVisible());

  }

}

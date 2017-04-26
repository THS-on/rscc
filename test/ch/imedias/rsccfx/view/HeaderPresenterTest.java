package ch.imedias.rsccfx.view;

import static javafx.application.Application.launch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import ch.imedias.rsccfx.model.Rscc;
import com.sun.xml.internal.ws.server.sei.MessageFiller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by jp on 20/04/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HeaderPresenter.class, ButtonBase.class)
public class HeaderPresenterTest {
  HeaderPresenter headerPresenter;

  EventHandler<ActionEvent> eventHandler = (event) -> { };
  @Mock Rscc mockModel;
  @Mock Button backBtn;
  @Mock Button helpBtn;
  @Mock Button settingsBtn;
  @InjectMocks HeaderView mockHeaderView;

  /**
   * Test JavaFX application:
   * http://stackoverflow.com/questions/11385604/how-do-you-unit-test-a-javafx-controller-with-junit.
   */
  @BeforeClass
  public static void initJavaFx() {
    Thread t = new Thread("JavaFX Init Thread") {
      public void run() {
        Application.launch(AsNonApp.class, new String[0]);
      }
    };
    t.setDaemon(true);
    t.start();
  }

  public static class AsNonApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
      // noop
    }
  }

  /**
   * Initializes test fixture before each test.
   */
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    headerPresenter = new HeaderPresenter(mockModel, mockHeaderView);
  }

  @Test
  public void testSetBackBtnAction() throws Exception {
    suppress(method(HeaderPresenter.class, "setBackBtnAction"));
    suppress(method(ButtonBase.class, "setOnAction"));
    headerPresenter.setBackBtnAction(eventHandler);
    verify(backBtn).setOnAction(eventHandler);
  }

  /*
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
  */

}

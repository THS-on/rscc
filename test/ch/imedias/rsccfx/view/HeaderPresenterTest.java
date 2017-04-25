package ch.imedias.rsccfx.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import ch.imedias.rsccfx.model.Rscc;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by jp on 20/04/17.
 */
public class HeaderPresenterTest {
  EventHandler<ActionEvent> eventHandler;
  HeaderPresenter headerPresenter;
  HeaderView headerView;
  Rscc mockModel;

  /**
   * Initializes test fixture before each test .
   */
  @Before
  public void setUp() throws Exception {
    EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
      }
    };
    mockModel = mock(Rscc.class);
    headerView = new HeaderView(mockModel);
    headerPresenter = new HeaderPresenter(mockModel, headerView);
  }

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


  @Test
  public void testSetBackBtnAction() throws Exception {
    headerPresenter.setBackBtnAction(eventHandler);
    EventHandler retunedEventHandler = headerView.backBtn.getOnAction();
    assertEquals(eventHandler, retunedEventHandler);
  }

  @Test
  public void testSetHelpBtnAction() throws Exception {
    headerPresenter.setHelpBtnAction(eventHandler);
    EventHandler retunedEventHandler = headerView.helpBtn.getOnAction();
    assertEquals(eventHandler, retunedEventHandler);
  }

  @Test
  public void testSetSettingsBtnAction() throws Exception {
    headerPresenter.setSettingsBtnAction(eventHandler);
    EventHandler retunedEventHandler = headerView.settingsBtn.getOnAction();
    assertEquals(eventHandler, retunedEventHandler);
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
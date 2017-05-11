package ch.imedias.rsccfx;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.model.SystemCommander;
import ch.imedias.rsccfx.model.util.KeyUtil;
import ch.imedias.rsccfx.view.RsccHomePresenter;
import ch.imedias.rsccfx.view.RsccHomeView;
import ch.imedias.rsccfx.view.RsccRequestPresenter;
import ch.imedias.rsccfx.view.RsccRequestView;
import ch.imedias.rsccfx.view.RsccSupportPresenter;
import ch.imedias.rsccfx.view.RsccSupportView;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Starts the Rscc Application.
 */
public class RsccApp extends Application {
  private static final Logger LOGGER =
      Logger.getLogger(RsccApp.class.getName());

  public static final String APP_NAME = "Remote Support";

  //2560x1440 ==> 3686400 ==> 4K
  //1920x1080 ==> 2073600 ==> FullHD
  //1440x900  ==> 1296000 ==> MacBook Air
  private static final double borderToFullHD = 1700000;
  private static final double borderTo4K = 2800000;

  /**
   * Declares views for use with ViewController.
   */
  public static final String HOME_VIEW = "home";
  public static final String REQUEST_VIEW = "requestHelp";
  public static final String SUPPORT_VIEW = "supporter";

  private Rscc model;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    setLogLevel(Level.INFO);

    model = new Rscc(new SystemCommander(), new KeyUtil());
    ViewController mainView = new ViewController();

    final Scene scene = new Scene(mainView);

    // Initialize the views and load them into ViewController
    // HomeView
    Node view = new RsccHomeView(model);
    ControlledPresenter presenter = new RsccHomePresenter(model, (RsccHomeView) view);
    mainView.loadView(RsccApp.HOME_VIEW, view, presenter);

    // RequestHelpView
    view = new RsccRequestView(model);
    presenter = new RsccRequestPresenter(model, (RsccRequestView) view);
    mainView.loadView(RsccApp.REQUEST_VIEW, view, presenter);

    // SupporterView
    view = new RsccSupportView(model);
    presenter = new RsccSupportPresenter(model, (RsccSupportView) view);
    mainView.loadView(RsccApp.SUPPORT_VIEW, view, presenter);

    // Set initial screen
    mainView.setView(RsccApp.HOME_VIEW);

    // Get Screensize
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    double screenHeight = primaryScreenBounds.getHeight();
    double screenWidth = primaryScreenBounds.getWidth();

    double resolution = screenHeight * screenWidth;

    //set Stage boundaries to visible bounds of the main screen
    stage.setWidth(screenWidth / 1.8);
    stage.setHeight(screenHeight / 1.5);
    stage.setX(screenWidth / 2 - stage.getWidth() / 2);
    stage.setY(screenHeight / 2 - stage.getHeight() / 2);

    stage.setMinWidth((screenWidth / 1.8) / 1.2);
    stage.setMinHeight((screenHeight / 1.5) / 1.3);

    stage.setScene(scene);
    stage.setTitle(APP_NAME);
    stage.show();

    // Initialize sizing of views
    ((RsccHomePresenter) mainView.getPresenter(HOME_VIEW)).initSize(scene);
    ((RsccRequestPresenter) mainView.getPresenter(REQUEST_VIEW)).initSize(scene);
    ((RsccSupportPresenter) mainView.getPresenter(SUPPORT_VIEW)).initSize(scene);

    // Initialize stylesheets
    /*if(resolution )*/
    String styleSheet = getClass().getClassLoader()
        .getResource("css/styles.css").toExternalForm();

    scene.getStylesheets().add(styleSheet);
  }

  @Override
  public void stop() throws Exception {
    String key = model.getKeyUtil().getKey();
    if (key != null) {
      model.killConnection();
    }
    super.stop();
  }

  private void setLogLevel(Level logLevel) {
    Logger log = LogManager.getLogManager().getLogger("");
    for (Handler h : log.getHandlers()) {
      h.setLevel(logLevel);
    }
  }
}

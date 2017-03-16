package ch.imedias.rsccfx;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.model.SystemCommander;

import ch.imedias.rsccfx.view.RsccRequestHelpPresenter;
import ch.imedias.rsccfx.view.RsccRequestHelpView;
import ch.imedias.rsccfx.view.RsccSupporterPresenter;
import ch.imedias.rsccfx.view.RsccSupporterView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RsccApp extends Application {
  public static final String APP_NAME = "Remote Support";
  Rscc model;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    model = new Rscc(new SystemCommander());
    // RsccHomeView view = new RsccHomeView(model);
    // RsccRequestHelpView view = new RsccRequestHelpView(model);
    RsccSupporterView view = new RsccSupporterView(model);

    // the scene to listen for the focus change
    Scene scene = new Scene(view);
    //Scene supporterScene = new Scene(supporterView);
    // String stSheet = getClass().getClassLoader().getResource("css/HomeStyle.css").toExternalForm();
    String stSheet = getClass().getClassLoader().getResource("css/supporterStyle.css").toExternalForm();
    scene.getStylesheets().add(stSheet);
    //supporterScene.getStylesheets().add(stSheet);
    String headerSheet = getClass().getClassLoader()
        .getResource("css/headerStyle.css").toExternalForm();
   // supporterScene.getStylesheets().add(headerSheet);
     scene.getStylesheets().add(headerSheet);

    //stage.setScene(supporterScene);
     stage.setScene(scene);
     //RsccRequestHelpPresenter presenter = new RsccRequestHelpPresenter(model, view);
    RsccSupporterPresenter presenter = new RsccSupporterPresenter(model, view);

     // supporterPresenter.initSize(supporterScene);
     presenter.initSize(scene);

    stage.setTitle(APP_NAME);

    // Get Screensize.

    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    // Set Stage boundaries to visible bounds of the main screen.

    stage.setWidth(primaryScreenBounds.getWidth() / 1.8);
    stage.setHeight(primaryScreenBounds.getHeight() / 1.5);
    stage.setX(primaryScreenBounds.getWidth() / 2 - stage.getWidth() / 2);
    stage.setY(primaryScreenBounds.getHeight() / 2 - stage.getHeight() / 2);

    stage.setMinWidth((primaryScreenBounds.getWidth() / 1.8) / 1.5);
    stage.setMinHeight((primaryScreenBounds.getHeight() / 1.5) / 1.5);

    stage.show();

  }

//  @Override
//  public void stop() throws Exception {
//    model.killConnection(model.getKey());
//    super.stop();
//  }
}

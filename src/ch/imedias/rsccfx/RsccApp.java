package ch.imedias.rsccfx;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.model.SystemCommander;

import ch.imedias.rsccfx.view.RsccHomePresenter;
import ch.imedias.rsccfx.view.RsccHomeView;
import ch.imedias.rsccfx.view.RsccRequestHelpPresenter;
import ch.imedias.rsccfx.view.RsccRequestHelpView;
import javafx.application.Application;
import javafx.scene.Scene;
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
    // RsccRequestHelpView showTokenView = new RsccRequestHelpView(model);
    // RsccSupporterView enterTokenView = new RsccSupporterView(model);
    // RsccHomeView view = new RsccHomeView(model);
    RsccRequestHelpView view = new RsccRequestHelpView(model);

    // the scene to listen for the focus change
    Scene scene = new Scene(view);
    String stSheet = getClass().getClassLoader().getResource("css/HomeStyle.css").toExternalForm();
    scene.getStylesheets().add(stSheet);
    String headerSheet = getClass().getClassLoader().getResource("css/style.css").toExternalForm();
    scene.getStylesheets().add(headerSheet);

    stage.setWidth(1000);
    stage.setHeight(450);
    stage.setMinWidth(250);
    stage.setMinHeight(300);
    stage.setScene(scene);
    RsccRequestHelpPresenter presenter = new RsccRequestHelpPresenter(model, view);
    presenter.initSize(scene);

    stage.setTitle(APP_NAME);
    stage.setHeight(400);
    stage.setWidth(700);
    stage.show();

  }

  @Override
  public void stop() throws Exception {
    model.killConnection(model.getKey());
    super.stop();
  }
}

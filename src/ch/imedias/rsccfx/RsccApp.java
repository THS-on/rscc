package ch.imedias.rsccfx;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.view.RsccHomeView;
import ch.imedias.rsccfx.view.RsccPresenter;
import ch.imedias.rsccfx.view.RsccView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RsccApp extends Application {
  public static final String APP_NAME = "Rscc";

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    Rscc model = new Rscc();
    RsccHomeView view = new RsccHomeView(model);

    // Must set the scene before creating the presenter that uses
    // the scene to listen for the focus change
    Scene scene = new Scene(view);
    String stylesheet = getClass().getClassLoader().getResource("HomeStyle.css").toExternalForm();
    scene.getStylesheets().add(stylesheet);
    RsccPresenter presenter = new RsccPresenter(model, view);

    stage.setScene(scene);
    stage.setTitle(APP_NAME);
    stage.show();
  }
}

package ch.imedias.rsccfx;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.model.SystemCommander;
import ch.imedias.rsccfx.view.RsccEnterTokenPresenter;
import ch.imedias.rsccfx.view.RsccEnterTokenView;
import ch.imedias.rsccfx.view.RsccPresenter;
import ch.imedias.rsccfx.view.RsccShowTokenPresenter;
import ch.imedias.rsccfx.view.RsccShowTokenView;
import ch.imedias.rsccfx.view.RsccView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RsccApp extends Application {
  public static final String APP_NAME = "Remote Support - Enter Token";

  public static void main(String[] args) {
    Application.launch(args);
  }


  @Override
  public void start(Stage stage) {

    Rscc model = new Rscc(new SystemCommander());
    RsccShowTokenView showTokenView = new RsccShowTokenView(model);
    RsccEnterTokenView enterTokenView = new RsccEnterTokenView(model);
    RsccView view = new RsccView(model, showTokenView, enterTokenView);


    // the scene to listen for the focus change
    Scene scene = new Scene(view);

    RsccPresenter presenter = new RsccPresenter(model, view);
    RsccShowTokenPresenter showTokenPresenter = new RsccShowTokenPresenter(model, showTokenView);
    RsccEnterTokenPresenter enterTokenPresenter = new RsccEnterTokenPresenter(model,
        enterTokenView);

    stage.setWidth(1000);
    stage.setHeight(450);
    stage.setMinWidth(250);
    stage.setMinHeight(300);
    stage.setScene(scene);
    stage.setTitle(APP_NAME);
    stage.show();
  }
}

package ch.imedias.rsccfx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreensFramework extends Application {

  public static final String MAIN_SCREEN = "main";
  public static final String MAIN_SCREEN_FXML = "main.fxml";
  public static final String POKER_SCREEN = "poker";
  public static final String POKER_SCREEN_FXML =
      "poker.fxml";
  public static final String ROULETTE_SCREEN = "roulette";
  public static final String ROULETTE_SCREEN_FXML =
      "roulette.fxml";

  @Override
  public void start(Stage primaryStage) {

    ScreensController mainContainer = new ScreensController();
    mainContainer.loadScreen(ScreensFramework.MAIN_SCREEN,
        ScreensFramework.MAIN_SCREEN_FXML);
    mainContainer.loadScreen(ScreensFramework.POKER_SCREEN,
        ScreensFramework.POKER_SCREEN_FXML);
    mainContainer.loadScreen(ScreensFramework.ROULETTE_SCREEN,
        ScreensFramework.ROULETTE_SCREEN_FXML);

    mainContainer.setScreen(ScreensFramework.MAIN_SCREEN);

    Group root = new Group();
    root.getChildren().addAll(mainContainer);
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}

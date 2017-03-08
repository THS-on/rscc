package ch.imedias.rsccfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class Screen1Controller implements Initializable,
    ControlledScreen {

  ScreensController myController;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  public void setScreenParent(ScreensController screenParent){
    myController = screenParent;
  }

  //any required method here
}

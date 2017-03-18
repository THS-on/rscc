package ch.imedias.rsccfx;

import javafx.scene.Scene;

public interface ControlledPresenter {

  // This method will allow the injection of the Parent viewParent
  public void setViewParent(ViewController viewParent);

  // This method visualizes the size of the gui elements.
  public void initSize(Scene scene);
}

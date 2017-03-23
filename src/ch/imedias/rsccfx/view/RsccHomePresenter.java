package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;

/**
 * Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class RsccHomePresenter implements ControlledPresenter {
  private final Rscc model;
  private final RsccHomeView view;
  private ViewController viewParent;

  /**
   * Initializes a new RsccHomePresenter with the matching view.
   */
  public RsccHomePresenter(Rscc model, RsccHomeView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  /**
   * Defines the ViewController to allow changing of views.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  /**
   * Initializes the size of the RsccHomeView.
   */
  public void initSize(Scene scene) {
    view.supportViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(2));
    view.supportViewBtn.prefHeightProperty().bind(scene.heightProperty());
    view.requestViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(2));
    view.requestViewBtn.prefHeightProperty().bind(scene.heightProperty());
    view.requestSupportImgView.fitWidthProperty().bind(scene.widthProperty().divide(4));
  }

  private void attachEvents() {
    view.supportViewBtn.setOnAction(event -> viewParent.setView(RsccApp.SUPPORT_VIEW));
    view.requestViewBtn.setOnAction(event -> viewParent.setView(RsccApp.REQUEST_VIEW));
  }
}

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
  private static final int VIEW_BTN_DIVISOR = 2;
  private static final int IMG_VIEW_DIVISOR = 4;

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
    view.supportViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(VIEW_BTN_DIVISOR));
    view.supportViewBtn.prefHeightProperty().bind(scene.heightProperty());
    view.requestViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(VIEW_BTN_DIVISOR));
    view.requestViewBtn.prefHeightProperty().bind(scene.heightProperty());
    view.requestImgView.fitWidthProperty().bind(scene.widthProperty().divide(IMG_VIEW_DIVISOR));
    view.supportImgView.fitWidthProperty().bind(scene.widthProperty().divide(IMG_VIEW_DIVISOR));
  }

  private void attachEvents() {
    view.supportViewBtn.setOnAction(event -> viewParent.setView(RsccApp.SUPPORT_VIEW));
    view.requestViewBtn.setOnAction(event -> viewParent.setView(RsccApp.REQUEST_VIEW));
  }
}

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
   * Initializes a new RsccHomePresenter with the according view.
   *
   * @param model the presentation model to coordinate views.
   * @param view  the view which needs to be configured.
   */
  public RsccHomePresenter(Rscc model, RsccHomeView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  /**
   * Defines the ViewController to allow changing views.
   *
   * @param viewParent the controller to be used.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  /**
   * Initializes the size of the whole RsccHomeView elements.
   *
   * @param scene initially loaded scene by RsccApp.
   */
  public void initSize(Scene scene) {
    view.supportViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(2));
    view.supportViewBtn.prefHeightProperty().bind(scene.heightProperty());
    view.requestViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(2));
    view.requestViewBtn.prefHeightProperty().bind(scene.heightProperty());
  }

  private void attachEvents() {
    view.supportViewBtn.setOnAction(event -> viewParent.setView(RsccApp.SUPPORT_VIEW));
    view.requestViewBtn.setOnAction(event -> viewParent.setView(RsccApp.REQUEST_VIEW));
  }
}

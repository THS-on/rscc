package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Presenter class of RsccHomeView. Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class RsccHomePresenter implements ControlledPresenter {
  private final Rscc model;
  private final RsccHomeView view;
  private ViewController viewParent;

  /**
   * Initilizes a new RsccHomePresenter with the according view.
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
   * Definies the ViewController to allow changing views.
   *
   * @param viewParent the controller to be used.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  private void attachEvents() {
    view.requestSupportBtn.setOnAction(event -> showRequestHelpView());
    view.offerSupportBtn.setOnAction(event -> showSupporterView());
  }

  private void showSupporterView() {
    Stage stage = (Stage) view.getScene().getWindow();
    stage.setScene(new Scene(new RsccSupporterView(model)));
  }

  private void showRequestHelpView() {
    Stage stage = (Stage) view.getScene().getWindow();
    stage.setScene(new Scene(new RsccRequestHelpView(model)));
  }
}

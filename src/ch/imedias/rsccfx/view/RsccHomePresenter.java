package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RsccHomePresenter implements ControlledPresenter {
  private final Rscc model;
  private final RsccHomeView view;
  private ViewController viewParent;

  /**
   * Javadoc comment here.
   */
  public RsccHomePresenter(Rscc model, RsccHomeView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  private void attachEvents() {

    view.requestSupportBtn.setOnAction(event -> showRequestHelpView());
    view.offerSupportBtn.setOnAction(event -> showSupporterView());
  }

  private void showSupporterView() {
    Stage stage = (Stage) view.getScene().getWindow();
    stage.setScene(new Scene(new RsccSupportView(model)));
  }

  private void showRequestHelpView() {
    Stage stage = (Stage) view.getScene().getWindow();
    stage.setScene(new Scene(new RsccRequestView(model)));
  }

  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

}

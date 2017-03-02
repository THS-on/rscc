package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RsccHomeViewPresenter {
  private final Rscc model;
  private final RsccHomeView view;

  /**
   * Javadoc comment here.
   */
  public RsccHomeViewPresenter(Rscc model, RsccHomeView view) {
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
    stage.setScene(new Scene(new RsccSupporterView(model)));
  }
  private void showRequestHelpView() {
    Stage stage = (Stage) view.getScene().getWindow();
    stage.setScene(new Scene(new RsccRequestHelpView(model)));
  }
}

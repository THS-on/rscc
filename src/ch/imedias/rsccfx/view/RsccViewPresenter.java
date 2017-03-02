package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RsccViewPresenter {
  private final Rscc model;
  private final RsccHomeView view;

  /**
   * Javadoc comment here.
   */
  public RsccViewPresenter(Rscc model, RsccHomeView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  private void attachEvents() {

    view.requestSupportBtn.setOnAction(event -> nowYouChangeToWhatever());
  }

  private void nowYouChangeToWhatever() {
    Stage stage = (Stage) view.getScene().getWindow();
    stage.setScene(new Scene(new RsccOldView(model)));
  }
}

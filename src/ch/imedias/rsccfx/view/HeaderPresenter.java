package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Lukas Marchesi
 * @date 03.03.2017.
 */
public class HeaderPresenter {
  private final Rscc model;
  private HeaderView view;

  /**
   * Javadoc comment here.
   */
  public HeaderPresenter(Rscc model, HeaderView view) {
    this.model = model;
    this.view = view;
    // attachEvents();
  }

  private void attachEvents() {
    view.backBtn.setOnAction(event -> showHomeView());
  }

  private void showHomeView() {
    Stage stage = (Stage) view.getScene().getWindow();
    stage.setScene(new Scene(new RsccHomeView(model)));
  }
}

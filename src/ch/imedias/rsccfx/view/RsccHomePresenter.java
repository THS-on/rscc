package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ScreenLoader;
import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * TODO: Javadoc comment here.
 */
public class RsccHomePresenter {
  private final Rscc model;
  private final RsccHomeView homeView;
  private final ScreenLoader screenLoader;


  /**
   * TODO: Javadoc comment here.
   */
  public RsccHomePresenter(Rscc model, RsccHomeView homeView, ScreenLoader screenLoader) {
    this.model = model;
    this.homeView = homeView;
    this.screenLoader = screenLoader;
    attachEvents();
  }

  private void attachEvents() {

    homeView.requestSupportBtn.setOnAction(event -> showRequestHelpView());
    homeView.offerSupportBtn.setOnAction(event -> showSupporterView());
  }

  private void showSupporterView() {
    Stage stage = (Stage) homeView.getScene().getWindow();
    stage.setScene(new Scene(new RsccSupporterView(model)));
  }

  private void showRequestHelpView() {
    Stage stage = (Stage) homeView.getScene().getWindow();
    stage.setScene(new Scene(new RsccRequestHelpView(model)));
  }
}

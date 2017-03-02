package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;

/**
 * This is the old GUI of Swing in JavaFX.
 */
public class RsccOldPresenter {
  private final Rscc model;
  private final RsccHomeView view;

  /**
   * Javadoc comment here.
   */
  public RsccOldPresenter(Rscc model, RsccHomeView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  private void attachEvents() {
    //TODO put all setOnAction/addListeners in here
  }
}

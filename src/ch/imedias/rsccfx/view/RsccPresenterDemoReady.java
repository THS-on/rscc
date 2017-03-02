package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;

public class RsccPresenterDemoReady {
  private final Rscc model;
  private final RsccViewDemoReady view;

  /**
   * Javadoc comment here.
   */
  public RsccPresenterDemoReady(Rscc model, RsccViewDemoReady view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  private void attachEvents() {
    //TODO put all setOnAction/addListeners in here
  }
}

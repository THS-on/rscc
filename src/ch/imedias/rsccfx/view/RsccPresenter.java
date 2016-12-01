package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;

public class RsccPresenter {
  private final Rscc model;
  private final ShowTokenView view;

  /** Javadoc comment here. */
  public RsccPresenter(Rscc model, ShowTokenView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  private void attachEvents() {
    //TODO put all setOnAction/addListeners in here
  }
}

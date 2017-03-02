package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;

public class RsccPresenter {
  private final Rscc model;
  private final RsccView view;

  // FIXME: Use this as main Presenter and base all of the presenters around it on this
  /**
   * Javadoc comment here.
   */
  public RsccPresenter(Rscc model, RsccView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  private void attachEvents() {
    //TODO put all setOnAction/addListeners in here
  }
}

package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;

public class RsccShowTokenPresenter {
  private final Rscc model;
  private final RsccShowTokenView view;

  // For the moment, hardcoded the server parameters // FIXME: get rid of hardcoded server parameters
  private static final int FORWARDING_PORT = 5900;
  private static final int KEY_SERVER_SSH_PORT = 2201;
  private static final String KEY_SERVER_IP = "86.119.39.89";
  private static final int KEY_SERVER_HTTP_PORT = 800;
  private static final boolean IS_COMPRESSION_ENABLED = true;
  String key = "";

  /**
   * Javadoc comment here. // FIXME: add proper comment
   */
  public RsccShowTokenPresenter(Rscc model, RsccShowTokenView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  private void attachEvents() {
    //TODO put all setOnAction/addListeners in here
    view.reloadButton.setOnAction(
        event -> {
          String newKey = model.refreshKey(model.getKey(), FORWARDING_PORT, KEY_SERVER_IP,
              KEY_SERVER_SSH_PORT, KEY_SERVER_HTTP_PORT, IS_COMPRESSION_ENABLED);
          model.keyProperty().set(newKey);
        }
    );
  }
}

package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;

/**
 * TODO: Javadoc comment here.
 */
public class RsccRequestPresenter {
  private final Rscc model;
  private final RsccRequestView view;

  // For the moment, hardcoded the server parameters
  private static final int FORWARDING_PORT = 5900;
  private static final int KEY_SERVER_SSH_PORT = 2201;
  private static final String KEY_SERVER_IP = "86.119.39.89";
  private static final int KEY_SERVER_HTTP_PORT = 800;
  private static final boolean IS_COMPRESSION_ENABLED = true;
  String key = "";

  /**
   * TODO: Javadoc comment here.
   * @param model
   * @param view
   */
  public RsccRequestPresenter(Rscc model, RsccRequestView view) {
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

    // TODO: Set actions on buttons (back, Help, Settings)

  }

  /**
   * TODO: Javadoc comment here.
   * initSize method
   * @param scene
   */
  public void initSize(Scene scene) {
    view.topBox.prefWidthProperty().bind(scene.widthProperty());
    view.generatedKeyFld.prefWidthProperty().bind(scene.widthProperty().subtract(80));
    view.descriptionTxt.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    view.additionalDescriptionTxt.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    view.headerPresenter.initSize(scene);
  }


}

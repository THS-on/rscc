package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.image.Image;

/**
 * This is the presenter for the EnterTokenView in which the supporter will enter the token. // FIXME: Update to conform with the google java style guidelines
 */
public class RsccEnterTokenPresenter {

  private final Rscc model;
  private final RsccEnterTokenView view;

  // For the moment, hardcoded the server parameters // FIXME: get rid of hardcoded server parameters
  private static final int FORWARDING_PORT = 5900;
  private static final int KEY_SERVER_SSH_PORT = 2201;
  private static final String KEY_SERVER_IP = "86.119.39.89";
  private static final int KEY_SERVER_HTTP_PORT = 800;
  private static final boolean IS_COMPRESSION_ENABLED = true;
  String key = "";

  /**
   * Initializes the RsccEnterTokenPresenter.
   */
  public RsccEnterTokenPresenter(Rscc model, RsccEnterTokenView view) {
    this.model = model;
    this.view = view;

    attachEvents();
  }

  /**
   * Updates the validation image after every key pressed.
   */
  private void attachEvents() {

    view.tokentxt.setOnKeyPressed(event -> {
      view.isValidimg.setImage(new Image(validationImage(view.tokentxt.getText())));
    });

    view.connectbtn.setOnAction(
        event -> {
          model.keyProperty().set(view.tokentxt.getText());
          model.connectToUser(model.getKey(),FORWARDING_PORT,KEY_SERVER_IP,
              KEY_SERVER_HTTP_PORT);
        }
    );

  }

  /**
   * Validates the token and displays a symbolic image.
   */
  public String validationImage(String token) { // FIXME: method names should be formed as "verb + noun", so here it would be "validateImage"
                                                // FIXME: does this need to be public?
    if (validateToken(token)) {
      return getClass().getClassLoader().getResource("emblem-default.png").toExternalForm();
    }
    return getClass().getClassLoader().getResource("dialog-error.png").toExternalForm();
  }

  private static boolean validateToken(String token) {    // FIXME: why is this static? what about the TODO?
    return (int) (Math.random() * 2) == 1;
    //TODO Validate token
  }
}

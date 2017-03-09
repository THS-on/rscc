package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.image.Image;

/**
 * This is the presenter for the EnterTokenView in which the supporter will enter the token.
 * Validates the token and displays an image.
 */
public class RsccSupportPresenter implements ControlledPresenter {

  private final Rscc model;
  private final RsccSupportView view;
  private ViewController viewParent;

  // For the moment, hardcoded the server parameters // FIXME: get rid of hardcoded server parameters
  private static final int FORWARDING_PORT = 5900;
  private static final int KEY_SERVER_SSH_PORT = 2201;
  private static final String KEY_SERVER_IP = "86.119.39.89";
  private static final int KEY_SERVER_HTTP_PORT = 800;
  private static final boolean IS_COMPRESSION_ENABLED = true;
  String key = "";

  /**
   * Initializes the RsccSupportPresenter.
   */
  public RsccSupportPresenter(Rscc model, RsccSupportView view) {
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

    // TODO: Set actions on buttons (back, Help, Settings)

  }

  /**
   * Validates the token and displays a symbolic image.
   */
  public String validateImage(String token) { 
                                                // FIXME: does this need to be public? (not sure anymore)
    if (validateToken(token)) {
      return getClass().getClassLoader().getResource("emblem-default.png").toExternalForm();
    }
    return getClass().getClassLoader().getResource("dialog-error.png").toExternalForm();
  }

  private static boolean validateToken(String token) {    // FIXME: why is this static? (not sure anymore)
    return (int) (Math.random() * 2) == 1;
    //TODO Validate token
  }

  public void setViewParent(ViewController viewParent){
    this.viewParent = viewParent;
  }

}

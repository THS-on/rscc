package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * Presenter class of RsccSupporterView. Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 * The supporter can enter the key given from the help requester to establish a connection.
 */
public class RsccSupporterPresenter implements ControlledPresenter {
  private final Rscc model;
  private final RsccSupporterView view;
  private ViewController viewParent;

  // For the moment, hardcoded the server parameters
  private static final int FORWARDING_PORT = 5900;
  private static final int KEY_SERVER_SSH_PORT = 2201;
  private static final String KEY_SERVER_IP = "86.119.39.89";
  private static final int KEY_SERVER_HTTP_PORT = 800;
  private static final boolean IS_COMPRESSION_ENABLED = true;
  String key = "";

  /**
   * Initializes a new RsccSupporterPresenter with the according view.
   *
   * @param model the presentation model to coordinate views.
   * @param view  the view which needs to be configured.
   */
  public RsccSupporterPresenter(Rscc model, RsccSupporterView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  /**
   * Defines the ViewController to allow changing views.
   *
   * @param viewParent the controller to be used.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  /**
   * Initializes the size of the whole RsccSupporterView elements.
   *
   * @param scene initially loaded scene by RsccApp.
   */

  public void initSize(Scene scene) {
    view.topBox.prefWidthProperty().bind(scene.widthProperty());
    view.enterTokenlbl.prefWidthProperty().bind(scene.widthProperty().subtract(80));
    view.headerPresenter.initSize(scene);
  }

  /**
   * Validates the token and displays a symbolic image.
   *
   * @param token the token to be validated.
   * @return path to the image to display.
   */
  public String validationImage(String token) {

    if (validateToken(token)) {
      return getClass().getClassLoader().getResource("emblem-default.png").toExternalForm();
    }
    return getClass().getClassLoader().getResource("dialog-error.png").toExternalForm();
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
          model.connectToUser(model.getKey(), FORWARDING_PORT, KEY_SERVER_IP,
              KEY_SERVER_HTTP_PORT);
        }
    );

    // TODO: Set actions on buttons (back, Help, Settings)
  }

  /**
   * Validates a token.
   *
   * @param token the token to be validated.
   * @return true or false.
   */
  private static boolean validateToken(String token) {
    return (int) (Math.random() * 2) == 1;
    //TODO: Validate token
  }

}

package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * Presenter class of RsccSupportView. Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 * The supporter can enter the key given from the help requester to establish a connection.
 */
public class RsccSupportPresenter implements ControlledPresenter {
  private static final Logger LOGGER =
      Logger.getLogger(RsccSupportPresenter.class.getName());

  private static final double WIDTH_SUBTRACTION_ENTERTOKEN = 80d;

  private final Rscc model;
  private final RsccSupportView view;
  private final HeaderPresenter headerPresenter;
  private ViewController viewParent;

  /**
   * Initializes a new RsccSupportPresenter with the according view.
   */
  public RsccSupportPresenter(Rscc model, RsccSupportView view) {
    this.model = model;
    this.view = view;
    headerPresenter = new HeaderPresenter(model, view.headerView);
    attachEvents();
    initHeader();
  }

  /**
   * Validates a token.
   */
  private static boolean validateToken(String token) {
    return token.matches("\\d{9}");
  }

  /**
   * Defines the ViewController to allow changing of views.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  /**
   * Initializes the size of the whole RsccSupportView elements.
   *
   * @param scene must be initialized and displayed before calling this method;
   *              The size of all header elements are based on it.
   * @throws NullPointerException if called before this object is fully initialized.
   */
  public void initSize(Scene scene) {
    // initialize header
    headerPresenter.initSize(scene);

    // initialize view
    view.enterTokenLbl.prefWidthProperty().bind(scene.widthProperty()
        .subtract(WIDTH_SUBTRACTION_ENTERTOKEN));
  }

  /**
   * Validates the token and displays a symbolic image.
   *
   * @param token the token to be validated.
   * @return path to the image to display.
   */
  public Image validationImage(String token) {

    if (validateToken(token)) {
      view.connectBtn.setDisable(false);
      return new Image(getClass().getClassLoader().getResource("emblem-default.png")
          .toExternalForm());
    }
    view.connectBtn.setDisable(true);
    return new Image(getClass().getClassLoader().getResource("dialog-error.png")
        .toExternalForm());
  }

  /**
   * Updates the validation image after every key pressed.
   */
  private void attachEvents() {

    view.tokenFld.setOnKeyReleased(event -> {
      view.isValidImg.setImage(validationImage(view.tokenFld.getText()));
    });


    view.connectBtn.setOnAction(event -> {
      model.setKey(view.tokenFld.getText());
      model.connectToUser();
    });


    // Closes the other TitledPane so that just one TitledPane is shown on the screen.
    view.keyInputPane.setOnMouseClicked(event -> view.predefinedAdressesPane.setExpanded(false));
    view.predefinedAdressesPane.setOnMouseClicked(event -> view.keyInputPane.setExpanded(false));
  }



  /**
   * Initializes the functionality of the header, e.g. back and settings button.
   */
  private void initHeader() {
    // Set all the actions regarding buttons in this method.
    headerPresenter.setBackBtnAction(event -> viewParent.setView("home"));
    // TODO: Set actions on buttons (Help, Settings)
  }

}

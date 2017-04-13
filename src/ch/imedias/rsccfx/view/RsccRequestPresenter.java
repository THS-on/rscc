package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;

/**
 * Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class RsccRequestPresenter implements ControlledPresenter {
  // For the moment, hardcoded the server parameters
  private static final int FORWARDING_PORT = 5900;
  private static final int KEY_SERVER_SSH_PORT = 2201;
  private static final String KEY_SERVER_IP = "86.119.39.89";
  private static final int KEY_SERVER_HTTP_PORT = 800;
  private static final boolean IS_COMPRESSION_ENABLED = true;
  private final Rscc model;
  private final RsccRequestView view;
  HeaderPresenter headerPresenter;
  String key = "";
  private ViewController viewParent;

  /**
   * Initializes a new RsccRequestPresenter with the matching view.
   */
  public RsccRequestPresenter(Rscc model, RsccRequestView view) {
    this.model = model;
    this.view = view;
    headerPresenter = new HeaderPresenter(model, view.headerView);
    attachEvents();
    initHeader();
  }

  /**
   * Defines the ViewController to allow changing views.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  private void attachEvents() {
    //TODO put all setOnAction/addListeners in here
    // FIXME: Please fix it.
    /* view.reloadKeyBtn.setOnAction(
        event -> {
          String newKey = model.refreshKey(model.getKey(), FORWARDING_PORT, KEY_SERVER_IP,
              KEY_SERVER_SSH_PORT, KEY_SERVER_HTTP_PORT, IS_COMPRESSION_ENABLED);
          model.keyProperty().set(newKey);
        }
    );*/

    // TODO: Set actions on buttons (back, Help, Settings)

    // Closes the other TitledPane so that just one TitledPane is shown on the screen.
    view.keyGeneratorPane.setOnMouseClicked(event -> view.supporterAdminPane.setExpanded(false));
    view.supporterAdminPane.setOnMouseClicked(event -> view.keyGeneratorPane.setExpanded(false));
  }

  /**
   * Initializes the size of the whole RsccRequestView elements.
   *
   * @param scene must be initialized and displayed before calling this method;
   *              The size of all header elements are based on it.
   * @throws NullPointerException if called before this object is fully initialized.
   */
  public void initSize(Scene scene) {
    // initialize header
    headerPresenter.initSize(scene);

    // initialize view
    view.generatedKeyFld.prefWidthProperty().bind(scene.widthProperty().subtract(80));
    view.descriptionTxt.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    view.additionalDescriptionTxt.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    view.keyGeneratingBox.prefWidthProperty().bind(scene.widthProperty());
  }

  /**
   * Initializes the functionality of the header, e.g. back button and settings button.
   */
  private void initHeader() {
    // Set all the actions regarding buttons in this method.
    headerPresenter.setBackBtnAction(event -> viewParent.setView("home"));
  }
}

package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

  private static final double WIDTH_SUBTRACTION_ENTERKEY = 80d;
  private final Image validImage =
      new Image(getClass().getClassLoader().getResource("emblem-default.png").toExternalForm());
  private final Image invalidImage =
      new Image(getClass().getClassLoader().getResource("dialog-error.png").toExternalForm());

  private final BooleanProperty keyValidityProperty = new SimpleBooleanProperty(false);

  private final Rscc model;
  private final RsccSupportView view;
  private final HeaderPresenter headerPresenter;
  private ViewController viewParent;
  private PopOverHelper popOverHelper;

  /**
   * Initializes a new RsccSupportPresenter with the according view.
   *
   * @param model model with all data.
   * @param view the view belonging to the presenter.
   */
  public RsccSupportPresenter(Rscc model, RsccSupportView view) {
    this.model = model;
    this.view = view;
    headerPresenter = new HeaderPresenter(model, view.headerView);
    attachEvents();
    initHeader();
    initBindings();
    popOverHelper = new PopOverHelper(model, RsccApp.SUPPORT_VIEW);
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
    view.titleLbl.prefWidthProperty().bind(scene.widthProperty()
        .subtract(WIDTH_SUBTRACTION_ENTERKEY));

    // FIXME: Magic numbeeer.
    view.centerBox.prefHeightProperty().bind(scene.heightProperty()
        .subtract(159d));

    view.keyInputPane.prefWidthProperty().bind(scene.widthProperty());
  }

  /**
   * Updates the validation image after every key pressed.
   */
  private void attachEvents() {
    // update keyValidityProperty property every time the textfield with the key changes
    view.keyFld.textProperty().addListener(
        (observable, oldKey, newKey) -> keyValidityProperty.set(model.validateKey(newKey))
    );

    view.connectBtn.setOnAction(event -> {
      model.setKey(view.keyFld.getText());
      model.connectToUser();
    });

    // Closes the other TitledPane so that just one TitledPane is shown on the screen.
    view.keyInputPane.setOnMouseClicked(event -> view.addressbookPane.setExpanded(false));
    view.addressbookPane.setOnMouseClicked(event -> view.keyInputPane.setExpanded(false));
  }

  private void initBindings() {
    // disable connect button if key is NOT valid
    view.connectBtn.disableProperty().bind(keyValidityProperty.not());

    // bind validation image to keyValidityProperty
    view.validationImgView.imageProperty().bind(
        Bindings.when(keyValidityProperty)
            .then(validImage)
            .otherwise(invalidImage)

    );
  }

  /**
   * Initializes the functionality of the header, e.g. back and settings button.
   */
  private void initHeader() {
    // Set all the actions regarding buttons in this method.
    headerPresenter.setBackBtnAction(event -> viewParent.setView("home"));
    headerPresenter.setHelpBtnAction(event ->
        popOverHelper.helpPopOver.show(view.headerView.helpBtn));
    headerPresenter.setSettingsBtnAction(event ->
        popOverHelper.settingsPopOver.show(view.headerView.settingsBtn));
    // TODO: Set actions on buttons (Help, Settings)
  }

}

package ch.imedias.rsccfx.view;

import static ch.imedias.rscc.RemoteSupportFrame.getDefaultList;

import ch.imedias.rscc.SupportAddress;
import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class RsccRequestPresenter implements ControlledPresenter {
  private static final Logger LOGGER =
      Logger.getLogger(RsccRequestPresenter.class.getName());
  private static final double WIDTH_SUBTRACTION_GENERAL = 50d;
  private static final double WIDTH_SUBTRACTION_KEYFIELD = 100d;
  private static final int GRID_MAXIMUM_COLUMNS = 3;
  private static final String SUPPORT_ADDRESSES = "supportAddresses";

  private final Rscc model;
  private final RsccRequestView view;
  private final HeaderPresenter headerPresenter;
  private ViewController viewParent;
  private PopOverHelper popOverHelper;

  private ArrayList<Button> buttons = new ArrayList<>();
  private int rowSize = 0;

  private List<SupportAddress> supportAddresses;
  private final Preferences preferences = Preferences.userNodeForPackage(RsccApp.class);

  /**
   * Initializes a new RsccRequestPresenter with the matching view.
   *
   * @param model model with all data.
   * @param view the view belonging to the presenter.
   */
  public RsccRequestPresenter(Rscc model, RsccRequestView view) {
    this.model = model;
    this.view = view;
    headerPresenter = new HeaderPresenter(model, view.headerView);
    attachEvents();
    initHeader();
    initSupporterList();
    popOverHelper = new PopOverHelper(model, RsccApp.REQUEST_VIEW);
  }

  /**
   * Defines the ViewController to allow changing views.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  private void attachEvents() {
    view.reloadKeyBtn.setOnAction(
        event -> model.refreshKey()
    );

    // Closes the other TitledPane so that just one TitledPane is shown on the screen.
    view.keyGeneratorPane.setOnMouseClicked(
        event -> view.predefinedAddressesPane.setExpanded(false)
    );
    view.predefinedAddressesPane.setOnMouseClicked(
        event -> view.keyGeneratorPane.setExpanded(false)
    );
    attachButtonEvents();
  }

  private void attachButtonEvents() {
    for (Button b:buttons) {
      b.setOnMouseClicked(event ->
          new SupporterAttributesDialog());
    }

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
    // TODO: requestHelpView --> generatedKeyFld should not take the whole width!
    view.generatedKeyFld.prefWidthProperty().bind(scene.widthProperty()
        .subtract(WIDTH_SUBTRACTION_KEYFIELD));
    view.descriptionLbl.prefWidthProperty().bind(scene.widthProperty()
        .subtract(WIDTH_SUBTRACTION_GENERAL));
    view.keyGeneratorPane.prefWidthProperty().bind(scene.widthProperty());
    view.keyGeneratorPane.maxWidthProperty().bind(scene.widthProperty());

    view.predefinedAddressesPane.prefWidthProperty().bind(scene.widthProperty());
    view.predefinedAddressesPane.maxWidthProperty().bind(scene.widthProperty());

    // FIXME: need the height of the titlePane itself... or magic number. François
    view.centerBox.prefHeightProperty().bind(scene.heightProperty()
        .subtract(195d));

    view.keyGeneratorPane.prefWidthProperty().bind(scene.widthProperty());

    view.predefinedAdressessBox.prefHeightProperty().bind(scene.heightProperty()
        .subtract(155d));

    view.supporterDescriptionLbl.prefWidthProperty().bind(scene.widthProperty().divide(3));
    view.supporterGrid.prefWidthProperty().bind(scene.widthProperty().divide(3).multiply(2));

  }

  /**
   * Initializes the functionality of the header, e.g. back button and settings button.
   */
  private void initHeader() {
    // Set all the actions regarding buttons in this method.
    headerPresenter.setBackBtnAction(event -> {
      model.killConnection();
      saveSupporterList(); // TODO make this an action on the "save button"
      viewParent.setView(RsccApp.HOME_VIEW);
    });
    headerPresenter.setHelpBtnAction(event ->
        popOverHelper.helpPopOver.show(view.headerView.helpBtn));
    headerPresenter.setSettingsBtnAction(event ->
        popOverHelper.settingsPopOver.show(view.headerView.settingsBtn));
  }

  /**
   * Calls createSupporterList() and creates a button for every supporter found.
   */

  private void initSupporterList() {
    createSupporterList();
    for (int counter = 0; counter < supportAddresses.size(); counter++) {
      createNewSupporterBtn();
      // TODO: connect to the right GUI component
      buttons.get(counter).textProperty().set(supportAddresses.get(counter).getAddress() + "\n"
          + supportAddresses.get(counter).getDescription());
    }
    createNewSupporterBtn();
  }

  /**
   * Gets the supporter list.
   * If no preferences are found the defaultList is generated.
   */
  private void createSupporterList() {
    // load preferences
    String supportAddressesXml = preferences.get(SUPPORT_ADDRESSES, null);
    if (supportAddressesXml == null) {
      // use some hardcoded defaults
      supportAddresses = getDefaultList();
    } else {
      byte[] array = supportAddressesXml.getBytes();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
      XMLDecoder decoder = new XMLDecoder(inputStream);
      supportAddresses = (List<SupportAddress>) decoder.readObject();
    }
  }

  /**
   * Saves the preferences made by the user.
   */
  private void saveSupporterList() {
    // save preferences
    ByteArrayOutputStream byteArrayOutputStream =
        new ByteArrayOutputStream();
    XMLEncoder encoder = new XMLEncoder(byteArrayOutputStream);
    encoder.setPersistenceDelegate(SupportAddress.class,
        SupportAddress.getPersistenceDelegate());
    encoder.writeObject(supportAddresses);
    encoder.close();
    String supportAddressesXml = byteArrayOutputStream.toString();
    preferences.put(SUPPORT_ADDRESSES, supportAddressesXml);
  }

  /**
   * Creates new SupporterButton and adds it to the GridPane.
   */
  private void createNewSupporterBtn() {

    Button supporter = new Button("+");
    supporter.getStyleClass().add("supporterBtn");

    buttons.add(supporter);

    int buttonSize = buttons.size() - 1;

    if (buttonSize % GRID_MAXIMUM_COLUMNS == 0) {
      rowSize++;
    }
    view.supporterGrid.add(buttons.get(buttonSize), buttonSize % GRID_MAXIMUM_COLUMNS, rowSize);
    buttons.get(buttonSize).setOnAction(event -> createNewSupporterBtn());
    // FIXME: Throws IndexOutOfBoundsException, because 1 - 2 is -1. And yes, we can.
    if (buttonSize > 1) {    // IndexOutOfBoundsException fix.
      buttons.get(buttonSize - 1).setOnAction(null);
    } else if (buttonSize > 0) {
      buttons.get(0).setOnAction(null);
    }
    attachButtonEvents();
  }
}

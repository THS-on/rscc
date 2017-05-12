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
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

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
  private final Preferences preferences = Preferences.userNodeForPackage(RsccApp.class);
  private ViewController viewParent;
  private PopOverHelper popOverHelper;
  private ArrayList<Button> buttons = new ArrayList<>();
  private int rowSize = 0;
  private List<SupportAddress> supportAddresses;

  /**
   * Initializes a new RsccRequestPresenter with the matching view.
   *
   * @param model model with all data.
   * @param view  the view belonging to the presenter.
   */
  public RsccRequestPresenter(Rscc model, RsccRequestView view) {
    this.model = model;
    this.view = view;
    headerPresenter = new HeaderPresenter(model, view.headerView);
    attachEvents();
    attachButtonEvents();
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
        event -> {
          Thread thread = new Thread(model::refreshKey);
          thread.start();
        }
    );

    // handles TitledPane switching between the two TitledPanes
    view.keyGenerationTitledPane.expandedProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (oldValue != newValue) {
            if (newValue) {
              view.predefinedAddressesTitledPane.setExpanded(false);
              view.contentBox.getChildren().removeAll(view.predefinedAdressesInnerBox);
              view.contentBox.getChildren().add(1, view.keyGenerationInnerPane);
            }
          }
        }
    );
    view.predefinedAddressesTitledPane.expandedProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (oldValue != newValue) {
            if (newValue) {
              view.keyGenerationTitledPane.setExpanded(false);
              view.contentBox.getChildren().removeAll(view.keyGenerationInnerPane);
              view.contentBox.getChildren().add(2, view.predefinedAdressesInnerBox);
            }
          }
        }
    );

    model.connectionStatusStyleProperty().addListener((observable, oldValue, newValue) -> {
      Platform.runLater(() -> {
        view.statusBox.getStyleClass().clear();
        view.statusBox.getStyleClass().add(newValue);
      });
    });

    model.connectionStatusTextProperty().addListener((observable, oldValue, newValue) -> {
      Platform.runLater(() -> {
        view.statusLbl.textProperty().set(newValue);
      });
    });

    attachButtonEvents();
  }

  private void attachButtonEvents() {
    for (Button b : buttons) {
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
    // initialize view
    view.generatedKeyFld.prefWidthProperty().bind(scene.widthProperty()
          .subtract(WIDTH_SUBTRACTION_KEYFIELD));
    view.supporterDescriptionLbl.prefWidthProperty().bind(scene.widthProperty().divide(3));
    view.supporterGrid.prefWidthProperty().bind(scene.widthProperty().divide(3).multiply(2));
    view.reloadKeyBtn.prefHeightProperty().bind(view.generatedKeyFld.heightProperty());

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
    createNewSupporterBtn();
    createSupporterList();
    for (int counter = 0; counter < supportAddresses.size(); counter++) {
      createNewSupporterBtn();
      // TODO: connect to the right GUI component
      buttons.get(counter).textProperty().set(supportAddresses.get(counter).getAddress() + "\n"
          + supportAddresses.get(counter).getDescription());
    }


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
    attachContextMenu(supporter);
    supporter.getStyleClass().add("supporterBtn");

    initButtonSize(supporter);

    attachButtonEvents();

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
  }

  private void attachContextMenu(Button button) {

    // Create ContextMenu
    ContextMenu contextMenu = new ContextMenu();

    MenuItem editMenuItem = new MenuItem("Edit");
    editMenuItem.setOnAction(event -> new SupporterAttributesDialog());


    MenuItem connectMenuItem = new MenuItem("Call");
    connectMenuItem.setOnAction(event -> {
      /*TODO start connection*/

    });

    // Add MenuItem to ContextMenu
    contextMenu.getItems().addAll(editMenuItem, connectMenuItem);

    // When user right-click on Supporterbutton
    button.setOnContextMenuRequested(event -> contextMenu.show(button, event.getScreenX(),
        event.getScreenY()));
  }
  private void initButtonSize(Button button) {
    GridPane.setVgrow(button, Priority.ALWAYS);
    GridPane.setHgrow(button, Priority.ALWAYS);
    GridPane.setValignment(button, VPos.CENTER);
    GridPane.setHalignment(button, HPos.CENTER);
    GridPane.setMargin(button, new Insets(10));

    button.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
    button.setPadding(new Insets(20));

  }
}

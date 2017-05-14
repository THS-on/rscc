package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;

import ch.imedias.rsccfx.model.xml.Supporter;
import ch.imedias.rsccfx.model.xml.SupporterHelper;
import java.util.List;
import java.util.logging.Logger;

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
  private final Rscc model;
  private final RsccRequestView view;
  private final HeaderPresenter headerPresenter;
  private final SupporterHelper supporterHelper;
  private ViewController viewParent;
  private PopOverHelper popOverHelper;
  private int buttonSize = 0;
  public static List<Supporter> supporters;


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
    supporterHelper = new SupporterHelper();
    initHeader();
    initSupporterList();
    attachEvents();
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
        .subtract(200d));

    view.keyGeneratorPane.prefWidthProperty().bind(scene.widthProperty());

    view.predefinedAdressessBox.prefHeightProperty().bind(scene.heightProperty()
        .subtract(159d));

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
      viewParent.setView(RsccApp.HOME_VIEW);
    });
    headerPresenter.setHelpBtnAction(event ->
        popOverHelper.helpPopOver.show(view.headerView.helpBtn));
    headerPresenter.setSettingsBtnAction(event ->
        popOverHelper.settingsPopOver.show(view.headerView.settingsBtn));
  }

  /**
   * creates a button for every supporter in the supporter list.
   */

  public void initSupporterList() {
    supporters = supporterHelper.loadSupporters();
    // check if invalid format of XML was found during loading
    if (supporters == null) {
      supporters = supporterHelper.getDefaultSupporters();
      supporterHelper.saveSupporters(supporters);
    }

    supporters.stream().forEachOrdered(this::createNewSupporterBtn);

    createNewSupporterBtn(new Supporter());
  }

  /**
   * Creates new SupporterButton and adds it to the GridPane.
   */
  public void createNewSupporterBtn(Supporter supporter) {

    Button supporterBtn = new Button(supporter.toString());
    supporterBtn.getStyleClass().add("supporterBtn");

    supporterBtn.setOnAction(event -> new SupporterAttributesDialog(supporter));

    int row = ((buttonSize - 1) / GRID_MAXIMUM_COLUMNS) + 1;
    int column = ((buttonSize -1) % GRID_MAXIMUM_COLUMNS) + 1;
    view.supporterGrid.add(supporterBtn, column, row);
    buttonSize++;
  }
}

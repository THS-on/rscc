package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;

/**
 * Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class RsccRequestPresenter implements ControlledPresenter {
  private static final Logger LOGGER =
      Logger.getLogger(RsccRequestPresenter.class.getName());
  private static final double WIDTH_SUBTRACTION_GENERAL = 50d;
  private static final double WIDTH_SUBTRACTION_KEYFIELD = 100d;

  private final Rscc model;
  private final RsccRequestView view;
  private final HeaderPresenter headerPresenter;
  private ViewController viewParent;

  private ArrayList<Button> buttons = new ArrayList<>();
  private int rowSize = 0;

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

    view.btn6.setOnAction(event -> createNewSupporterBtn());

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
        .subtract(159d));

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
  }

  /**
   * Creates new SupporterButton and adds it to the GridPane.
   */
  private void createNewSupporterBtn() {
    int counter = buttons.size();
    Button supporter = new Button("+");

    buttons.add(supporter);

    if((buttons.size())%3 == 0)
      rowSize++;
    view.supporterGrid.add(buttons.get(buttons.size()-1), buttons.size()%3, rowSize);
    buttons.get(buttons.size()-1).setOnAction(event -> createNewSupporterBtn());
    // FIXME: Throws IndexOutOfBoundsException, because 1 - 2 is -1. And yes, we can.
    if(buttons.size()> 2)     // IndexOutOfBoundsException fix.
      buttons.get(buttons.size()-2).setOnAction(null);
  }
}

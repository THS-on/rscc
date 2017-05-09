package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
/**
 * Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class RsccRequestPresenter implements ControlledPresenter {
  private static final Logger LOGGER =
      Logger.getLogger(RsccRequestPresenter.class.getName());
  private static final double WIDTH_SUBTRACTION_GENERAL = 50d;
  private static final double WIDTH_SUBTRACTION_KEYFIELD = 100d;
  public static final int GRID_MAXIMUM_COLUMNS = 3;

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
    initSupporterListFromFile();
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

    view.btn7.setOnAction(event -> createNewSupporterBtn());
    view.btn1.setOnAction(event -> new SupporterAttributesDialog());



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

    Button supporter = new Button("+");
    supporter.getStyleClass().add("supporterBtn");

    buttons.add(supporter);

    int buttonSize = buttons.size()-1;

    if(buttonSize% GRID_MAXIMUM_COLUMNS == 0)
      rowSize++;

    view.supporterGrid.add(buttons.get(buttonSize), buttonSize%GRID_MAXIMUM_COLUMNS, rowSize);
    buttons.get(buttonSize).setOnAction(event -> createNewSupporterBtn());
    // FIXME: Throws IndexOutOfBoundsException, because 1 - 2 is -1. And yes, we can.
    if(buttonSize > 1)     // IndexOutOfBoundsException fix.
      buttons.get(buttonSize-1).setOnAction(null);
    else if (buttonSize > 0)
      buttons.get(0).setOnAction(null);
  }

  private void initSupporterListFromFile() {
    // TODO: Jan implements this feature. Thank you Jan!

    buttons.add(view.btn1);
    buttons.add(view.btn2);
    buttons.add(view.btn3);
    rowSize++;
    buttons.add(view.btn4);
    buttons.add(view.btn5);
    buttons.add(view.btn6);
    rowSize++;
    buttons.add(view.btn7);
  }

  private void createDialogPane(){
    Dialog dialog = new Dialog();
    DialogPane dialogPane = new DialogPane();
    GridPane gridPane = new GridPane();

    final Label nameLbl = new Label("Name");
    final Label adressLbl = new Label("Adress");
    final Label portLbl = new Label("Port");
    final Label pictureLbl = new Label("Picture");
    final Label chargeableLbl = new Label("Chargeable");
    final Label encryptedLbl = new Label("Encrypted");

    final TextField nameTxt = new TextField("Ronny");
    final TextField adressTxt = new TextField("127.0.0.1");
    final TextField portTxt = new TextField("5900");
    final TextField pictureTxt = new TextField("/images/sup.jpg");

    final CheckBox chargeableCBox = new CheckBox();
    final CheckBox encryptedCBox = new CheckBox();


    File file = new File("src/Box13.jpg");
    Image image = new Image(file.toURI().toString());
    ImageView imageView = new ImageView(image);

    gridPane.add(nameLbl,0,0);
    gridPane.add(nameTxt,1,0);
    gridPane.add(adressLbl,0,1);
    gridPane.add(adressTxt,1,1);
    gridPane.add(portLbl,0,2);
    gridPane.add(portTxt,1,2);
    gridPane.add(pictureLbl,0,3);
    gridPane.add(pictureTxt,1,3);
    gridPane.add(chargeableLbl,0,4);
    gridPane.add(chargeableCBox,1,4);
    gridPane.add(encryptedLbl,0,5);
    gridPane.add(encryptedCBox,1,5);

    dialogPane.getButtonTypes().add(ButtonType.APPLY);

    gridPane.setHgap(20);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(25, 25, 25, 25));

    dialogPane.setContent(gridPane);
    dialog.setDialogPane(dialogPane);


    dialog.setResizable(true);
    dialog.setHeight(500);
    dialog.setWidth(500);

    dialog.show();



}
}

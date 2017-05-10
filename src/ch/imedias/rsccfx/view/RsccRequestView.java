package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Defines all elements shown in the request section.
 */
public class RsccRequestView extends BorderPane {
  private static final Logger LOGGER =
      Logger.getLogger(RsccRequestView.class.getName());

  private static final double BUTTON_SIZE = 50d;
  private static final double GENERATEDKEYFLD_HEIGHT = 60d;

  final HeaderView headerView;
  private final Rscc model;
  private final Strings strings = new Strings();

  final Label titleLbl = new Label();
  final Label descriptionLbl = new Label();
  final Label supporterDescriptionLbl = new Label();
  final Label statusLbl = new Label();

  final VBox descriptionBox = new VBox();
  final GridPane collectBox = new GridPane();

  final HBox centerBox = new HBox();
  final HBox keyGeneratingBox = new HBox();
  final HBox predefinedAdressessBox = new HBox();
  final HBox statusBox = new HBox();

  final TitledPane keyGeneratorPane = new TitledPane();
  final TitledPane predefinedAddressesPane = new TitledPane();
  final ScrollPane scrollPane = new ScrollPane();

  final TextField generatedKeyFld = new TextField();

  final Button reloadKeyBtn = new Button();
  final Button readyBtn = new Button();

  Image reloadImg;

  ImageView reloadImgView;

  GridPane supporterGrid = new GridPane();

  /**
   * Initializes all the GUI components needed to generate the key the supporter needs.
   *
   * @param model the model to handle the data.
   */
  public RsccRequestView(Rscc model) {
    this.model = model;
    headerView = new HeaderView(model);
    SvgImageLoaderFactory.install();
    initFieldData();
    layoutForm();
    layoutSupporterPane();
    layoutCollectBox();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data

    titleLbl.textProperty().set("Generate key");

    descriptionLbl.textProperty().set("Send this code to your supporter and click ready. "
        + "Once your supporter enters this code, the remote support will start.");

    InputStream reloadImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/reload.svg");
    reloadImg = new Image(reloadImagePath);
    reloadImgView = new ImageView(reloadImg);
    reloadKeyBtn.setGraphic(reloadImgView);

    readyBtn.textProperty().set("Ready");

    keyGeneratorPane.setText("Key generator");
    keyGeneratorPane.setExpanded(true);


    predefinedAddressesPane.setText("Predefined Addresses");
    predefinedAddressesPane.setExpanded(false);
    predefinedAddressesPane.setId("predefinedAddressesPane");

    supporterDescriptionLbl.setText("Description on the right");

    // TODO: Tech Group - switch waiting and ready Label
    //statusLbl.textProperty().set(strings.requestStatusLblReady);
    statusLbl.textProperty().set(strings.requestStatusLblWaiting);
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    titleLbl.getStyleClass().add("titleLbl");

    statusBox.getStyleClass().add("statusBox");
    statusLbl.getStyleClass().add("statusLbl");

    //generatedKeyFld.setPrefHeight(GENERATEDKEYFLD_HEIGHT); // FIXME: Has this to be in the CSS?
    generatedKeyFld.setEditable(false); // FIXME: Has this to be in the CSS?
    generatedKeyFld.setId("generatedKeyFld");

    reloadImgView.fitWidthProperty().set(BUTTON_SIZE); // FIXME: Has this to be in the CSS?
    reloadImgView.fitHeightProperty().set(BUTTON_SIZE); // FIXME: Has this to be in the CSS?
    reloadImgView.setPreserveRatio(true);

    reloadKeyBtn.setPrefWidth(BUTTON_SIZE); // FIXME: Has this to be in the CSS?
    reloadKeyBtn.setPrefHeight(BUTTON_SIZE); // FIXME: Has this to be in the CSS?

    centerBox.setId("centerBox");

    keyGeneratingBox.getChildren().addAll(generatedKeyFld, reloadKeyBtn);
    keyGeneratingBox.setId("keyGeneratingBox");

    readyBtn.setId("readyBtn");
    reloadKeyBtn.setId("reloadKeyBtn");

    descriptionBox.getChildren().addAll(titleLbl, descriptionLbl, readyBtn);

    descriptionLbl.getStyleClass().add("descriptionLbl"); // TODO: Styling
    descriptionBox.getStyleClass().add("descriptionBox");

    centerBox.getChildren().addAll(keyGeneratingBox, descriptionBox);

    statusBox.getChildren().addAll(statusLbl);
    keyGeneratorPane.setContent(collectBox);

    setTop(headerView);
    setCenter(keyGeneratorPane);
  }

  private void layoutSupporterPane() {
    predefinedAdressessBox.getChildren().addAll(scrollPane, supporterDescriptionLbl);
    predefinedAddressesPane.setContent(predefinedAdressessBox);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setContent(supporterGrid);

    // add column constraints
    ColumnConstraints col1 = new ColumnConstraints();
    ColumnConstraints col2 = new ColumnConstraints();
    ColumnConstraints col3 = new ColumnConstraints();
    supporterGrid.getColumnConstraints().addAll(col1, col2, col3);
    int amountOfColumns = supporterGrid.getColumnConstraints().size();
    int columnPercentWidth = 100 / amountOfColumns;
    col1.setPercentWidth(columnPercentWidth);
    col2.setPercentWidth(columnPercentWidth);
    col3.setPercentWidth(columnPercentWidth);

    setTop(headerView);
    setCenter(keyGeneratorPane);
    setBottom(predefinedAddressesPane);
  }

  private void layoutCollectBox() {
    GridPane.setConstraints(generatedKeyFld, 0, 0);
    GridPane.setConstraints(reloadKeyBtn, 1, 0);
    GridPane.setConstraints(titleLbl, 2, 0);
    GridPane.setConstraints(descriptionLbl, 2, 1);
    GridPane.setConstraints(statusBox, 0, 2);

    GridPane.setRowSpan(generatedKeyFld, 2);
    GridPane.setRowSpan(reloadKeyBtn, 2);
    GridPane.setColumnSpan(statusBox, 3);

    //collectBox.set

    collectBox.getChildren().addAll(generatedKeyFld, reloadKeyBtn, titleLbl, descriptionLbl, statusBox);
    collectBox.setAlignment(Pos.CENTER);
    collectBox.getChildren().stream()
        .forEach(node -> {
          GridPane.setVgrow(node, Priority.ALWAYS);
          GridPane.setHgrow(node, Priority.ALWAYS);
          GridPane.setValignment(node, VPos.CENTER);
          GridPane.setHalignment(node, HPos.CENTER);
          //GridPane.setMargin(node, new Insets(25,25,25,25));
        });
  }

  private void bindFieldsToModel() {
    // make bindings to the model
    generatedKeyFld.textProperty().bind(model.keyProperty());
  }
}

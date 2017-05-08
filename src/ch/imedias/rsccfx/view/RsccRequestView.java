package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;
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
  final Label predefinedAddressesLbl = new Label();
  final Label descriptionLbl = new Label();
  final Label supporterDescriptionLbl = new Label();

  final VBox descriptionBox = new VBox();

  final HBox centerBox = new HBox();
  final HBox keyGeneratingBox = new HBox();
  final HBox predefinedAdressessBox = new HBox();

  final TitledPane keyGeneratorPane = new TitledPane();
  final TitledPane predefinedAddressesPane = new TitledPane();
  final ScrollPane scrollPane = new ScrollPane();

  final TextField generatedKeyFld = new TextField();

  final Button reloadKeyBtn = new Button();
  final Button readyBtn = new Button();
  final Button supporterOneBtn = new Button();
  final Button btn2 = new Button("Btn");
  final Button btn3 = new Button("Btn");
  final Button btn4 = new Button("Btn");
  final Button btn5 = new Button("Btn");
  final Button btn6 = new Button("Btn");
  final Button btn7 = new Button("Btn");

  Image reloadImg;

  ImageView reloadImgView;

  GridPane supporterGrid = new GridPane();

  /**
   * Initializes all the GUI components needed generate the key the supporter needs.
   *
   * @param model the model to handle the data.
   */
  public RsccRequestView(Rscc model) {
    this.model = model;
    headerView = new HeaderView(model);
    SvgImageLoaderFactory.install();
    initFieldData();
    layoutForm();
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

    predefinedAddressesLbl.textProperty().set("Predefined Addresses");
    predefinedAddressesPane.setText("Predefined Addresses");
    predefinedAddressesPane.setExpanded(false);
    predefinedAddressesPane.setId("predefinedAddressesPane");

    supporterDescriptionLbl.setText("Description on the right");

    // TODO: Finish all the buttons here according to mockup.
    // Admin Buttons
    // label, six Buttons, six images
    /*supporterOneBtn.setGraphic();*/
    supporterOneBtn.textProperty().setValue("Supporter 1");
    supporterOneBtn.getStyleClass().add("supporterBtn");
    btn2.getStyleClass().add("supporterBtn");
    btn3.getStyleClass().add("supporterBtn");
    btn4.getStyleClass().add("supporterBtn");
    btn5.getStyleClass().add("supporterBtn");
    btn6.getStyleClass().add("supporterBtn");
    btn7.getStyleClass().add("supporterBtn");
    /*supporterTwoBtn.setGraphic();*/
    // two HBox'es
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    titleLbl.getStyleClass().add("titleLbl");

    generatedKeyFld.setPrefHeight(GENERATEDKEYFLD_HEIGHT); // FIXME: Has this to be in the CSS?
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

    keyGeneratorPane.setContent(centerBox);

    // *** Supporter Pane ***
    predefinedAdressessBox.getChildren().addAll(scrollPane, supporterDescriptionLbl);
    predefinedAddressesPane.setContent(predefinedAdressessBox);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setContent(supporterGrid);

    // add column constraints
    ColumnConstraints col1 = new ColumnConstraints();
    ColumnConstraints col2 = new ColumnConstraints();
    ColumnConstraints col3 = new ColumnConstraints();
    col1.setPercentWidth(33.33333);
    col2.setPercentWidth(33.33333);
    col3.setPercentWidth(33.33333);
    supporterGrid.getColumnConstraints().addAll(col1, col2, col3);

    supporterGrid.add(supporterOneBtn, 0, 0);
    supporterGrid.add(btn2, 1, 0);
    supporterGrid.add(btn3, 2, 0);
    supporterGrid.add(btn4, 0, 1);
    supporterGrid.add(btn5, 1, 1);
    supporterGrid.add(btn6, 2, 1);
    supporterGrid.add(btn7, 0, 2);

    // FIXME: Define height of scrollPane

    // ***************

    setTop(headerView);
    setCenter(keyGeneratorPane);
    setBottom(predefinedAddressesPane);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
    generatedKeyFld.textProperty().bind(model.keyProperty());
  }
}


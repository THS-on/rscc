package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.view.util.KeyTextField;
import java.util.logging.Logger;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Defines all elements shown in the support section.
 */
public class RsccSupportView extends BorderPane {
  private static final Logger LOGGER =
      Logger.getLogger(RsccSupportView.class.getName());

  private final Rscc model;
  private final Strings strings = new Strings();
  private static final double KEYFLD_HEIGHT = 60d;

  final HeaderView headerView;

  final Label titleLbl = new Label();
  final Label descriptionLbl = new Label();
  final Label statusLbl = new Label();

  final HBox statusBox = new HBox();

  final KeyTextField keyFld = new KeyTextField();

  final VBox contentBox = new VBox();

  final GridPane keyInputInnerPane = new GridPane();
  final GridPane addressbookInnerPane = new GridPane();

  final TitledPane keyInputTitledPane = new TitledPane();
  final TitledPane addressbookTitledPane = new TitledPane();

  ImageView validationImgView = new ImageView();

  final Button connectBtn = new Button();

  /**
   * Initializes all the GUI components needed to enter the key the supporter received.
   *
   * @param model the model to handle the data.
   */
  public RsccSupportView(Rscc model) {
    this.model = model;
    headerView = new HeaderView(model);
    initFieldData();
    layoutForm();
    bindFieldsToModel();
    layoutKeyInputPane();
  }

  private void initFieldData() {
    // populate fields which require initial data
    titleLbl.textProperty().set(strings.supportTitleLbl);
    descriptionLbl.textProperty().set(strings.supportDescriptionLbl);
    connectBtn.textProperty().set(strings.supportConnectBtn);

    // TODO: Tech Group - switch waiting and ready Label
    //statusLbl.textProperty().set(strings.supportStatusLblReady);
    statusLbl.textProperty().set(strings.supportStatusLblWaiting);

    validationImgView = new ImageView(getClass()
        .getClassLoader()
        .getResource("dialog-error.png")
        .toExternalForm());                     // TODO: Check what to do here.

    keyInputTitledPane.setText(strings.supportKeyInputPane);

    addressbookTitledPane.setText(strings.supportAdressBookPane);
  }

  private void layoutForm() {
    keyInputTitledPane.setExpanded(true);
    keyInputTitledPane.setId("keyInputTitledPane");

    addressbookTitledPane.setExpanded(false);
    addressbookTitledPane.setId("addressbookTitledPane");

    titleLbl.getStyleClass().add("titleLbl");

    descriptionLbl.getStyleClass().add("descriptionLbl");

    statusLbl.getStyleClass().add("statusLbl");
    statusBox.getChildren().add(statusLbl);
    statusBox.getStyleClass().add("statusBox");

    //keyFld.setPrefHeight(KEYFLD_HEIGHT);
    keyFld.setId("keyFld");

    validationImgView.setSmooth(true);

    titleLbl.getStyleClass().add("titleLbl");

    connectBtn.setId("connectBtn");

    connectBtn.setDisable(true);

    contentBox.getChildren().addAll(keyInputTitledPane,keyInputInnerPane,addressbookTitledPane);
    VBox.setVgrow(keyInputInnerPane, Priority.ALWAYS);
    VBox.setVgrow(addressbookInnerPane, Priority.ALWAYS);

    setTop(headerView);
    setCenter(contentBox);
  }

  private void layoutKeyInputPane() {
    GridPane.setConstraints(keyFld, 0, 0);
    GridPane.setConstraints(validationImgView,1,0);
    GridPane.setConstraints(connectBtn,0,1);
    GridPane.setConstraints(titleLbl, 2, 0);
    GridPane.setConstraints(descriptionLbl, 2, 1);
    GridPane.setConstraints(statusBox, 0, 3);

    GridPane.setColumnSpan(statusBox, 3);

    keyInputInnerPane.getChildren().addAll(keyFld, validationImgView,connectBtn, titleLbl, descriptionLbl,statusBox);
    keyInputInnerPane.setAlignment(Pos.CENTER);
    keyInputInnerPane.getChildren().stream()
        .forEach(node -> {
          GridPane.setVgrow(node, Priority.ALWAYS);
          GridPane.setHgrow(node, Priority.ALWAYS);
          GridPane.setValignment(node, VPos.CENTER);
          GridPane.setHalignment(node, HPos.CENTER);
        });
  }

  private void layoutUnderPane() {
    GridPane.setConstraints(keyFld, 0, 0);
    GridPane.setConstraints(validationImgView,1,0);
    GridPane.setConstraints(connectBtn,0,1);
    GridPane.setConstraints(titleLbl, 2, 0);
    GridPane.setConstraints(descriptionLbl, 2, 1);
    GridPane.setConstraints(statusBox, 0, 3);

    GridPane.setColumnSpan(keyFieldAndImageBox, 3);
    GridPane.setColumnSpan(statusBox, 3);

    keyInputInnerPane.getChildren().addAll(keyFld, validationImgView,connectBtn, titleLbl, descriptionLbl,statusBox);
    keyInputInnerPane.setAlignment(Pos.CENTER);
    keyInputInnerPane.getChildren().stream()
        .forEach(node -> {
          GridPane.setVgrow(node, Priority.ALWAYS);
          GridPane.setHgrow(node, Priority.ALWAYS);
          GridPane.setValignment(node, VPos.CENTER);
          GridPane.setHalignment(node, HPos.CENTER);
        });
  }

  private void bindFieldsToModel() {
    // make bindings to the model

  }

}

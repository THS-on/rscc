package ch.imedias.rsccfx.view;

//import ch.imedias.rsccfx.localization.Strings;

import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

/**
 * Defines all elements shown in the header.
 */
public class HeaderView extends HBox {
  private static final double HEADER_HEIGHT = 250d;
  private static final double BUTTON_SIZE = 50d;
  private static final Insets BACK_BUTTON_INSETS = new Insets(0);
  private static final Insets SETTINGS_BUTTON_INSETS = new Insets(0, 5, 0, 20);
  private static final Insets HELP_BUTTON_INSETS = new Insets(0, 10, 0, 20);

  final Pane spacer = new Pane();
  //private final Strings strings = new Strings();
  private final Rscc model;
  Button backBtn = new Button();
  Button helpBtn = new Button();
  Button settingsBtn = new Button();

  Image backImg;
  Image helpImg;
  Image settingImg;

  ImageView backImgView;
  ImageView helpImgView;
  ImageView settingImgView;

  VBox settingsBox = new VBox();
  VBox helpBox = new VBox();

  PopOver settingsPopOver = new PopOver(settingsBox);
  PopOver helpPopOver = new PopOver(helpBox);

  Label compressionLbl = new Label();
  Label qualityLbl = new Label();
  Label bitSettingsLbl = new Label();
  Label bitCurrentSettingsLbl = new Label();

  Slider compressionSldr;
  Slider qualitySldr;

  Label helpLbl = new Label();

  ToggleButton toggleBtn = new ToggleButton();

  /**
   * Initializes all the GUI components needed in the Header.
   */
  public HeaderView(Rscc model) {
    this.model = model;
    SvgImageLoaderFactory.install();
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)

    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox.setMargin(backBtn, BACK_BUTTON_INSETS);
    HBox.setMargin(settingsBtn, SETTINGS_BUTTON_INSETS);
    HBox.setMargin(helpBtn, HELP_BUTTON_INSETS);

    this.getChildren().addAll(backBtn, spacer, helpBtn, settingsBtn);
    this.setId("header");

    InputStream backImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/back.svg");
    backImg = new Image(backImagePath);
    backImgView = new ImageView(backImg);
    backImgView.fitWidthProperty().set(BUTTON_SIZE);
    backImgView.fitHeightProperty().set(BUTTON_SIZE);
    backImgView.setPreserveRatio(true);
    backBtn.setGraphic(backImgView);
    backBtn.setPrefWidth(BUTTON_SIZE);
    backBtn.setPrefHeight(BUTTON_SIZE);
    backBtn.setId("backBtn");

    InputStream helpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/question.svg");
    helpImg = new Image(helpImagePath);
    helpImgView = new ImageView(helpImg);
    helpImgView.fitWidthProperty().set(BUTTON_SIZE);
    helpImgView.fitHeightProperty().set(BUTTON_SIZE);
    helpImgView.setPreserveRatio(true);
    helpBtn.setGraphic(helpImgView);
    helpBtn.setPrefWidth(BUTTON_SIZE);
    helpBtn.setPrefHeight(BUTTON_SIZE);
    helpBtn.setAlignment(Pos.BASELINE_RIGHT);
    helpBtn.setId("helpBtn");

    InputStream settingImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/settings.svg");
    settingImg = new Image(settingImagePath);
    settingImgView = new ImageView(settingImg);
    settingImgView.fitWidthProperty().set(BUTTON_SIZE);
    settingImgView.fitHeightProperty().set(BUTTON_SIZE);
    settingImgView.setPreserveRatio(true);
    settingsBtn.setGraphic(settingImgView);
    settingsBtn.setPrefWidth(BUTTON_SIZE);
    settingsBtn.setPrefHeight(BUTTON_SIZE);
    settingsBtn.setId("settingsBtn");

    this.setHeight(HEADER_HEIGHT);

    // Settings PopOver
    compressionLbl.textProperty().set("Kompression");
    compressionLbl.setId("compressionLbl");

    compressionSldr = new Slider(0, 100, 30);
    compressionSldr.setId("compressionSldr");

    qualityLbl.textProperty().set("Qualität");
    qualitySldr = new Slider(0, 100, 10);

    bitSettingsLbl.textProperty().set("8-Bit-Farben");
    bitSettingsLbl.setId("bitSettingsLbl");

    toggleBtn.textProperty().set("On");
    toggleBtn.setId("toggleBtn");

    bitCurrentSettingsLbl.textProperty().set("Ihre momentane Einstellung ist");
    bitCurrentSettingsLbl.setId("bitCurrentSettingsLbl");

    settingsBox.getChildren().add(new HBox(compressionLbl, compressionSldr));
    settingsBox.getChildren().add(new HBox(qualityLbl, qualitySldr));
    settingsBox.getChildren().add(new HBox(bitSettingsLbl, toggleBtn));
    settingsBox.getChildren().add(bitCurrentSettingsLbl);

    settingsPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);

    // Help popover
    helpLbl.textProperty().set("The remote support tool allows you to get help " +
        "or help someone in need");
    helpLbl.setId("helpLbl");

    // TODO: If we have more labels, we can add it to the box.
    helpBox.getChildren().addAll(helpLbl);

    helpPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);

    // TODO: Use according method in "HeaderPresenter.java"!
    settingsBtn.setOnAction(event -> settingsPopOver.show(settingsBtn));
    helpBtn.setOnAction(event -> helpPopOver.show(helpBtn));
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }


}


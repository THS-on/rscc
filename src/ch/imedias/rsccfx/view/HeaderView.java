package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Defines all elements shown in the header.
 */
public class HeaderView extends HBox {
  private static final double HEADER_HEIGHT = 250d;
  private static final double WIDTH_HEIGHT_PROPERTY_VALUE = 50d;
  private static final Insets BACK_BUTTON_INSETS = new Insets(0);
  private static final Insets SETTINGS_BUTTON_INSETS = new Insets(0, 5, 0, 20);
  private static final Insets HELP_BUTTON_INSETS = new Insets(0, 10, 0, 20);

  final Pane spacer = new Pane();
  private final Strings strings = new Strings();
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
    backImgView.fitWidthProperty().set(WIDTH_HEIGHT_PROPERTY_VALUE);
    backImgView.fitHeightProperty().set(WIDTH_HEIGHT_PROPERTY_VALUE);
    backImgView.setPreserveRatio(true);
    backBtn.setGraphic(backImgView);
    backBtn.setPrefWidth(WIDTH_HEIGHT_PROPERTY_VALUE);
    backBtn.setPrefHeight(WIDTH_HEIGHT_PROPERTY_VALUE);
    backBtn.setId("backBtn");

    InputStream helpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/question.svg");
    helpImg = new Image(helpImagePath);
    helpImgView = new ImageView(helpImg);
    helpImgView.fitWidthProperty().set(WIDTH_HEIGHT_PROPERTY_VALUE);
    helpImgView.fitHeightProperty().set(WIDTH_HEIGHT_PROPERTY_VALUE);
    helpImgView.setPreserveRatio(true);
    helpBtn.setGraphic(helpImgView);
    helpBtn.setPrefWidth(WIDTH_HEIGHT_PROPERTY_VALUE);
    helpBtn.setPrefHeight(WIDTH_HEIGHT_PROPERTY_VALUE);
    helpBtn.setAlignment(Pos.BASELINE_RIGHT);
    helpBtn.setId("helpBtn");

    InputStream settingImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/settings.svg");
    settingImg = new Image(settingImagePath);
    settingImgView = new ImageView(settingImg);
    settingImgView.fitWidthProperty().set(WIDTH_HEIGHT_PROPERTY_VALUE);
    settingImgView.fitHeightProperty().set(WIDTH_HEIGHT_PROPERTY_VALUE);
    settingImgView.setPreserveRatio(true);
    settingsBtn.setGraphic(settingImgView);
    settingsBtn.setPrefWidth(WIDTH_HEIGHT_PROPERTY_VALUE);
    settingsBtn.setPrefHeight(WIDTH_HEIGHT_PROPERTY_VALUE);
    settingsBtn.setId("settingsBtn");

    this.setHeight(HEADER_HEIGHT);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }

}



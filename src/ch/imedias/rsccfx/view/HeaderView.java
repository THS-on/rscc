package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;
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
  private static final Logger LOGGER =
      Logger.getLogger(Rscc.class.getName());

  private static final double HEADER_HEIGHT = 250d;
  private static final double BUTTON_SIZE = 35d;
  private static final int INSETS_SIZE = 15;
  private static final int BUTTON_PADDING = 15;

  private static final Insets BACK_BUTTON_INSETS = new Insets(INSETS_SIZE);
  private static final Insets SETTINGS_BUTTON_INSETS = new Insets(INSETS_SIZE,0,INSETS_SIZE,INSETS_SIZE);
  private static final Insets HELP_BUTTON_INSETS = new Insets(INSETS_SIZE);

  final Pane spacer = new Pane();
  final Button backBtn = new Button();
  final Button helpBtn = new Button();
  final Button settingsBtn = new Button();
  private final Strings strings = new Strings();
  private final Rscc model;
  Image backImg;
  Image helpImg;
  Image settingImg;

  ImageView backImgView;
  ImageView helpImgView;
  ImageView settingImgView;

  /**
   * Initializes all the GUI components needed in the Header.
   *
   * @param model the model to handle the data.
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

    backBtn.setPadding(new Insets(BUTTON_PADDING));
    settingsBtn.setPadding(new Insets(BUTTON_PADDING));
    helpBtn.setPadding(new Insets(BUTTON_PADDING));

    this.getChildren().addAll(backBtn, spacer, settingsBtn, helpBtn);
    this.setId("header");

    InputStream backImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/back.svg");
    backImg = new Image(backImagePath);
    backImgView = new ImageView(backImg);
    backImgView.setFitWidth(BUTTON_SIZE);
    backImgView.setFitHeight(BUTTON_SIZE);
    backImgView.setPreserveRatio(true);
    backBtn.setGraphic(backImgView);
    backBtn.setPrefWidth(BUTTON_SIZE);
    backBtn.setPrefHeight(BUTTON_SIZE);
    backBtn.setId("backBtn");

    InputStream helpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/question.svg");
    helpImg = new Image(helpImagePath);
    helpImgView = new ImageView(helpImg);
    helpImgView.setFitWidth(BUTTON_SIZE);
    helpImgView.setFitHeight(BUTTON_SIZE);
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
    settingImgView.setFitWidth(BUTTON_SIZE);
    settingImgView.setFitHeight(BUTTON_SIZE);
    settingImgView.setPreserveRatio(true);
    settingsBtn.setGraphic(settingImgView);
    settingsBtn.setPrefWidth(BUTTON_SIZE);
    settingsBtn.setPrefHeight(BUTTON_SIZE);
    settingsBtn.setId("settingsBtn");
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }


}


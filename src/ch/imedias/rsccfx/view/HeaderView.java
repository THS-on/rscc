package ch.imedias.rsccfx.view;

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

  private final Rscc model;

  final Pane spacer = new Pane();

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
    HBox.setMargin(backBtn,new Insets(0,0,0,0));
    HBox.setMargin(settingsBtn,new Insets(0,5,0,20));
    HBox.setMargin(helpBtn,new Insets(0,10,0,20));

    this.getChildren().addAll(backBtn, spacer, helpBtn, settingsBtn);
    this.setId("header");


    InputStream backImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/back.svg");
    backImg = new Image(backImagePath);
    backImgView = new ImageView(backImg);
    backImgView.fitWidthProperty().set(50);
    backImgView.fitHeightProperty().set(50);
    backImgView.setPreserveRatio(true);
    backBtn.setGraphic(backImgView);
    backBtn.setPrefWidth(50);
    backBtn.setPrefHeight(50);
    backBtn.setId("backBtn");
    // What needs to be added to the CSS?

    InputStream helpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/question.svg");
    helpImg = new Image(helpImagePath);
    helpImgView = new ImageView(helpImg);
    helpImgView.fitWidthProperty().set(50);
    helpImgView.fitHeightProperty().set(50);
    helpImgView.setPreserveRatio(true);
    helpBtn.setGraphic(helpImgView);
    helpBtn.setPrefWidth(50);
    helpBtn.setPrefHeight(50);
    helpBtn.setAlignment(Pos.BASELINE_RIGHT);
    helpBtn.setId("helpBtn");
    // What needs to be added to the CSS?

    InputStream settingImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/settings.svg");
    settingImg = new Image(settingImagePath);
    settingImgView = new ImageView(settingImg);
    settingImgView.fitWidthProperty().set(50);
    settingImgView.fitHeightProperty().set(50);
    settingImgView.setPreserveRatio(true);
    settingsBtn.setGraphic(settingImgView);
    settingsBtn.setPrefWidth(50);
    settingsBtn.setPrefHeight(50);
    settingsBtn.setId("settingsBtn");
    // TODO: What needs to be added to the CSS?

    this.setHeight(HEADER_HEIGHT);
  }


  private void bindFieldsToModel() {
    // make bindings to the model
  }

}



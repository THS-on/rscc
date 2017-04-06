package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Defines all elements shown in the header.
 */
public class HeaderView extends HBox {
  private final Rscc model;

  HBox headerBox = new HBox();

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

    headerBox.getChildren().add(backBtn);
    headerBox.getChildren().add(helpBtn);
    headerBox.getChildren().add(settingsBtn);
    headerBox.setSpacing(10);
    headerBox.setId("header");

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
    // What needs to be added to the CSS?

    this.getChildren().add(headerBox);
  }


  private void bindFieldsToModel() {
    // make bindings to the model
  }

}



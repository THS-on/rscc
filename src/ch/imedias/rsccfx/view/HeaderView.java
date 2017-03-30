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

  Label headLbl = new Label();

  Button backBtn = new Button();
  Button helpBtn = new Button();
  Button settBtn = new Button();

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
    headLbl.textProperty().set("I need Help");
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)

    headerBox.getChildren().add(backBtn);
    headerBox.getChildren().add(headLbl);
    headerBox.getChildren().add(helpBtn);
    headerBox.getChildren().add(settBtn);
    headerBox.setId("header");

    InputStream backImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/back1.svg");
    backImg = new Image(backImagePath);
    backImgView = new ImageView(backImg);
    backImgView.setPreserveRatio(true);
    backBtn.setGraphic(backImgView);
    // What needs to be added to the CSS?

    InputStream helpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/help1.svg");
    helpImg = new Image(helpImagePath);
    helpImgView = new ImageView(helpImg);
    helpImgView.setPreserveRatio(true);
    helpBtn.setGraphic(backImgView);
    // What needs to be added to the CSS?

    InputStream settingImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/setting1.svg");
    settingImg = new Image(settingImagePath);
    settingImgView = new ImageView(settingImg);
    settingImgView.setPreserveRatio(true);
    settBtn.setGraphic(backImgView);
    // What needs to be added to the CSS?

    headLbl.setAlignment(Pos.CENTER);
    headLbl.setId("headerText");

    this.getChildren().add(headerBox);
  }


  private void bindFieldsToModel() {
    // make bindings to the model

  }

}



package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
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
  Button settBtn = new Button();
  Button helpBtn = new Button();

  /**
   * Initializes all the GUI components needed in the Header.
   */
  public HeaderView(Rscc model) {
    this.model = model;
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

    backBtn.setGraphic(new ImageView(new Image(getClass()
        .getClassLoader().getResource("images/back1.png").toExternalForm())));

    headLbl.setAlignment(Pos.CENTER);
    headLbl.setId("headerText");

    helpBtn.setGraphic(new ImageView(new Image(getClass()
        .getClassLoader().getResource("images/help1.png").toExternalForm())));

    settBtn.setGraphic(new ImageView(new Image(getClass()
        .getClassLoader().getResource("images/setting1.png").toExternalForm())));

    this.getChildren().add(headerBox);
  }


  private void bindFieldsToModel() {
    // make bindings to the model

  }

}




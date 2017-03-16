package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * Defines all elements shown in the Header.
 */
public class HeaderView extends HBox {

  private final Rscc model;
  private final HeaderPresenter presenter;

  HBox headerBox;
  Label headLbl;
  Button backBtn;
  Button settBtn;
  Button helpBtn;

  /**
   * Initializes all the GUI components needed in the Header.
   *
   * @param model defines what is displayed.
   */
  public HeaderView(Rscc model) {
    this.model = model;
    this.presenter = new HeaderPresenter(model, this);
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)

    headerBox = new HBox();

    headerBox.setPadding(new Insets(10, 10, 10, 10));

    backBtn = new Button();
    backBtn.setAlignment(Pos.TOP_LEFT);
    backBtn.setGraphic(new ImageView(new Image(getClass()
        .getClassLoader().getResource("images/back1.png").toExternalForm())));
    helpBtn = new Button();
    helpBtn.setGraphic(new ImageView(new Image(getClass()
        .getClassLoader().getResource("images/help1.png").toExternalForm())));
    settBtn = new Button();
    settBtn.setGraphic(new ImageView(new Image(getClass()
        .getClassLoader().getResource("images/setting1.png").toExternalForm())));
    settBtn.setAlignment(Pos.TOP_RIGHT);
    headLbl = new Label("I need Help");

    headLbl.setAlignment(Pos.CENTER);

    headerBox.getChildren().add(backBtn);
    headerBox.getChildren().add(headLbl);
    headerBox.getChildren().add(helpBtn);
    headerBox.getChildren().add(settBtn);

    this.getChildren().add(headerBox);

    // apply styling
    headerBox.setId("header");
    headLbl.setId("headerText");
  }

  private void initFieldData() {
    //populate fields which require initial data
    // TODO: Implement that back Button always goes "one back"?
    // TODO: SA, please review idea.

  }

  private void bindFieldsToModel() {
    //make the bindings to the model

  }

}




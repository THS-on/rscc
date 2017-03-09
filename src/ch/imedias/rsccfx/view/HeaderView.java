package ch.imedias.rsccfx.view;

/**
 * import statements.
 */

import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HeaderView extends VBox {

  private final Rscc model;
  private final HeaderPresenter presenter;

  // TODO: refactor buttonBox and separatorBox
  VBox headerBox;
  HBox buttonBox;
  HBox separatorBox;
  Label headLbl;
  Button backBtn;
  Button settBtn;
  Button helpBtn;
  Separator sep;

  /**
   *
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

    headerBox = new VBox();
    buttonBox = new HBox();
    separatorBox = new HBox();

    buttonBox.setPadding(new Insets(10, 10, 10, 10));
    separatorBox.setPadding(new Insets(10, 10, 10, 10));


    sep = new Separator();
    //HBox.setHgrow(sep, Priority.ALWAYS);
    separatorBox.getChildren().add(sep);

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

    headLbl.setFont(new Font("Cantarell", 20));   // TODO: move to CSS
    headLbl.setAlignment(Pos.CENTER);

    buttonBox.getChildren().add(backBtn);
    buttonBox.getChildren().add(headLbl);
    buttonBox.getChildren().add(helpBtn);
    buttonBox.getChildren().add(settBtn);

    headerBox.getChildren().add(buttonBox);
    headerBox.getChildren().add(separatorBox);

    this.getChildren().add(headerBox);

  }

  private void initFieldData() {
    //populate fields which require initial data

    // TODO: layout according to the mock


  }



  private void bindFieldsToModel() {
    //make the bindings to the model

  }

}




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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Created by user on 02.12.16.
 */
public class HeaderView extends VBox {
  private final Rscc model;

  VBox headerbox; // FIXME: Change variable names according to the SAD
  HBox boxLine1;
  HBox boxLine2;
  Label headLbl;
  Button backBtn;
  Button settBtn;
  Button helpBtn;
  Separator sep;

  /**
   * class HeaderView.
   */ // FIXME: add proper comment according to the google style guidelines
  public HeaderView(Rscc model) {
    this.model = model;
    //this.headerbox = headerbox;
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)

    headerbox = new VBox();
    boxLine1 = new HBox();
    boxLine2 = new HBox();

    boxLine1.setPadding(new Insets(10, 10, 10, 10));
    boxLine2.setPadding(new Insets(10, 10, 10, 10));


    sep = new Separator();
    boxLine2.getChildren().add(sep);


  }

  private void initFieldData() {
    //populate fields which require initial data
    // FIXME: move those to "layoutForm" and only keep the ones that actually populate fields with initial data
    // FIXME: improve code structure by putting together inidivudal elements, and separating each element with a line break
    // TODO: layout according to the mock
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
    headLbl.setFont(new Font("Cantarell", 20));
    headLbl.setAlignment(Pos.CENTER);
    //TODO: Make names better...
    boxLine1.getChildren().add(backBtn);
    boxLine1.getChildren().add(headLbl);
    boxLine1.getChildren().add(helpBtn);
    boxLine1.getChildren().add(settBtn);
  }

  /**
   * initSize method.
   */ // FIXME: add proper comment according to the google style guidelines
  public void initSize(Scene scene) {
    // FIXME: make a presenter class for the headerview and put the bindings in there
    // FIXME: this method is called in ShowTokenView??? should not be that way...
    sep.prefWidthProperty().bind(scene.widthProperty());
    headLbl.prefWidthProperty().bind(scene.widthProperty());
    boxLine1.prefWidthProperty().bind(scene.widthProperty());
    boxLine2.prefWidthProperty().bind(scene.widthProperty());
    headerbox.prefWidthProperty().bind(scene.widthProperty());


  }

  private void bindFieldsToModel() {
    //make the bindings to the model
    // FIXME: those are not bindings to the model, so put them in layoutForm
    headerbox.getChildren().add(boxLine1);
    headerbox.getChildren().add(boxLine2);

    this.getChildren().add(headerbox);
  }

}




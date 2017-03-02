package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class RsccEnterTokenView extends VBox {

  private final Rscc model;

  Label enterTokenlbl;  // FIXME: rename all fields to the proper camel case, i. e. enterTokenLbl
  Label loremIpsumlbl; // FIXME: give the fields more meaningful names than "loremIpsumLbl" or "exampleLbl"
  Label examplelbl;
  Label instructionlbl;

  VBox groupingbox;
  HBox tokenValidationbox;
  TextField tokentxt;
  ImageView isValidimg;

  Button connectbtn;
  Button expandOptionbtn;

  /**
   * This is the view for the supporter to enter the token. // FIXME: Update to conform with the google java style guidelines
   */
  public RsccEnterTokenView(Rscc model) {
    this.model = model;

    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void layoutForm() {
    this.setPadding(new Insets(5, 25, 5, 25));
    this.setSpacing(10);

    enterTokenlbl.setFont(new Font(25));

    loremIpsumlbl.setWrapText(true);

    tokenValidationbox = new HBox();
    tokentxt = new TextField();
    tokentxt.setFont(new Font(30));

    isValidimg.setSmooth(true);
    tokenValidationbox.getChildren().addAll(tokentxt, isValidimg);
    tokenValidationbox.setSpacing(5);
    tokenValidationbox.setHgrow(tokentxt, Priority.ALWAYS);
    tokenValidationbox.setAlignment(Pos.CENTER_LEFT);

    groupingbox = new VBox();
    groupingbox.getChildren().addAll(tokenValidationbox, instructionlbl);

    this.getChildren().addAll(enterTokenlbl,
        loremIpsumlbl,
        examplelbl,
        groupingbox,
        connectbtn,
        expandOptionbtn);

    connectbtn.setFont(new Font(30));
    // FIXME: restructure lines to be grouped by the object that is being used
  }

  private void initFieldData() {
    // FIXME: update initial values with ones that make sense
    isValidimg = new ImageView(getClass()
        .getClassLoader()
        .getResource("dialog-error.png")
        .toExternalForm());
    enterTokenlbl = new Label("EnterToken");
    loremIpsumlbl = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
    examplelbl = new Label("Number of characters: 8\nexample: 666xx666");
    instructionlbl = new Label("Instructions");
    connectbtn = new Button("Connect");
    expandOptionbtn = new Button("More");
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
  }

}

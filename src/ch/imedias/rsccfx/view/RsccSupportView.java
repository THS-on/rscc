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

public class RsccSupportView extends VBox {
  private final Rscc model;

  Label enterTokenLbl;
  Label descriptionLbl;
  Label exampleLbl;
  Label instructionLbl;

  VBox groupingBox;
  HBox tokenValidationBox;
  TextField tokenTxt;
  ImageView isValidImg;

  Button connectBtn;
  Button expandOptionBtn;

  /**
   * This is the view for the supporter to enter the token. // FIXME: Update to conform with the google java style guidelines
   */
  public RsccSupportView(Rscc model) {
    this.model = model;

    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void layoutForm() {
    this.setPadding(new Insets(5, 25, 5, 25));
    this.setSpacing(10);

    enterTokenLbl.setFont(new Font(25));

    descriptionLbl.setWrapText(true);

    tokenValidationBox = new HBox();
    tokenTxt = new TextField();
    tokenTxt.setFont(new Font(30));

    isValidImg.setSmooth(true);
    tokenValidationBox.getChildren().addAll(tokentxt, isValidimg);
    tokenValidationBox.setSpacing(5);
    tokenValidationBox.setHgrow(tokentxt, Priority.ALWAYS);
    tokenValidationBox.setAlignment(Pos.CENTER_LEFT);

    groupingBox = new VBox();
    groupingBox.getChildren().addAll(tokenValidationBox, instructionLbl);

    this.getChildren().addAll(enterTokenLbl,
        descriptionLbl,
        exampleLbl,
        groupingBox,
        connectBtn,
        expandOptionBtn);

    connectBtn.setFont(new Font(30));
    // FIXME: restructure lines to be grouped by the object that is being used
  }

  private void initFieldData() {
    // FIXME: update initial values with ones that make sense
    isValidImg = new ImageView(getClass()
        .getClassLoader()
        .getResource("dialog-error.png")
        .toExternalForm());
    enterTokenLbl = new Label("EnterToken");
    descriptionLbl = new Label("A description will soon be displayed here, for now: Lorem ipsum dolor sit amet");
    exampleLbl = new Label("Number of characters: 8\nexample: 666xx666");
    instructionLbl = new Label("Here will soon be displayed the Instructions");
    connectBtn = new Button("Connect");
    expandOptionBtn = new Button("More options");
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
  }

}

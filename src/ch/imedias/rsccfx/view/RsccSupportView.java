package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Defines all elements shown in the support section.
 */
public class RsccSupportView extends BorderPane {
  private final Rscc model;

  HeaderView headerView;
  HeaderPresenter headerPresenter;
  //FIXME: Those fields are never used. Consider using it in the future. Thx.
  TitledPane adminSupporterPane = new TitledPane();
  TitledPane mainPane = new TitledPane();

  Label enterTokenLbl = new Label("EnterToken");
  Label keyDescriptionLbl = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
  Label exampleLbl = new Label("Number of characters: 8\nexample: 666xx666");
  Label instructionLbl = new Label("Instructions");

  VBox topBox = new VBox();
  VBox centerBox = new VBox();
  VBox groupingBox = new VBox();
  HBox tokenValidationBox = new HBox();

  TextField tokenTxt = new TextField();

  ImageView isValidImg = new ImageView();

  Button connectBtn = new Button("Connect");
  Button expandOptionBtn = new Button("More");


  /**
   * Initializes all the GUI components needed to enter the token the supporter received.
   *
   * @param model defines what is displayed.
   */
  public RsccSupportView(Rscc model) {
    this.model = model;
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data
    headerView = new HeaderView(model);
    headerPresenter = new HeaderPresenter(model, headerView);

    // TODO: Move initialization to according class...
    enterTokenLbl.textProperty().set("");
    keyDescriptionLbl.textProperty().set("");
    exampleLbl.textProperty().set("");
    instructionLbl.textProperty().set("");

    isValidImg = new ImageView(getClass()
        .getClassLoader()
        .getResource("dialog-error.png")
        .toExternalForm());                     // TODO: Check what to do here.

    connectBtn.textProperty().set("");
    expandOptionBtn.textProperty().set("");

  }

  private void layoutForm() {
    // TODO: import CSS accordingly. Ask SA where it needs to be defined.
    // this.setPadding(new Insets(5, 25, 5, 25)); // TODO: set paddings for "center"
    // this.setId("SupporterView");

    //enterTokenLbl.setFont(new Font(25));
    enterTokenLbl.setId("EnterTokenLbl");

    keyDescriptionLbl.setWrapText(true);

    tokenTxt.setFont(new Font(30)); // TODO: Move to CSS

    isValidImg.setSmooth(true);

    tokenValidationBox.getChildren().addAll(tokenTxt, isValidImg);
    tokenValidationBox.setSpacing(5);       // TODO: Move to CSS.
    tokenValidationBox.setHgrow(tokenTxt, Priority.ALWAYS);
    tokenValidationBox.setAlignment(Pos.CENTER_LEFT);

    groupingBox.getChildren().addAll(tokenValidationBox, instructionLbl);

    centerBox.getChildren().addAll(enterTokenLbl,
        keyDescriptionLbl,
        exampleLbl,
        groupingBox,
        connectBtn,
        expandOptionBtn);

    connectBtn.setFont(new Font(30));       // TODO: Move to CSS.
    setCenter(centerBox);
    topBox.getChildren().add(headerView);
    setTop(topBox);
  }


  private void bindFieldsToModel() {
    //make the bindings to the model

  }

}


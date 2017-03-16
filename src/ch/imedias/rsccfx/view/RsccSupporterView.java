package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
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

public class RsccSupporterView extends BorderPane implements View{
/**
 * TODO: Javadoc comment here.
 */
public class RsccSupporterView extends VBox {

  private final Rscc model;

  TitledPane adminSupporterPane;
  TitledPane mainPane;
  Label enterTokenlbl;
  Label loremIpsumlbl;
  Label examplelbl;
  Label instructionlbl;

  VBox groupingbox;
  HBox tokenValidationbox;
  TextField tokentxt;
  ImageView isValidimg;

  Button connectbtn;
  Button expandOptionbtn;

  HeaderView headerView;
  HeaderPresenter headerPresenter;

  VBox topBox;
  VBox centerBox;


  /**
   * TODO: Javadoc comment here.
   * This is the view for the supporter to enter the token.
   * @param model
   */
  public RsccSupporterView(Rscc model) {
    this.model = model;

    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void layoutForm() {
    this.setPadding(new Insets(5, 25, 5, 25));


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

    centerBox.getChildren().addAll(enterTokenlbl,
        loremIpsumlbl,
        examplelbl,
        groupingbox,
        connectbtn,
        expandOptionbtn);

    connectbtn.setFont(new Font(30));
  }

  private void initFieldData() {

    adminSupporterPane = new TitledPane();
    mainPane = new TitledPane();

    headerView = new HeaderView(model);
    headerPresenter = new HeaderPresenter(model, headerView);
    topBox = new VBox();
    centerBox = new VBox();

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

    setCenter(centerBox);
    topBox.getChildren().add(headerView);
    setTop(topBox);
  }

  private void bindFieldsToModel() {
    //make the bindings to the model

  }

}

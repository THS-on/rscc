package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import java.util.logging.Logger;
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
  private static final Logger LOGGER =
      Logger.getLogger(RsccSupportView.class.getName());

  private final Rscc model;
  private final Strings strings = new Strings();

  final HeaderView headerView;

  final Label enterKeyLbl = new Label();
  final Label keyDescriptionLbl = new Label();
  final Label exampleLbl = new Label();
  final Label instructionLbl = new Label();

  final VBox centerBox = new VBox();
  final VBox groupingBox = new VBox();
  final HBox keyValidationBox = new HBox();

  final TextField keyFld = new TextField();

  final TitledPane keyInputPane = new TitledPane();
  final TitledPane predefinedAdressesPane = new TitledPane();

  ImageView validationImgView = new ImageView();

  final Button connectBtn = new Button();
  final Button expandOptionBtn = new Button();

  /**
   * Initializes all the GUI components needed to enter the key the supporter received.
   *
   * @param model the model to handle the data.
   */
  public RsccSupportView(Rscc model) {
    this.model = model;
    headerView = new HeaderView(model);
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data
    enterKeyLbl.textProperty().set("EnterKey");
    keyDescriptionLbl.textProperty().set("Test");
    exampleLbl.textProperty().set("Number of characters: 9\nExample: 123456789");
    instructionLbl.textProperty().set("Instructions");

    validationImgView = new ImageView(getClass()
        .getClassLoader()
        .getResource("dialog-error.png")
        .toExternalForm());                     // TODO: Check what to do here.

    connectBtn.textProperty().set("Connect");
    connectBtn.setDisable(true);
    expandOptionBtn.textProperty().set("More");

    keyInputPane.setText("Key Input");
    keyInputPane.setExpanded(true);

    predefinedAdressesPane.setText("Predefined Adresses");
    predefinedAdressesPane.setExpanded(false);
  }

  private void layoutForm() {
    enterKeyLbl.setId("EnterKeyLbl");

    keyDescriptionLbl.setWrapText(true);

    keyFld.setFont(new Font(30)); // TODO: Move to CSS

    validationImgView.setSmooth(true);

    keyValidationBox.getChildren().addAll(keyFld, validationImgView);
    keyValidationBox.setSpacing(5);       // TODO: Move to CSS.
    keyValidationBox.setHgrow(keyFld, Priority.ALWAYS);
    keyValidationBox.setAlignment(Pos.CENTER_LEFT);

    groupingBox.getChildren().addAll(keyValidationBox, instructionLbl);

    centerBox.getChildren().addAll(enterKeyLbl,
        keyDescriptionLbl,
        exampleLbl,
        groupingBox,
        connectBtn,
        expandOptionBtn);

    keyInputPane.setContent(centerBox);
    // TODO: Set content for predefinedAdressesPane

    connectBtn.setFont(new Font(30));       // TODO: Move to CSS.
    setCenter(keyInputPane);
    setTop(headerView);
    setBottom(predefinedAdressesPane);
  }


  private void bindFieldsToModel() {
    // make bindings to the model

  }

}

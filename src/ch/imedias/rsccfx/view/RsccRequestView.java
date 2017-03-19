package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

// TODO: Clean up messy code!

/**
 * Defines all elements shown in the request section.
 */
public class RsccRequestView extends BorderPane {
  private final Rscc model;

  HeaderView headerView;

  Label keyGenerationLbl = new Label();
  Label supporterAdminLbl = new Label();

  VBox topBox = new VBox();
  VBox mainBox = new VBox();
  VBox bottomBox = new VBox();

  HBox expandableBox = new HBox();
  VBox centerBox = new VBox();
  HBox keyGeneratingBox = new HBox();

  Text descriptionTxt = new Text();
  Text additionalDescriptionTxt = new Text();

  TextField generatedKeyFld = new TextField();

  Button reloadKeyBtn = new Button();
  Button supporterAdminBtn = new Button();

  /**
   * Initializes all the GUI components needed generate the token the supporter needs.
   */
  public RsccRequestView(Rscc model) {
    this.model = model;
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data
    // TODO: String Class implementation!
    headerView = new HeaderView(model);

    keyGenerationLbl.textProperty().set("Key generator"); // TODO: String Class
    keyGenerationLbl.setId("keyGenerationLbl");

    supporterAdminLbl.textProperty().set("Supporter administration"); // TODO: String Class

    descriptionTxt.textProperty().set("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
        + "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, "
        + "sed diam voluptua. At vero eos et accusam "
        + "et justo duo dolores et ea rebum. Stet clita kasd gubergren,"
        + " no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    descriptionTxt.setId("descriptionTxt"); // TODO: Styling

    generatedKeyFld.setPrefHeight(60); // FIXME: Has this to be in the CSS?
    generatedKeyFld.setEditable(false); // FIXME: Has this to be in the CSS?
    generatedKeyFld.setId("generatedKeyFld");

    reloadKeyBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader()
        .getResource("images/reload.png").toExternalForm())));
    reloadKeyBtn.setPrefHeight(50); // FIXME: Has this to be in the CSS?
    reloadKeyBtn.setPrefWidth(50); // FIXME: Has this to be in the CSS?

    ImageView imageView = new ImageView((new Image(getClass().getClassLoader()
        .getResource("images/arrowDown.png").toExternalForm())));
    imageView.setFitHeight(15); // FIXME: Has this to be in the CSS?
    imageView.setFitWidth(15); // FIXME: Has this to be in the CSS?
    supporterAdminBtn.setGraphic(imageView);

    // TODO: Implement String Class
    additionalDescriptionTxt.textProperty().set("Lorem ipsum dolor sit amet"
        + "consetetur sadipscing elitr, "
        + "sed diam nonumy eirmod tempor invidunt "
        + "ut labore et dolore magna aliquyam erat, sed diam voluptua. "
        + "At vero eos et accusam et justo duo dolores et ea rebum. Stet "
        + "clita kasd gubergren, no sea takimata sanctus est "
        + "Lorem ipsum dolor sit amet.");
    additionalDescriptionTxt.setId("additionalDescriptionTxt");
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    centerBox.setId("centerBox");
    bottomBox.setId("bottomBox");

    expandableBox.getChildren().addAll(supporterAdminBtn, supporterAdminLbl);
    keyGeneratingBox.getChildren().addAll(generatedKeyFld, reloadKeyBtn);

    topBox.getChildren().add(headerView);
    centerBox.getChildren().addAll(keyGenerationLbl, descriptionTxt, keyGeneratingBox,
        additionalDescriptionTxt);
    bottomBox.getChildren().add(expandableBox);

    setTop(topBox);
    setCenter(centerBox);
    setBottom(bottomBox);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
    generatedKeyFld.textProperty().bind(model.keyProperty());
  }
}



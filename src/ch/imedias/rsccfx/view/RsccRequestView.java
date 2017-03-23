package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

  HBox supporterAdminBox = new HBox();
  VBox centerBox = new VBox();
  HBox keyGeneratingBox = new HBox();

  TitledPane keyGeneratorPane = new TitledPane();
  TitledPane supporterAdminPane = new TitledPane();

  Text descriptionTxt = new Text();
  Text additionalDescriptionTxt = new Text();

  TextField generatedKeyFld = new TextField();

  Button reloadKeyBtn = new Button();
  // Button supporterAdminBtn = new Button();

  Image reloadImg;

  ImageView reloadImgView;

  /**
   * Initializes all the GUI components needed generate the token the supporter needs.
   */
  public RsccRequestView(Rscc model) {
    this.model = model;
    SvgImageLoaderFactory.install();
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

    InputStream reloadImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/reload.svg");
    reloadImg = new Image(reloadImagePath);
    reloadImgView = new ImageView(reloadImg);
    reloadImgView.fitWidthProperty().set(50); // FIXME: Has this to be in the CSS?
    reloadImgView.fitWidthProperty().set(50); // FIXME: Has this to be in the CSS?
    reloadImgView.setPreserveRatio(true);
    reloadKeyBtn.setGraphic(reloadImgView);
    reloadKeyBtn.setPrefHeight(50); // FIXME: Has this to be in the CSS?
    reloadKeyBtn.setPrefWidth(50); // FIXME: Has this to be in the CSS?

    // ImageView imageView = new ImageView((new Image(getClass().getClassLoader()
    //     .getResource("images/arrowDown.png").toExternalForm())));
    // imageView.setFitHeight(15); // FIXME: Has this to be in the CSS?
    // imageView.setFitWidth(15); // FIXME: Has this to be in the CSS?
    // supporterAdminBtn.setGraphic(imageView);

    // TODO: Implement String Class
    additionalDescriptionTxt.textProperty().set("Lorem ipsum dolor sit amet"
        + "consetetur sadipscing elitr, "
        + "sed diam nonumy eirmod tempor invidunt "
        + "ut labore et dolore magna aliquyam erat, sed diam voluptua. "
        + "At vero eos et accusam et justo duo dolores et ea rebum. Stet "
        + "clita kasd gubergren, no sea takimata sanctus est "
        + "Lorem ipsum dolor sit amet.");
    additionalDescriptionTxt.setId("additionalDescriptionTxt");

    // TODO: Implement String Class
    keyGeneratorPane.setText("Key generator");
    keyGeneratorPane.setExpanded(true);

    supporterAdminPane.setText("Supporter Administration");
    supporterAdminPane.setExpanded(false);

  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    centerBox.setId("centerBox");
    bottomBox.setId("bottomBox");

    // TODO: replace content in supporterAdminBox and delete arrowDown.png afterwards.
    supporterAdminBox.getChildren().addAll(/*supporterAdminBtn, */supporterAdminLbl);
    keyGeneratingBox.getChildren().addAll(generatedKeyFld, reloadKeyBtn);

    topBox.getChildren().add(headerView);
    centerBox.getChildren().addAll(keyGenerationLbl, descriptionTxt, keyGeneratingBox,
        additionalDescriptionTxt);
    bottomBox.getChildren().add(supporterAdminBox);

    keyGeneratorPane.setContent(centerBox);
    supporterAdminPane.setContent(bottomBox);

    setTop(topBox);
    setCenter(keyGeneratorPane);
    setBottom(supporterAdminPane);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
    generatedKeyFld.textProperty().bind(model.keyProperty());
  }
}



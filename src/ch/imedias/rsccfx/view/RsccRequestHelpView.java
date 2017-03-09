package ch.imedias.rsccfx.view;

/**
 * import statements.
 */

import ch.imedias.rsccfx.ScreenLoader;
import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Class RsccRequestHelpView
 * Created by Simon on 30.11.16.
 * Capsulated Class to test easy.
 */
public class RsccRequestHelpView extends BorderPane {
  private Rscc model;
  ScreenLoader screenLoader;
  BorderPane pane;
  HeaderView testTopbox;
  VBox topBox;
  HBox centerBox;
  VBox bottomBox;
  VBox bottomBoxInset;
  HBox expandableBox;
  Label keyGenerationLbl;
  Label supporterAdminLbl;
  Text descriptionTxt;
  Text additionalDescriptionTxt;
  TextField generatedKeyFld;
  Button reloadButton;
  Button supporterAdminBtn;

  /**
   * Constructor.
   * @param model test.
   */
  public RsccRequestHelpView(Rscc model, ScreenLoader screenLoader) {
    this.model = model;
    this.screenLoader = screenLoader;
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    pane = new BorderPane();
    topBox = new VBox();

    centerBox = new HBox();
    bottomBox = new VBox();
    bottomBoxInset = new VBox();
    expandableBox = new HBox();

    centerBox.setPadding(new Insets(10, 20, 10, 20));
    bottomBox.setPadding(new Insets(10, 20, 1, 20));
    bottomBoxInset.setPadding(new Insets(40, 20, 1, 20));
  }

  private void initFieldData() {
    //populate fields which require initial data
    // TODO: String Class implementation!
    // TODO: Resize behavior

    keyGenerationLbl = new Label("Schlüsselgenerierung");
    keyGenerationLbl.setFont(new Font("Cantarell", 30));

    supporterAdminLbl = new Label("Supporter Administration");
    descriptionTxt = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
        + "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, "
        + "sed diam voluptua. At vero eos et accusam "
        + "et justo duo dolores et ea rebum. Stet clita kasd gubergren,"
        + " no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    descriptionTxt.setWrappingWidth(450);


    generatedKeyFld = new TextField();
    generatedKeyFld.setPrefHeight(60);

    generatedKeyFld.setEditable(false);
    generatedKeyFld.setStyle("-fx-background-color: #e2e2e2;"); // TODO: Create styling sheet
    generatedKeyFld.setText("aw3k2ljfsl0Oo");
    generatedKeyFld.setFont(Font.font("Monospaced", 30));

    reloadButton = new Button();
    reloadButton.setGraphic(new ImageView(new Image(getClass().getClassLoader()
        .getResource("images/reload.png").toExternalForm())));
    reloadButton.setPrefHeight(50);
    reloadButton.setPrefWidth(50);

    supporterAdminBtn = new Button(); // TODO: Resize Image
    ImageView imageView = new ImageView((new Image(getClass().getClassLoader()
            .getResource("images/arrowDown.png").toExternalForm())));
    imageView.setFitHeight(15);
    imageView.setFitWidth(15);
    supporterAdminBtn.setGraphic(imageView);


    // TODO: Implement String Class
    additionalDescriptionTxt = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
        + "sed diam nonumy eirmod tempor invidunt "
        + "ut labore et dolore magna aliquyam erat, sed diam voluptua. "
        + "At vero eos et accusam et justo duo dolores et ea rebum. Stet "
        + "clita kasd gubergren, no sea takimata sanctus est "
        + "Lorem ipsum dolor sit amet.");
    additionalDescriptionTxt.setWrappingWidth(450);
  }

  private void bindFieldsToModel() {
    // TODO: make the bindings to the model
    testTopbox = new HeaderView(model);
    topBox.getChildren().add(testTopbox);
    VBox lbltxt1 = new VBox();
    lbltxt1.getChildren().add(keyGenerationLbl);
    lbltxt1.getChildren().add(descriptionTxt);
    lbltxt1.setPadding(new Insets(10, 20, 10, 20));
    topBox.getChildren().add(lbltxt1);
    centerBox.getChildren().add(generatedKeyFld);
    centerBox.getChildren().add(reloadButton);
    bottomBox.getChildren().add(additionalDescriptionTxt);
    bottomBox.getChildren().add(bottomBoxInset);
    bottomBox.getChildren().add(expandableBox);
    expandableBox.getChildren().addAll(supporterAdminBtn, supporterAdminLbl);

    pane.setTop(topBox);
    pane.setCenter(centerBox);
    pane.setBottom(bottomBox);
    this.getChildren().add(pane);

    generatedKeyFld.textProperty().bind(model.keyProperty());
  }

  /** initSize method. */
  public void initSize(Scene scene) {
    topBox.prefWidthProperty().bind(scene.widthProperty());
    generatedKeyFld.prefWidthProperty().bind(scene.widthProperty().subtract(80));
    descriptionTxt.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    additionalDescriptionTxt.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    testTopbox.initSize(scene);
  }
}




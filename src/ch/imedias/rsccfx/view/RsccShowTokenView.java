package ch.imedias.rsccfx.view;

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

public class RsccShowTokenView extends BorderPane {
  private Rscc model;
  BorderPane pane; // FIXME: rename all fields to comply with the SAD and google java code guidelines
  HeaderView testTopbox;
  VBox boxTop;
  HBox boxCenter;
  VBox boxBottom;
  VBox boxBottomInset;
  Label lbl;
  Text txt1;
  Text txt2;
  TextField tf;
  Button reloadButton;

  /**
   * Constructor. // FIXME: change according to the google java code guidelines
   */
  public RsccShowTokenView(Rscc model) {
    this.model = model;
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    // FIXME: restructure and group all of the objects that are being used together
    pane = new BorderPane();
    boxTop = new VBox();

    boxCenter = new HBox();
    boxBottom = new VBox();
    boxBottomInset = new VBox();


    boxCenter.setPadding(new Insets(10, 20, 10, 20));
    boxBottom.setPadding(new Insets(10, 20, 1, 20));
    boxBottomInset.setPadding(new Insets(40, 20, 1, 20));
  }

  private void initFieldData() {
    //populate fields which require initial data
    // FIXME: make sure only to put things here that have to do with populating initial data
    // FIXME: restructure and group all of the objects that are being used together

    lbl = new Label("Schlüsselgenerierung");
    lbl.setFont(new Font("Cantarell", 30));
    // FIXME: get rid of lorem ipsum and replace
    txt1 = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
        + "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, "
        + "sed diam voluptua. At vero eos et accusam "
        + "et justo duo dolores et ea rebum. Stet clita kasd gubergren,"
        + " no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    txt1.setWrappingWidth(450);


    tf = new TextField();
    tf.setPrefHeight(60);
    tf.setEditable(false);
    tf.setStyle("-fx-background-color: #e2e2e2;");
    tf.setText("aw3k2ljfsl0Oo");
    tf.setFont(Font.font("Monospaced", 30));

    reloadButton = new Button();
    reloadButton.setGraphic(new ImageView(new Image(getClass().getClassLoader()
        .getResource("images/reload.png").toExternalForm())));
    reloadButton.setPrefHeight(50);
    reloadButton.setPrefWidth(50);

    // FIXME: get rid of lorem ipsum and replace
    txt2 = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
        + "sed diam nonumy eirmod tempor invidunt "
        + "ut labore et dolore magna aliquyam erat, sed diam voluptua. "
        + "At vero eos et accusam et justo duo dolores et ea rebum. Stet "
        + "clita kasd gubergren, no sea takimata sanctus est "
        + "Lorem ipsum dolor sit amet.");
    txt2.setWrappingWidth(450);


  }

  private void bindFieldsToModel() {
    //make the bindings to the model
    // FIXME: only the last line binds things to the model, the rest belongs in layoutForm
    testTopbox = new HeaderView();
    boxTop.getChildren().add(testTopbox);
    VBox lbltxt1 = new VBox();
    lbltxt1.getChildren().add(lbl);
    lbltxt1.getChildren().add(txt1);
    lbltxt1.setPadding(new Insets(10, 20, 10, 20));
    boxTop.getChildren().add(lbltxt1);
    boxCenter.getChildren().add(tf);
    boxCenter.getChildren().add(reloadButton);
    boxBottom.getChildren().add(txt2);
    boxBottom.getChildren().add(boxBottomInset);


    pane.setTop(boxTop);
    pane.setCenter(boxCenter);
    pane.setBottom(boxBottom);
    this.getChildren().add(pane);

    tf.textProperty().bind(model.keyProperty());
  }

  /** initSize method. */ // FIXME: format javadoc comment according to the google java code style guidelines
  public void initSize(Scene scene) {
    // FIXME: duplicate code with HeaderView??
    boxTop.prefWidthProperty().bind(scene.widthProperty());
    tf.prefWidthProperty().bind(scene.widthProperty().subtract(80));
    // FIXME: get rid of magic numbers (according to google java code style guidelines)
    txt1.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    txt2.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    testTopbox.initSize(scene);
  }
}




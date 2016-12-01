package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by Simon on 30.11.16.
 * Capsulated Class to test easy
 */
public class ShowTokenView extends BorderPane {
  private Rscc model;
  private BorderPane pane;
  HBox header;
  VBox boxTop;
  HBox boxCenter;
  VBox boxBottom;
  VBox boxBottomInset;
  Label lbl;
  Button suppAdmin;
  Text txt1;
  Text txt2;
  TextField tf;
  Button reloadButton;
  Label lblHead;
  Button btnHead;
  Button btnSett;
  Button btnHelp;

  public ShowTokenView(Rscc model) {
    this.model = model;
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    pane = new BorderPane();
    boxTop = new VBox();
    boxCenter = new HBox();
    boxBottom = new VBox();
    boxBottomInset = new VBox();
    header = new HBox();
    boxTop.getChildren().add(header);

    header.setPadding(new Insets(5, 5, 20, 0));
    boxTop.setPadding(new Insets(5, 20, 5, 20));
    boxCenter.setPadding(new Insets(10, 20, 10, 20));
    boxBottom.setPadding(new Insets(10, 20, 1, 20));
    boxBottomInset.setPadding(new Insets(40, 20, 1, 20));
  }

  private void initFieldData() {
    //populate fields which require initial data
    btnHead = new Button();
    btnHead.setAlignment(Pos.TOP_LEFT);
    btnHead.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/back1.png").toExternalForm())));
    btnHelp = new Button();
    btnHelp.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/help1.png").toExternalForm())));
    btnSett = new Button();
    btnSett.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/setting1.png").toExternalForm())));
    btnSett.setAlignment(Pos.TOP_RIGHT);
    lblHead = new Label ("I need Help ");
    lblHead.setFont(new Font("Cantarell", 20));
    lblHead.setAlignment(Pos.CENTER);

    header.getChildren().add(btnHead);
    header.getChildren().add(lblHead);
    header.getChildren().add(btnHelp);
    header.getChildren().add(btnSett);



    lbl = new Label("Schlüsselgenerierung");
    lbl.setFont(new Font("Cantarell", 30));
    txt1 = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    txt1.setWrappingWidth(450);


    tf = new TextField();
    tf.setPrefHeight(60);
    tf.setEditable(false);
    tf.setStyle("-fx-background-color: #e2e2e2;");
    tf.setText("aw3k2ljfsl0Oo");
    tf.setFont(Font.font("Monospaced", 30));

    reloadButton = new Button();
    reloadButton.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/reload.png").toExternalForm())));
    reloadButton.setPrefHeight(50);
    reloadButton.setPrefWidth(50);


    txt2 = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    txt2.setWrappingWidth(450);

    suppAdmin = new Button("Supporter Administration");
    suppAdmin.setFont(new Font("Cantarell", 20));

  }

  private void bindFieldsToModel() {
    //make the bindings to the model
    boxTop.getChildren().add(lbl);
    boxTop.getChildren().add(txt1);
    boxCenter.getChildren().add(tf);
    boxCenter.getChildren().add(reloadButton);
    boxBottom.getChildren().add(txt2);
    boxBottom.getChildren().add(boxBottomInset);
    boxBottomInset.getChildren().add(suppAdmin);

    pane.setTop(boxTop);
    pane.setCenter(boxCenter);
    pane.setBottom(boxBottom);
    this.getChildren().add(pane);
  }

  public void initSize(Scene scene) {
    lblHead.prefWidthProperty().bind(scene.widthProperty());
    boxTop.prefWidthProperty().bind(scene.widthProperty());
    header.prefWidthProperty().bind(scene.widthProperty());
    tf.prefWidthProperty().bind(scene.widthProperty().subtract(80));
    txt1.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));
    txt2.wrappingWidthProperty().bind(scene.widthProperty().subtract(50));

  }
}




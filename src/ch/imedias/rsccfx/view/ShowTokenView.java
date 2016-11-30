package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.application.Application.launch;


/**
 * Created by Simon on 30.11.16.
 * Capsulated Class to test easy
 */
public class ShowTokenView extends Parent {
  private Rscc model;
  private BorderPane pane;
  VBox boxTop;
  HBox boxCenter;
  HBox boxBottom;
  Label lbl;
  Text txt1;
  Text txt2;
  TextField tf;
  Button reloadButton;

  public void ShowTokenView(Rscc model) {
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
    boxBottom = new HBox();

    boxTop.setPadding(new Insets(50, 20, 50, 20));
    boxCenter.setPadding(new Insets(10, 20, 10, 20));
    boxBottom.setPadding(new Insets(10, 20, 100, 20));
  }

  private void initFieldData() {
    //populate fields which require initial data
    lbl = new Label("Schlüsselgenerierung");
    lbl.setFont(new Font("Cambria", 20));
    txt1 = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    txt1.setWrappingWidth(450);


    tf = new TextField();
    tf.setPrefWidth(400);
    tf.setPrefHeight(50);
    tf.setText("aw3k2ljfsl");

    reloadButton = new Button("reload");
    reloadButton.setPrefHeight(50);


    txt2 = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    txt2.setWrappingWidth(450);
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
    boxTop.getChildren().add(lbl);
    boxTop.getChildren().add(txt1);
    boxCenter.getChildren().add(tf);
    boxCenter.getChildren().add(reloadButton);
    boxCenter.getChildren().add(tf);
    boxCenter.getChildren().add(reloadButton);
    boxBottom.getChildren().add(txt2);


    Scene scene = new Scene(pane, 500, 500);
    pane.setTop(boxTop);
    pane.setCenter(boxCenter);
    pane.setBottom(boxBottom);

  }
}




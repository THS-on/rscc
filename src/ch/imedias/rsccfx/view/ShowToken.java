package ch.imedias.rsccfx.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Simon on 30.11.16.
 * Capsulated Class to test easy
 */
public class ShowToken extends Application {

  public static void main(String[] args) {
    launch(args);
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    BorderPane pane = new BorderPane();
    Scene scene = new Scene(pane, 500, 500);

    VBox boxTop = new VBox();
    HBox boxCenter = new HBox();
    HBox boxBottom = new HBox();
    boxTop.setPadding(new Insets(50, 20, 50, 20));
    boxCenter.setPadding(new Insets(10, 20, 10, 20));
    boxBottom.setPadding(new Insets(10, 20, 100, 20));

    Label lbl = new Label("Schlüsselgenerierung");
    lbl.setFont(new Font("Cambria", 20));
    boxTop.getChildren().add(lbl);
    Text txt = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    txt.setWrappingWidth(450);
    boxTop.getChildren().add(txt);

    TextField tf = new TextField();
    tf.setPrefWidth(400);
    tf.setPrefHeight(50);
    tf.setText("aw3k2ljfsl");

    Button reloadButton = new Button("reload");
    reloadButton.setPrefHeight(50);
    boxCenter.getChildren().add(tf);
    boxCenter.getChildren().add(reloadButton);

    Text txt2 = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    txt2.setWrappingWidth(450);
    boxBottom.getChildren().add(txt2);

    pane.setTop(boxTop);
    pane.setCenter(boxCenter);
    pane.setBottom(boxBottom);
    primaryStage.setTitle("Remote Support");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

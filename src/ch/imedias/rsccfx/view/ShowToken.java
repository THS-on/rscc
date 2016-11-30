package ch.imedias.rsccfx.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Simon on 30.11.16.
 * Capsulated Class to test easy
 */
public class ShowToken extends Application {

  public static void main(String[] args){
    launch(args);
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    BorderPane pane = new BorderPane();
    Scene scene = new Scene(pane,300,300);

    VBox boxTop = new VBox();
    HBox boxCenter = new HBox();
    HBox boxBottom = new HBox();
    boxCenter.setPadding(new Insets(50,20,50,20));

    Text lbl = new Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");



    boxTop.getChildren().add(lbl);
    TextField tf = new TextField();
    tf.setPrefWidth(200);
    tf.setText("aw3k2ljfsl");
    boxCenter.getChildren().add(tf);


    pane.setTop(boxTop);
    pane.setCenter(boxCenter);
    pane.setBottom(boxBottom);
    primaryStage.setTitle("Remote Support");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

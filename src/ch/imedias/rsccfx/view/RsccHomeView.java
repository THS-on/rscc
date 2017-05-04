package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Defines all elements shown on the start page.
 */
public class RsccHomeView extends BorderPane {
  private static final Logger LOGGER =
      Logger.getLogger(RsccHomeView.class.getName());

  private final Rscc model;
  private final Strings strings = new Strings();
  final HeaderView headerView;

  final Button requestViewBtn = new Button();
  final Button supportViewBtn = new Button();

  final VBox contentBox = new VBox();

  Image requestImg;
  Image supportImg;

  ImageView requestImgView;
  ImageView supportImgView;

  //String big1 = "I need help\n";
  //String small1 = "Get somebody to help you remotely";
  //Font big11 = new Font("I need help\n",40);
  Text tbig = new Text ( "I need help\n");
  Text tsmall = new Text ("Get somebody to help you remotely");


  /**
   * Initializes all the GUI components needed on the start page.
   *
   * @param model the model to handle the data.
   */
  public RsccHomeView(Rscc model) {
    this.model = model;
    headerView = new HeaderView(model);
    SvgImageLoaderFactory.install();
    initFieldData();
    layoutForm();
    bindFieldsToModel();

  }

  private void initFieldData() {
    //new Approach

    HBox hBox = new HBox();
    VBox vBox = new VBox();
    Label label1 = new Label("I need help\n");
    label1.setFont(new Font(35));
    Label label2 = new Label("Get somebody to help you remotely");
    label2.setFont(new Font(20));
    InputStream requestHelpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/needHelp.svg");
    requestImg = new Image(requestHelpImagePath);
    requestImgView = new ImageView(requestImg);
    requestImgView.setPreserveRatio(true);
    requestImgView.setId("requestImgView");
    vBox.getChildren().addAll(label1, label2);
    vBox.setAlignment(Pos.CENTER_LEFT);
    hBox.getChildren().addAll(requestImgView, vBox);
    requestViewBtn.setGraphic(hBox);

    supportViewBtn.textProperty().setValue("I want to assist someone\nSomebody needs my help");
  }

  private void layoutForm() {


    //InputStream requestHelpImagePath = getClass().getClassLoader()
      //  .getResourceAsStream("images/needHelp.svg");
    //requestImg = new Image(requestHelpImagePath);
    //requestImgView = new ImageView(requestImg);
    //requestImgView.setPreserveRatio(true);


    //requestViewBtn.setGraphic(requestImgView);
    requestViewBtn.getStyleClass().add("HomeNavigationBtn");

    InputStream offerSupportImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/support.svg");
    supportImg = new Image(offerSupportImagePath);
    supportImgView = new ImageView(supportImg);
    supportImgView.setPreserveRatio(true);
    supportViewBtn.setGraphic(supportImgView);
    supportViewBtn.getStyleClass().add("HomeNavigationBtn");

    contentBox.setId("contentBox");

    contentBox.getChildren().addAll(requestViewBtn, supportViewBtn);

    this.setTop(headerView);
    this.setCenter(contentBox);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }

  /* Code from Mr. Standtke
  * Button button = new Button();
        HBox hBox = new HBox();
        Label label1 = new Label("small");
        label1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label1.setFont(new Font(10));
        Label label2 = new Label("large");
        label2.setFont(new Font(20));
        hBox.getChildren().addAll(label1, label2);
        button.setGraphic(hBox);
  *
  * */
}

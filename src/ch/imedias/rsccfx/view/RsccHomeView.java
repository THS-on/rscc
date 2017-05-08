package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Defines all elements shown on the start page.
 */
public class RsccHomeView extends BorderPane {
  private static final Logger LOGGER =
      Logger.getLogger(RsccHomeView.class.getName());

  private final Rscc model;
  final HeaderView headerView;

  final Button requestViewBtn = new Button();
  final Button supportViewBtn = new Button();

  final VBox contentBox = new VBox();

  Image requestImg;
  Image supportImg;

  ImageView requestImgView;
  ImageView supportImgView;

  final HBox requestBox = new HBox();
  final VBox requestBoxLabels = new VBox();
  final HBox supportBox = new HBox();
  final VBox supportBoxLabels = new VBox();

  Label requestBigLbl;
  Label requestSmallLbl;
  Label supportBigLbl;
  Label supportSmallLbl;


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
    requestBigLbl = new Label("I need help");
    requestSmallLbl = new Label("Get somebody to help you remotely");
    supportBigLbl = new Label("I want to assist someone");
    supportSmallLbl = new Label("Somebody needs my help");
  }

  private void layoutForm() {

    requestBigLbl.getStyleClass().add("BigFont");
    requestSmallLbl.getStyleClass().add("SmallFont");

    InputStream requestHelpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/needHelp.svg");
    requestImg = new Image(requestHelpImagePath);
    requestImgView = new ImageView(requestImg);
    requestImgView.setPreserveRatio(true);
    requestBoxLabels.getChildren().addAll(requestBigLbl, requestSmallLbl);
    requestBoxLabels.setAlignment(Pos.CENTER_LEFT);
    requestBox.getChildren().addAll(requestImgView, requestBoxLabels);
    requestViewBtn.setGraphic(requestBox);
    requestViewBtn.getStyleClass().add("HomeNavigationBtn");

    supportBigLbl.getStyleClass().add("BigFont");
    supportSmallLbl.getStyleClass().add("SmallFont");

    InputStream offerSupportImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/support.svg");
    supportImg = new Image(offerSupportImagePath);
    supportImgView = new ImageView(supportImg);
    supportImgView.setPreserveRatio(true);
    supportBoxLabels.getChildren().addAll(supportBigLbl, supportSmallLbl);
    supportBoxLabels.setAlignment(Pos.CENTER_LEFT);
    supportBox.getChildren().addAll(supportImgView, supportBoxLabels);
    supportViewBtn.setGraphic(supportBox);
    supportViewBtn.getStyleClass().add("HomeNavigationBtn");

    supportBoxLabels.setPadding(new Insets(0,20,0,20));
    requestBoxLabels.setPadding(new Insets(0,20,0,20));

    contentBox.setId("contentBox");

    contentBox.getChildren().addAll(requestViewBtn, supportViewBtn);

    this.setTop(headerView);
    this.setCenter(contentBox);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }

}

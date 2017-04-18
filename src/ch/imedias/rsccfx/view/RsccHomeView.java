package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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

  /**
   * Initializes all the GUI components needed on the start page.
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
    // populate fields which require initial data
    requestViewBtn.textProperty().setValue("I need help\nGet somebody to help you remotely");
    supportViewBtn.textProperty().setValue("I want to help someone\nSomebody needs my help");
  }

  private void layoutForm() {
    InputStream requestHelpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/needHelp.svg");
    requestImg = new Image(requestHelpImagePath);
    requestImgView = new ImageView(requestImg);
    requestImgView.setPreserveRatio(true);
    requestViewBtn.setGraphic(requestImgView);
    requestViewBtn.getStyleClass().add("HomeNavigationBtn");

    InputStream offerSupportImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/support.svg");
    supportImg = new Image(offerSupportImagePath);
    supportImgView = new ImageView(supportImg);
    supportImgView.setPreserveRatio(true);
    supportViewBtn.setGraphic(supportImgView);
    supportViewBtn.getStyleClass().add("HomeNavigationBtn");

    contentBox.getChildren().addAll(requestViewBtn, supportViewBtn);

    this.setTop(headerView);
    this.setCenter(contentBox);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }
}

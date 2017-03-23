package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * Defines all elements shown on the start page.
 */
public class RsccHomeView extends BorderPane {
  private final Rscc model;

  Button requestViewBtn = new Button();
  Button supportViewBtn = new Button();
  Image requestSupportImg;
  ImageView requestSupportImgView;

  /**
   * Initializes all the GUI components needed on the start page.
   */
  public RsccHomeView(Rscc model) {
    this.model = model;
    SvgImageLoaderFactory.install();
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data
    //TODO: replace Text, multilangual
    requestViewBtn.textProperty().setValue("I need help");
    supportViewBtn.textProperty().setValue("I want to help someone");
  }

  private void layoutForm() {
    // TODO: Resizing of pictures and size!
    InputStream requestHelpImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/help-browser.svg");
    requestSupportImg = new Image(requestHelpImagePath);
    requestSupportImgView = new ImageView(requestSupportImg);
    requestSupportImgView.setPreserveRatio(true);
    requestViewBtn.setGraphic(requestSupportImgView);
    requestViewBtn.getStyleClass().add("HomeNavigationBtn");

    InputStream offerSupportImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/audio-headset.svg");
    Image offerSupportImg = new Image(offerSupportImagePath);
    supportViewBtn.setGraphic(new ImageView(offerSupportImg));
    supportViewBtn.getStyleClass().add("HomeNavigationBtn");

    this.setLeft(requestViewBtn);
    this.setRight(supportViewBtn);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }
}

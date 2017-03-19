package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
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

  /**
   * Initializes all the GUI components needed on the start page.
   */
  public RsccHomeView(Rscc model) {
    this.model = model;
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
    String requestHelpImagePath = getClass().getClassLoader()
        .getResource("images/help-browser.png").toExternalForm();
    Image requestSupportImg = new Image(requestHelpImagePath);
    requestViewBtn.setGraphic(new ImageView(requestSupportImg));
    requestViewBtn.setId("HomeNavigationBtn");

    String offerSupportImagePath = getClass().getClassLoader()
        .getResource("images/audio-headset.png").toExternalForm();
    Image offerSupportImg = new Image(offerSupportImagePath);
    supportViewBtn.setGraphic(new ImageView(offerSupportImg));
    supportViewBtn.setId("HomeNavigationBtn");

    this.setLeft(requestViewBtn);
    this.setRight(supportViewBtn);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }
}

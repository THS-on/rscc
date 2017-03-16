package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * Defines all elements shown on the start page.
 */
public class RsccHomeView extends BorderPane {
  private final Rscc model;
  protected Button requestHelpBtn;
  protected Button offerSupportBtn;

  /**
   * Initializes all the GUI components needed on the start page.
   *
   * @param model defines what is displayed.
   */
  public RsccHomeView(Rscc model) {
    this.model = model;
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    requestHelpBtn = new Button();
    offerSupportBtn = new Button();
  }

  private void layoutForm() {
    requestHelpBtn.textProperty().setValue("I need help"); //TODO: replace Text, multilangual
    String requestHelpImagePath = getClass().getClassLoader()
        .getResource("images/help-browser.png").toExternalForm();
    Image requestSupportImg = new Image(requestHelpImagePath);
    requestHelpBtn.setGraphic(new ImageView(requestSupportImg));
    requestHelpBtn.setId("HomeNavigationBtn");

    offerSupportBtn.textProperty().setValue("I want to help someone"); // TODO: replace Text, multilangual
    String offerSupportImagePath = getClass().getClassLoader()
        .getResource("images/audio-headset.png").toExternalForm();
    Image offerSupportImg = new Image(offerSupportImagePath);
    offerSupportBtn.setGraphic(new ImageView(offerSupportImg));
    offerSupportBtn.setId("HomeNavigationBtn");

    this.setLeft(requestHelpBtn);
    this.setRight(offerSupportBtn);
  }

  private void bindFieldsToModel() {
    //dynamic growth

  }
}

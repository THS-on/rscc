package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * Created by pwg on 30.11.16.
 */
public class RsccHomeView extends BorderPane {
  private final Rscc model;
  private final RsccHomeViewPresenter presenter;
  protected Button requestSupportBtn;
  protected Button offerSupportBtn;

  /**
   * your javadoc comment goes here.
   *
   * @param model is the model
   */
  public RsccHomeView(Rscc model) {
    this.model = model;
    initFieldData();
    this.presenter = new RsccHomeViewPresenter(model, this);
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {

    requestSupportBtn = new Button();
    requestSupportBtn.textProperty().setValue("I need help"); //TODO: replace Text, multilangual
    String filepath = getClass().getClassLoader()
        .getResource("images/help-browser.png").toExternalForm();
    Image requestSupportImg = new Image(filepath);
    requestSupportBtn.setGraphic(new ImageView(requestSupportImg));
    offerSupportBtn = new Button();
    //

    offerSupportBtn.textProperty().setValue("I want to help someone");
    // TODO: replace Text, multilangual
    String filepath2 = getClass().getClassLoader()
        .getResource("images/audio-headset.png").toExternalForm();
    Image offerSupportImg = new Image(filepath2);
    offerSupportBtn.setGraphic(new ImageView(offerSupportImg));
    requestSupportBtn.setId("HomeNavigationBtn");
    offerSupportBtn.setId("HomeNavigationBtn");


  }

  private void bindFieldsToModel() {
    //dynamic growth

  }


  private void layoutForm() {

    this.setLeft(requestSupportBtn);
    this.setRight(offerSupportBtn);


  }

  /**
   * @param scene initially loaded scene by RsccApp.
   */
  public void initBtnPanel(Scene scene) {
    offerSupportBtn.prefWidthProperty().bind(scene.widthProperty().divide(2));
    offerSupportBtn.prefHeightProperty().bind(scene.heightProperty());
    requestSupportBtn.prefWidthProperty().bind(scene.widthProperty().divide(2));
    requestSupportBtn.prefHeightProperty().bind(scene.heightProperty());
  }
}

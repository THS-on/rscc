package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Created by pwg on 30.11.16.
 */
public class RsccHomeView extends Parent {
  private final Rscc model;
  private final RsccHomeViewPresenter presenter;
  protected Button requestSupportBtn;
  protected Button offerSupportBtn;
  private HBox homeBox;


  public RsccHomeView(Rscc model) {
    this.model = model;
    initFieldData();
    this.presenter = new RsccHomeViewPresenter(model, this);
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    requestSupportBtn = new Button();
    requestSupportBtn.textProperty().setValue("Test"); //TODO: replace Text, multilangual
    offerSupportBtn = new Button();
    offerSupportBtn.textProperty().setValue("Test2"); // TODO: replace Text, multilangual
    requestSupportBtn.setId("HomeNavigationBtn");
    offerSupportBtn.setId("HomeNavigationBtn");
    homeBox = new HBox();

  }

  private void bindFieldsToModel() {


  }

  private void layoutForm() {

    homeBox.getChildren().add(requestSupportBtn);
    homeBox.getChildren().add(offerSupportBtn);
    this.getChildren().add(homeBox);



  }


}

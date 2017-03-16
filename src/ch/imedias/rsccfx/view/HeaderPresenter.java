package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class HeaderPresenter {
  private final Rscc model;
  private HeaderView view;

  /**
   * TODO: Javadoc comment here.
   */
  public HeaderPresenter(Rscc model, HeaderView view) {
    this.model = model;
    this.view = view;
    // attachEvents();
  }

  private void attachEvents() {
    // view.backBtn.setOnAction(event -> showHomeView());
  }

  /* private void showHomeView() {
    Stage stage = (Stage) view.getScene().getWindow();
    stage.setScene(new Scene(new RsccHomeView(model)));
  } */

  /**
   * TODO: Javadoc comment here.
   * @param action is used to give the method a lambda expression.
   */
  public void setBackBtnAction(EventHandler<ActionEvent> action) {
    view.backBtn.setOnAction(action);
  }

  /**
   * TODO: Javadoc comment here.
   * @param title is used to hand in a new string.
   */
  public void setHeaderTitle(String title) {
    view.headLbl.textProperty().set(title);
  }

  /**
   * TODO: Javadoc comment here.
   * @param action is used to give the method a lambda expression.
   */
  public void setHelpBtnAction(EventHandler<ActionEvent> action) {
    view.helpBtn.setOnAction(action);
  }

  /**
   * TODO: Javadoc comment here.
   * @param action is used to give the method a lambda expression.
   */
  public void setSettingsBtnAction(EventHandler<ActionEvent> action) {
    view.settBtn.setOnAction(action);
  }

  /**
   * TODO: Javadoc comment here.
   * initSize method.
   * */
  public void initSize(Scene scene) {
    // view.sep.prefWidthProperty().bind(scene.widthProperty());
    view.headLbl.prefWidthProperty().bind(scene.widthProperty());
    view.buttonBox.prefWidthProperty().bind(scene.widthProperty());
    // view.separatorBox.prefWidthProperty().bind(scene.widthProperty());
    view.headerBox.prefWidthProperty().bind(scene.widthProperty());


  }
  
}

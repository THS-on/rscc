package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

/**
 * Presenter class of HeaderView. Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class HeaderPresenter {
  private final Rscc model;   // FIXME: François, do we need the model in this specific presenter?
  private HeaderView view;

  /**
   * Initializes a new HeaderPresenter with the according view.
   *
   * @param model the presentation model to coordinate views.
   * @param view  the view which needs to be configured.
   */
  public HeaderPresenter(Rscc model, HeaderView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Sets the behaviour of the back button in the HeaderView.
   *
   * @param action is used to give the method a lambda expression and defines what happens
   *               when the back button is clicked.
   */
  public void setBackBtnAction(EventHandler<ActionEvent> action) {
    view.backBtn.setOnAction(action);
  }

  /**
   * Sets the title in the middle of the HeaderView.
   *
   * @param title is used to hand in a new string.
   */
  public void setHeaderTitle(String title) {
    view.headLbl.textProperty().set(title);
  }

  /**
   * Sets the behaviour of the help button in the HeaderView.
   *
   * @param action is used to give the method a lambda expression and defines what happens
   *               when the help button is clicked.
   */
  public void setHelpBtnAction(EventHandler<ActionEvent> action) {
    view.helpBtn.setOnAction(action);
  }

  /**
   * Sets the behaviour of the settings button in the HeaderView.
   *
   * @param action is used to give the method a lambda expression and defines what happens
   *               when the settings button is clicked.
   */
  public void setSettingsBtnAction(EventHandler<ActionEvent> action) {
    view.settBtn.setOnAction(action);
  }

  /**
   * Initializes the size of the whole HeaderView elements.
   *
   * @param scene is needed to get the initial window size.
   */
  public void initSize(Scene scene) {
    view.headLbl.prefWidthProperty().bind(scene.widthProperty());
    view.headerBox.prefWidthProperty().bind(scene.widthProperty());
  }

}

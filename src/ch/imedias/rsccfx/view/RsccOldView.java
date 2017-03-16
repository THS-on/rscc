package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

// TODO: Decide if class needs to get deleted.

/**
 * This is the old GUI of Swing in JavaFX.
 */
public class RsccOldView extends BorderPane { // change Parent to GridPane etc.
  private final Rscc model;
  protected Button testbts = new Button();
  //declare all elements here

  /**
   * Javadoc comment here.
   */
  public RsccOldView(Rscc model) {
    this.model = model;
    testbts.textProperty().setValue("test");
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    this.setCenter(testbts);
  }

  private void initFieldData() {
    //populate fields which require initial data
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
  }


}



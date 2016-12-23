package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class RsccView extends BorderPane { // change Parent to GridPane etc.
  private final Rscc model;
  final ShowTokenView showToken;
  //declare all elements here

  /** Javadoc comment here. */
  public RsccView(Rscc model) {
    this.model = model;
    showToken = new ShowTokenView(model);
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    setCenter(showToken);
  }

  private void initFieldData() {
    //populate fields which require initial data
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
  }


}



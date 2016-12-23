package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.layout.BorderPane;

public class RsccView extends BorderPane { // change Parent to GridPane etc.
  private final Rscc model;
  final RsccShowTokenView showTokenView;
  //declare all elements here

  /** Javadoc comment here. */
  public RsccView(Rscc model, RsccShowTokenView showTokenView) {
    this.model = model;
    this.showTokenView = showTokenView;
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    setCenter(showTokenView);
  }

  private void initFieldData() {
    //populate fields which require initial data
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
  }


}



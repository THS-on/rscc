package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.layout.BorderPane;

public class RsccView extends BorderPane { // change Parent to GridPane etc.
  private final Rscc model;
  final RsccShowTokenView showTokenView;
  final RsccEnterTokenView enterTokenView;
  //declare all elements here

  // FIXME: Use this as main View and base all of the views around it on this
  /**
   * Javadoc comment here.
   */
  public RsccView(Rscc model, RsccShowTokenView showTokenView, RsccEnterTokenView enterTokenView) {
    this.model = model;
    this.showTokenView = showTokenView;
    this.enterTokenView = enterTokenView;
    layoutForm();
    initFieldData();
    bindFieldsToModel();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    setLeft(showTokenView);
    setRight(enterTokenView);
  }

  private void initFieldData() {
    //populate fields which require initial data
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
  }


}



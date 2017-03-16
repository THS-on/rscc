package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.layout.BorderPane;

/**
 * TODO: Javadoc comment here.
 */
public class RsccViewDemoReady extends BorderPane { // change Parent to GridPane etc.
  private final Rscc model;
  final RsccRequestHelpView showTokenView;
  final RsccSupporterView enterTokenView;
  //declare all elements here

  /**
   * TODO: Javadoc comment here.
   * @param model
   * @param showTokenView
   * @param enterTokenView
   */
  public RsccViewDemoReady(Rscc model, RsccRequestHelpView showTokenView,
                           RsccSupporterView enterTokenView) {
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



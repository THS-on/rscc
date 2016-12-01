package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RsccEnterTokenView extends VBox {

  private final Rscc model;
  private final RsccEnterTokenPresenter presenter;

  //declare all elements here
  Label enterTokenlbl;
  Label loremIpsumlbl;
  Label examplelbl;
  Label instructionlbl;

  HBox tokenValidationbox;
  TextField tokentxt;
  ImageView isValidimg;

  Button connectbtn;
  Button expandOptionbtn;

  /**
   * Javadoc comment here.
   */
  public RsccEnterTokenView(Rscc model) {
    this.model = model;
    this.presenter = new RsccEnterTokenPresenter(model, this);

    initFieldData();
    bindFieldsToModel();
    layoutForm();
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    tokenValidationbox = new HBox();
    tokentxt = new TextField();
    isValidimg = new ImageView(
            new Image(presenter.validationImage(tokentxt.getText())));
    tokenValidationbox.getChildren().addAll(tokentxt, isValidimg);

    this.getChildren().addAll(enterTokenlbl,
            loremIpsumlbl,
            examplelbl,
            tokenValidationbox,
            instructionlbl,
            connectbtn,
            expandOptionbtn);
  }

  private void initFieldData() {
    //populate fields which require initial data
//    enterTokenlbl = new Label(Strings.remoteSupportFrameEnterTokenLabelText);
//    loremIpsumlbl = new Label(Strings.remoteSupportFrameDescriptionLabelText);
//    examplelbl = new Label(Strings.remoteSupportFrameUsageExampleLabelText);
//    instructionlbl = new Label(Strings.remoteSupportFrameInstructionLabelText);
//    connectbtn = new Button(Strings.remoteSupportDialogConnectButtonText);
//    expandOptionbtn = new Button(Strings.remoteSupportDialogExpandButtonText);

    enterTokenlbl = new Label("EnterToken");
    loremIpsumlbl = new Label("lorem Ipsum");
    examplelbl = new Label("this is an example");
    instructionlbl = new Label("Instructions!");
    connectbtn = new Button("Beam me up scotty");
    expandOptionbtn = new Button("MOARRR");
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
  }

}

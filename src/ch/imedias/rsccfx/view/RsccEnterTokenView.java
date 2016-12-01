package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
    this.setPadding(new Insets(5, 25, 5, 25));
    
    enterTokenlbl.setFont(new Font(50));
    
    loremIpsumlbl.setWrapText(true);
    
    tokenValidationbox = new HBox();
    tokentxt = new TextField();
    isValidimg = new ImageView(
            new Image(presenter.validationImage(tokentxt.getText())));
    isValidimg.setFitHeight(25);
    isValidimg.setFitWidth(25);
    isValidimg.setSmooth(true);
    tokenValidationbox.getChildren().addAll(tokentxt, isValidimg);
    tokenValidationbox.setPadding(new Insets(5, 5, 30, 0));

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
    //enterTokenlbl = new Label(Strings.remoteSupportFrameEnterTokenLabelText);
    //loremIpsumlbl = new Label(Strings.remoteSupportFrameDescriptionLabelText);
    //examplelbl = new Label(Strings.remoteSupportFrameUsageExampleLabelText);
    //instructionlbl = new Label(Strings.remoteSupportFrameInstructionLabelText);
    //connectbtn = new Button(Strings.remoteSupportDialogConnectButtonText);
    //expandOptionbtn = new Button(Strings.remoteSupportDialogExpandButtonText);

    enterTokenlbl = new Label("EnterToken");
    loremIpsumlbl = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    examplelbl = new Label("this is an example");
    instructionlbl = new Label("Instructions!");
    connectbtn = new Button("Beam me up scotty");
    expandOptionbtn = new Button("MOARRR");
  }

  private void bindFieldsToModel() {
    //make the bindings to the model
  }

}

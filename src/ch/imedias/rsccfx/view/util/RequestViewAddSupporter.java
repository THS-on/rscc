package ch.imedias.rsccfx.view.util;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RequestViewAddSupporter extends DialogPane {

  final Label nameLbl = new Label("Name");
  final Label adressLbl = new Label("Adress");
  final Label portLbl = new Label("Port");
  final Label pictureLbl = new Label("Picture");
  final Label chargeableLbl = new Label("Chargeable");
  final Label encryptedLbl = new Label("Encrypted");

  final TextField nameTxt = new TextField("Ronny");
  final TextField adressTxt = new TextField("127.0.0.1");
  final TextField portTxt = new TextField("5900");
  final TextField pictureTxt = new TextField("/images/sup.jpg");

  /*final Image supporterImg;
  final ImageView supporterImgView;*/

  final Button cancelBtn = new Button("Cancel");
  final Button okBtn = new Button("OK");

  final CheckBox chargeableChk = new CheckBox();
  final CheckBox encryptedChk = new CheckBox();

  // TODO: Add "Opened folder"

  public RequestViewAddSupporter() {

    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {

  }

  private void layoutForm() {
    this.getChildren().addAll(nameLbl, nameTxt);
  }

  private void bindFieldsToModel() {

  }

}
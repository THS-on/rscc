package ch.imedias.rsccfx.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Creates the DialogPane by SupporterButton click.
 */
public class SupporterAttributesDialog extends DialogPane {

  Dialog dialog = new Dialog();
  GridPane gridPane = new GridPane();

  private Supporter supporter;

  final Label nameLbl = new Label();
  final Label adressLbl = new Label();
  final Label portLbl = new Label();
  final Label pictureLbl = new Label();
  final Label chargeableLbl = new Label();
  final Label encryptedLbl = new Label();

  final TextField nameFld = new TextField();
  final TextField addressFld = new TextField();
  final TextField portFld = new TextField();
  final TextField pictureFld = new TextField();

  final ButtonType applyBtn = ButtonType.APPLY;
  final Button dummyBtn = new Button("dummy");

  final CheckBox chargeableCBox = new CheckBox();
  final CheckBox encryptedCBox = new CheckBox();


  /**
   * Initializes all the GUI components needed in the DialogPane.
   * @param supporter = the supporter for the dialog.
   */
  public SupporterAttributesDialog(Supporter supporter) {
    this.supporter = supporter;

    initFieldData();
    layoutForm();
    bindFieldsToModel();
    dialog.show();
  }

  private void initFieldData() {
    // populate fields which require initial data
    nameLbl.setText("Name");
    adressLbl.setText("Adress");
    portLbl.setText("Port");
    pictureLbl.setText("Picture");
    chargeableLbl.setText("Chargeable");
    encryptedLbl.setText("Encrypted");

    nameFld.setText(supporter.getDescription());
    addressFld.setText(supporter.getAddress());
    portFld.setText(supporter.getPort());
    pictureFld.setText("/images/sup.jpg");
    chargeableCBox.setSelected(supporter.isChargeable());
    encryptedCBox.setSelected(supporter.isEncrypted());

  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    gridPane.setHgap(20);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(25, 25, 25, 25));
    dialog.setResizable(true);
    dialog.setHeight(500);
    dialog.setWidth(500);

    gridPane.add(nameLbl,0,0);
    gridPane.add(nameFld,1,0);
    gridPane.add(adressLbl,0,1);
    gridPane.add(addressFld,1,1);
    gridPane.add(portLbl,0,2);
    gridPane.add(portFld,1,2);
    gridPane.add(pictureLbl,0,3);
    gridPane.add(pictureFld,1,3);
    gridPane.add(chargeableLbl,0,4);
    gridPane.add(chargeableCBox,1,4);
    gridPane.add(encryptedLbl,0,5);
    gridPane.add(encryptedCBox,1,5);
    gridPane.add(dummyBtn, 1, 6);

    this.getButtonTypes().add(applyBtn);

    this.setContent(gridPane);
    dialog.setDialogPane(this);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
    //TODO bindings
  }
}

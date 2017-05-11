package ch.imedias.rsccfx.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Creates the DialogPane by SupporterButton click.
 */
public class SupporterAttributesDialog extends DialogPane {

  final Dialog dialog = new Dialog();
  final GridPane attributePane = new GridPane();

  final Label nameLbl = new Label();
  final Label adressLbl = new Label();
  final Label portLbl = new Label();
  final Label pictureLbl = new Label();
  final Label chargeableLbl = new Label();
  final Label encryptedLbl = new Label();

  final TextField nameFld = new TextField();
  final TextField adressFld = new TextField();
  final TextField portFld = new TextField();
  final TextField pictureFld = new TextField();

  final CheckBox chargeableCBox = new CheckBox();
  final CheckBox encryptedCBox = new CheckBox();


  /**
   * Initializes all the GUI components needed in the DialogPane.
   */
  public SupporterAttributesDialog() {
    dialog.setTitle("Supporter Details");
    String styleSheet = getClass().getClassLoader()
        .getResource("css/styles.css").toExternalForm();
    this.getStylesheets().add(styleSheet);
    initFieldData();
    layoutForm();
    bindFieldsToModel();
    dialog.show();
  }

  private void initFieldData() {
    // populate fields which require initial data

    nameLbl.setText("Name");
    nameLbl.setId("txt");
    adressLbl.setText("Adress");
    portLbl.setText("Port");
    pictureLbl.setText("Picture");
    chargeableLbl.setText("Chargeable");
    encryptedLbl.setText("Encrypted");

    nameFld.setText("Ronny");
    nameFld.getStyleClass().add("gridPane");
    nameFld.setEditable(false);
    nameFld.setDisable(true);
    adressFld.setText("127.0.0.1");
    adressFld.setEditable(false);
    adressFld.setDisable(true);
    portFld.setText("5900");
    portFld.setEditable(false);
    portFld.setDisable(true);
    pictureFld.setText("/images/sup.jpg");
    pictureFld.setEditable(false);
    pictureFld.setDisable(true);

    chargeableCBox.setDisable(true);
    encryptedCBox.setDisable(true);

    // Set Hgrow for TextField
    attributePane.setHgrow(adressFld, Priority.ALWAYS);
    attributePane.getStyleClass().add("gridPane");

  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    attributePane.setHgap(20);
    attributePane.setVgap(10);
    attributePane.setPadding(new Insets(25, 25, 25, 25));
    attributePane.autosize();
    //Resizable is false because form would look bad with big font size
    dialog.setResizable(false);
    dialog.setHeight(500);
    dialog.setWidth(500);

    attributePane.add(nameLbl,0,0);
    attributePane.add(nameFld,1,0);
    attributePane.add(adressLbl,0,1);
    attributePane.add(adressFld,1,1);
    attributePane.add(portLbl,0,2);
    attributePane.add(portFld,1,2);
    attributePane.add(pictureLbl,0,3);
    attributePane.add(pictureFld,1,3);
    attributePane.add(chargeableLbl,0,4);
    attributePane.add(chargeableCBox,1,4);
    attributePane.add(encryptedLbl,0,5);
    attributePane.add(encryptedCBox,1,5);

    this.createButtonBar();

    this.getButtonTypes().add(ButtonType.CLOSE);

    this.setContent(attributePane);
    dialog.setDialogPane(this);

  }


  private void bindFieldsToModel() {
    // make bindings to the model
  }

  @Override
  public ButtonBar createButtonBar() {
    final ButtonBar buttonBar = new ButtonBar();
    buttonBar.getStyleClass().add("buttonBar");

    final Button connectButton = new Button("Call");
    final Button editButton = new Button("Edit");
    final Button applyButton = new Button(" Close ");

    // Create the buttons to go into the ButtonBar
    ButtonBar.setButtonData(connectButton, ButtonBar.ButtonData.YES);

    ButtonBar.setButtonData(editButton, ButtonBar.ButtonData.NO);
    editButton.setOnAction(event -> {
      changeEditable(true);
      applyButton.setText("Apply");
      connectButton.setVisible(false);
    });

    ButtonBar.setButtonData(applyButton, ButtonBar.ButtonData.APPLY);
    applyButton.setOnAction(event -> {
      changeEditable(false);
      if (applyButton.getText().equals("Close")) {
        dialog.close();

      } else {
        applyButton.setText("Close");
        connectButton.setVisible(true);
      }
    });

    // Add buttons to the ButtonBar
    buttonBar.getButtons().addAll(connectButton, editButton,applyButton);

    return buttonBar;
  }

  private void changeEditable(boolean bool) {
    nameFld.setEditable(bool);
    nameFld.setDisable(!bool);
    adressFld.setEditable(!bool);
    adressFld.setDisable(!bool);
    portFld.setEditable(!bool);
    portFld.setDisable(!bool);
    pictureFld.setEditable(!bool);
    pictureFld.setDisable(!bool);
    chargeableCBox.setDisable(!bool);
    encryptedCBox.setDisable(!bool);

  }

}

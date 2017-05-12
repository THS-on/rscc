package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.localization.Strings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
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

  Strings strings = new Strings();

  final Label nameLbl = new Label();
  final Label addressLbl = new Label();
  final Label portLbl = new Label();
  final Label pictureLbl = new Label();
  final Label chargeableLbl = new Label();
  final Label encryptedLbl = new Label();

  final TextField nameFld = new TextField();
  final TextField addressFld = new TextField();
  final TextField portFld = new TextField();
  final TextField pictureFld = new TextField();

  final CheckBox chargeableCBox = new CheckBox();
  final CheckBox encryptedCBox = new CheckBox();

  /**
   * Initializes all the GUI components needed in the DialogPane.
   */
  public SupporterAttributesDialog() {
    // TODO: 4K usw.?
    this.getStylesheets().add(RsccApp.styleSheet);
    initFieldData();
    layoutForm();
    bindFieldsToModel();
    dialog.show();
  }

  private void initFieldData() {
    // populate fields which require initial data
    // TODO: Strings class
    dialog.setTitle(strings.dialogTitleText);
    nameLbl.setText(strings.dialogNameText);
    nameLbl.setId("txt");
    addressLbl.setText(strings.dialogAddressText);
    portLbl.setText(strings.dialogPortText);
    pictureLbl.setText(strings.dialogImageText);
    chargeableLbl.setText(strings.dialogChargeableLbl);
    encryptedLbl.setText(strings.dialogEncryptedLbl);

    nameFld.setText("Ronny");
    nameFld.getStyleClass().add("gridPane");
    nameFld.setEditable(false);
    nameFld.setDisable(true);
    addressFld.setText("127.0.0.1");
    addressFld.setEditable(false);
    addressFld.setDisable(true);
    portFld.setText("5900");
    portFld.setEditable(false);
    portFld.setDisable(true);
    pictureFld.setText("/images/sup.jpg");
    pictureFld.setEditable(false);
    pictureFld.setDisable(true);

    chargeableCBox.setDisable(true);
    encryptedCBox.setDisable(true);

    // Set Hgrow for TextField
    attributePane.setHgrow(addressFld, Priority.ALWAYS);
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
    attributePane.setId("dialogAttributePane");

    attributePane.add(nameLbl, 0, 0);
    attributePane.add(nameFld, 1, 0);
    attributePane.add(addressLbl, 0, 1);
    attributePane.add(addressFld, 1, 1);
    attributePane.add(portLbl, 0, 2);
    attributePane.add(portFld, 1, 2);
    attributePane.add(pictureLbl, 0, 3);
    attributePane.add(pictureFld, 1, 3);
    attributePane.add(chargeableLbl, 0, 4);
    attributePane.add(chargeableCBox, 1, 4);
    attributePane.add(encryptedLbl, 0, 5);
    attributePane.add(encryptedCBox, 1, 5);

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
    buttonBar.getButtons().addAll(connectButton, editButton, applyButton);

    return buttonBar;
  }

  private void changeEditable(boolean bool) {
    nameFld.setEditable(bool);
    nameFld.setDisable(!bool);
    addressFld.setEditable(!bool);
    addressFld.setDisable(!bool);
    portFld.setEditable(!bool);
    portFld.setDisable(!bool);
    pictureFld.setEditable(!bool);
    pictureFld.setDisable(!bool);
    chargeableCBox.setDisable(!bool);
    encryptedCBox.setDisable(!bool);

  }

}

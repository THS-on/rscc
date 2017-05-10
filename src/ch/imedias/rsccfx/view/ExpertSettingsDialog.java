package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.controlsfx.control.ToggleSwitch;

/**
 * Created by user on 10.05.17.
 */
public class ExpertSettingsDialog extends DialogPane {

  Dialog dialog = new Dialog();
  GridPane gridPane = new GridPane();
  Strings strings = new Strings();

  final Label keyserverIpLbl = new Label();
  final Label forceConnectOverServerLbl = new Label();
  final Label keyServerHttpPortLbl = new Label();
  final Label vncPortLbl = new Label();
  final Label icePortLbl = new Label();
  final Label udpPackageSizeLbl = new Label();
  final Label localForwadingPortLbl = new Label();
  final Label stunServerLbl = new Label();
  final Label stunServersLbl = new Label();

  final ToggleSwitch forceConnectOverServerTgl = new ToggleSwitch();

  final TextField keyServerIpFld = new TextField();
  final TextField keyServerHttpPortFld = new TextField();
  final TextField vncPortFld = new TextField();
  final TextField icePortFld = new TextField();
  final TextField udpPackageSizeFld = new TextField();
  final TextField localForwardingPortFld = new TextField();
  final TextField stunServerPortFld = new TextField();
  final ListView stunSeversList = new ListView();


  /**
   * Initializes all the GUI components needed in the DialogPane.
   */
  public ExpertSettingsDialog() {
    initFieldData();
    layoutForm();
    bindFieldsToModel();
    dialog.show();
  }

  private void initFieldData() {
    // populate fields which require initial data
    forceConnectOverServerLbl.textProperty().set(strings.expertForceConnectOverServerLbl);
    keyserverIpLbl.textProperty().set(strings.expertKeyserverIpLbl);
    keyServerHttpPortLbl.textProperty().set(strings.expertKeyserverHttpLbl);
    vncPortLbl.textProperty().set(strings.expertVncPortLbl);
    icePortLbl.textProperty().set(strings.expertIcePortLbl);
    udpPackageSizeLbl.textProperty().set(strings.expertUdpPackageSizeLbl);
    localForwadingPortLbl.textProperty().set(strings.expertForwardingPortLbl);
    stunServersLbl.textProperty().set(strings.expertStunserverLbl);
    stunServerLbl.textProperty().set(strings.expertStunServerPortLbl);
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    gridPane.setHgap(20);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(25, 25, 25, 25));
    dialog.setResizable(true);
    dialog.setHeight(500);
    dialog.setWidth(500);

    gridPane.add(forceConnectOverServerLbl,0,0);
    gridPane.add(forceConnectOverServerTgl,1,0);
    gridPane.add(keyserverIpLbl,0,1);
    gridPane.add(keyServerIpFld,1,1);
    gridPane.add(keyServerHttpPortLbl,0,2);
    gridPane.add(keyServerHttpPortFld,1,2);
    gridPane.add(vncPortLbl,0,3);
    gridPane.add(vncPortFld,1,3);
    gridPane.add(icePortLbl,0,4);
    gridPane.add(icePortFld,1,4);
    gridPane.add(udpPackageSizeLbl,0,5);
    gridPane.add(udpPackageSizeFld,1,5);
    gridPane.add(localForwadingPortLbl, 0, 6);
    gridPane.add(localForwardingPortFld,1,6);
    gridPane.add(stunServerLbl,0,7);
    gridPane.add(stunServerPortFld,1,7);
    gridPane.add(stunServersLbl, 0, 8);
    gridPane.add(stunSeversList,1,8);

    this.getButtonTypes().add(ButtonType.APPLY);
    this.getButtonTypes().add(ButtonType.CLOSE);
    this.getButtonTypes().add(ButtonType.PREVIOUS);


    this.setContent(gridPane);
    dialog.setDialogPane(this);
  }


  private void bindFieldsToModel() {
    // make bindings to the model
  }

}



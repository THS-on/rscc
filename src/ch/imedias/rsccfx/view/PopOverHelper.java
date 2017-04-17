package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

/**
 * @author Lukas Marchesi
 * @date 17.04.2017.
 */
public class PopOverHelper {

  private final Rscc model;
  VBox requestSettingsBox = new VBox();
  VBox requestHelpBox = new VBox();
  PopOver settingsPopOver = new PopOver(requestSettingsBox);
  PopOver helpPopOver = new PopOver(requestHelpBox);
  Label compressionLbl = new Label();
  Label qualityLbl = new Label();
  Label bitSettingsLbl = new Label();
  Label bitCurrentSettingsLbl = new Label();
  Slider compressionSldr;
  Slider qualitySldr;
  Label helpLbl = new Label();
  ToggleButton toggleBtn = new ToggleButton();
  private ViewController viewParent;

  public PopOverHelper(ViewController viewParent, Rscc model) {
    this.viewParent = viewParent;
    this.model = model;
    initFieldData();
    layoutForm();
    bindFieldsToModel();
    initChangeListeners();
  }

  private void initFieldData() {
    // populate fields which require initial data
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)

    // Settings PopOver - request
    compressionLbl.textProperty().set("Kompression");
    compressionLbl.setId("compressionLbl");

    compressionSldr = new Slider(0, 100, 30);
    compressionSldr.setId("compressionSldr");

    qualityLbl.textProperty().set("Qualität");
    qualitySldr = new Slider(0, 100, 10);

    bitSettingsLbl.textProperty().set("8-Bit-Farben");
    bitSettingsLbl.setId("bitSettingsLbl");

    toggleBtn.textProperty().set("On");
    toggleBtn.setId("toggleBtn");

    bitCurrentSettingsLbl.textProperty().set("Ihre momentane Einstellung ist");
    bitCurrentSettingsLbl.setId("bitCurrentSettingsLbl");

    requestSettingsBox.getChildren().add(new HBox(compressionLbl, compressionSldr));
    requestSettingsBox.getChildren().add(new HBox(qualityLbl, qualitySldr));
    requestSettingsBox.getChildren().add(new HBox(bitSettingsLbl, toggleBtn));
    requestSettingsBox.getChildren().add(bitCurrentSettingsLbl);

    settingsPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
    settingsPopOver.setDetachable(false);

    // Help popover - request
    helpLbl.textProperty().set("The remote support tool allows you to get help " +
        "or help someone in need");
    helpLbl.setId("helpLbl");

    // TODO: If we have more labels, we can add it to the box.
    requestHelpBox.getChildren().addAll(helpLbl);

    helpPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
    helpPopOver.setDetachable(false);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }

  private void initChangeListeners() {
    viewParent.nameActiveViewProperty().addListener(observable -> changingView());
  }

  private void changingView() {
    switch (viewParent.getNameActiveView()) {
      case "home":
        helpPopOver.setContentNode(homeHelpBox);
        settingsPopOver.setContentNode(null);
        break;
      case "requestHelp":
        helpPopOver.setContentNode(requestHelpBox);
        settingsPopOver.setContentNode(requestSettingsBox);
        break;
      case "supporter":
        helpPopOver.setContentNode(supporterHelpBox);
        settingsPopOver.setContentNode(supporterSettingsBox);
        break;
      default:
        helpPopOver.setContentNode(null);
        settingsPopOver.setContentNode(null);
        break;
    }

  }

}

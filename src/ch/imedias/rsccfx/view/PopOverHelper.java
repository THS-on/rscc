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
  VBox homeHelpBox = new VBox();
  VBox requestSettingsBox = new VBox();
  VBox requestHelpBox = new VBox();
  VBox supporterSettingsBox = new VBox();
  VBox supporterHelpBox = new VBox();
  PopOver settingsPopOver = new PopOver(requestSettingsBox);
  PopOver helpPopOver = new PopOver(requestHelpBox);
  Label homeHelpLbl = new Label();
  Label requestCompressionLbl = new Label();
  Label requestQualityLbl = new Label();
  Label requestBitSettingsLbl = new Label();
  Label requestBitCurrentSettingsLbl = new Label();
  Label requestHelpLbl = new Label();
  Slider compressionSldr;
  Slider qualitySldr;
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

    // Help PopOver - Home
    homeHelpLbl.textProperty().set("Diese Applikation erlaubt Ihnen, " +
        "jemandem zu helfen oder Hilfe zu bekommen");
    homeHelpLbl.setId("homeHelpLbl");

    homeHelpBox.getChildren().add(new HBox(homeHelpLbl));

    // Settings PopOver - request
    requestCompressionLbl.textProperty().set("Kompression");
    requestCompressionLbl.setId("requestCompressionLbl");

    compressionSldr = new Slider(0, 100, 30);
    compressionSldr.setId("compressionSldr");

    requestQualityLbl.textProperty().set("Qualität");
    qualitySldr = new Slider(0, 100, 10);

    requestBitSettingsLbl.textProperty().set("8-Bit-Farben");
    requestBitSettingsLbl.setId("requestBitSettingsLbl");

    toggleBtn.textProperty().set("On");
    toggleBtn.setId("toggleBtn");

    requestBitCurrentSettingsLbl.textProperty().set("Ihre momentane Einstellung ist");
    requestBitCurrentSettingsLbl.setId("requestBitCurrentSettingsLbl");

    requestSettingsBox.getChildren().add(new HBox(requestCompressionLbl, compressionSldr));
    requestSettingsBox.getChildren().add(new HBox(requestQualityLbl, qualitySldr));
    requestSettingsBox.getChildren().add(new HBox(requestBitSettingsLbl, toggleBtn));
    requestSettingsBox.getChildren().add(requestBitCurrentSettingsLbl);

    // Help popover - request
    requestHelpLbl.textProperty().set("The remote support tool allows you to get help " +
        "or help someone in need");
    requestHelpLbl.setId("requestHelpLbl");

    // TODO: If we have more labels, we can add it to the box.
    requestHelpBox.getChildren().addAll(requestHelpLbl);

    // Settings PopOver - supporter

    /*requestCompressionLbl.textProperty().set("Kompression");
    requestCompressionLbl.setId("requestCompressionLbl");

    compressionSldr = new Slider(0, 100, 30);
    compressionSldr.setId("compressionSldr");

    requestQualityLbl.textProperty().set("Qualität");
    qualitySldr = new Slider(0, 100, 10);

    requestBitSettingsLbl.textProperty().set("8-Bit-Farben");
    requestBitSettingsLbl.setId("requestBitSettingsLbl");

    toggleBtn.textProperty().set("On");
    toggleBtn.setId("toggleBtn");

    requestBitCurrentSettingsLbl.textProperty().set("Ihre momentane Einstellung ist");
    requestBitCurrentSettingsLbl.setId("requestBitCurrentSettingsLbl");*/

    supporterSettingsBox.getChildren().add(new HBox(requestCompressionLbl, compressionSldr));
    supporterSettingsBox.getChildren().add(new HBox(requestQualityLbl, qualitySldr));
    supporterSettingsBox.getChildren().add(new HBox(requestBitSettingsLbl, toggleBtn));
    supporterSettingsBox.getChildren().add(requestBitCurrentSettingsLbl);

    // Help popover - supporter
    requestHelpLbl.textProperty().set("The remote support tool allows you to get help " +
        "or help someone in need");
    requestHelpLbl.setId("requestHelpLbl");

    // TODO: If we have more labels, we can add it to the box.
    supporterHelpBox.getChildren().addAll(requestHelpLbl);

    // PopOver related
    settingsPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
    settingsPopOver.setDetachable(false);
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
    // FIXME: It is not working... yet.
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

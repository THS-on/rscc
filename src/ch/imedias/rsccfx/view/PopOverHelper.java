package ch.imedias.rsccfx.view;

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

  VBox settingsBox = new VBox();
  VBox helpBox = new VBox();

  PopOver settingsPopOver = new PopOver(settingsBox);
  PopOver helpPopOver = new PopOver(helpBox);

  Label compressionLbl = new Label();
  Label qualityLbl = new Label();
  Label bitSettingsLbl = new Label();
  Label bitCurrentSettingsLbl = new Label();

  Slider compressionSldr;
  Slider qualitySldr;

  Label helpLbl = new Label();

  ToggleButton toggleBtn = new ToggleButton();

  public PopOverHelper() {
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)

    // Settings PopOver
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

    settingsBox.getChildren().add(new HBox(compressionLbl, compressionSldr));
    settingsBox.getChildren().add(new HBox(qualityLbl, qualitySldr));
    settingsBox.getChildren().add(new HBox(bitSettingsLbl, toggleBtn));
    settingsBox.getChildren().add(bitCurrentSettingsLbl);

    settingsPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);

    // Help popover
    helpLbl.textProperty().set("The remote support tool allows you to get help " +
        "or help someone in need");
    helpLbl.setId("helpLbl");

    // TODO: If we have more labels, we can add it to the box.
    helpBox.getChildren().addAll(helpLbl);

    helpPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }

}

package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;


/**
 * @author Lukas Marchesi
 * @date 17.04.2017.
 */
public class PopOverHelper {
  // Get Screensize
  Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

  private static final int COMPRESSION_MAX = 9;
  private static final int COMPRESSION_MIN = 0;
  private static final int COMPRESSION_VALUE = 6;

  private static final int QUALITY_MAX = 9;
  private static final int QUALITY_MIN = 0;
  private static final int QUALITY_VALUE = 6;

  private final double OVERLAY_HEIGHT = primaryScreenBounds.getHeight() / 4;
  private final double OVERLAY_WIDTH = primaryScreenBounds.getWidth() / 9;

  private final double SLIDER_WIDTH = OVERLAY_WIDTH / 1.2;
  private final double START_X_SLIDER = (OVERLAY_WIDTH / 2) - (SLIDER_WIDTH / 2);

  RsccApp rsccApp = new RsccApp();

  private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);

  private final Rscc model;
  private ViewController viewParent;

  ToggleSwitch eightBitTgl = new ToggleSwitch();
  ToggleSwitch viewOnlyTgl = new ToggleSwitch();

  VBox homeHelpBox = new VBox();
  VBox requestSettingsBox = new VBox();
  VBox requestHelpBox = new VBox();
  VBox supporterSettingsBox = new VBox();
  VBox supporterHelpBox = new VBox();

  PopOver settingsPopOver = new PopOver();
  PopOver helpPopOver = new PopOver();

  Text compressionSliderTxt = new Text();
  Text qualitySliderTxt = new Text();

  Label requestCompressionLbl = new Label();
  Label requestQualityLbl = new Label();
  Label requestBitSettingsLbl = new Label();
  Label requestBitCurrentSettingsLbl = new Label();
  Label requestViewOnlyLbl = new Label();
  Label homeHelpLbl = new Label();
  Label requestHelpLbl = new Label();
  Label supporterHelpLbl = new Label();
  Slider compressionSldr;

  Slider qualitySldr;
  Slider pictureScalingSldr;

  Pane compressionSliderPane = new Pane();
  Pane requestSettingsPane = new Pane();
  Pane qualitySliderPane = new Pane();

  // TODO: 8 bit Toggle is according to SA not needed anymore.
  ToggleButton eightBitToggl = new ToggleButton();
  ToggleButton showOnlyToggl = new ToggleButton();

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

    eightBitTgl.getStyleClass().add("toggles");
    viewOnlyTgl.getStyleClass().add("toggles");

    // Help PopOver - Home
    homeHelpLbl.textProperty().set("Diese Applikation erlaubt Ihnen, " +
        "jemandem zu helfen oder Hilfe zu bekommen");
    homeHelpLbl.setId("homeHelpLbl");

    homeHelpBox.getChildren().add(new HBox(homeHelpLbl));

    // Settings PopOver - request
    // Settings PopOver TODO: StringsClass

    // Compression Settings
    compressionSldr = new Slider(COMPRESSION_MIN, COMPRESSION_MAX, COMPRESSION_VALUE) {
      @Override
      protected void layoutChildren() {
        super.layoutChildren();

        Region thumb = (Region) lookup(".thumb");
        if (thumb != null) {
          compressionSliderTxt.setLayoutX(
              thumb.getLayoutX()
                  + thumb.getWidth() / 2
                  - compressionSliderTxt.getLayoutBounds().getWidth() / 2
                  + START_X_SLIDER
          );
        }
      }
    };

    compressionSldr.setLayoutY(40);
    compressionSldr.setPrefWidth(SLIDER_WIDTH);

    compressionSliderTxt.setTextOrigin(VPos.TOP);
    compressionSliderTxt.textProperty().bind(
        compressionSldr.valueProperty().asString("%,.0f"));
    compressionSliderTxt.getStyleClass().add("sliderTxts");
    qualitySliderTxt.getStyleClass().add("sliderTxts");

    requestCompressionLbl.textProperty().set("Kompression");
    requestCompressionLbl.getStyleClass().add("sliderLbls");


    // Quality Settings
    qualitySldr = new Slider(QUALITY_MIN, QUALITY_MAX, QUALITY_VALUE) {
      @Override
      protected void layoutChildren() {
        super.layoutChildren();

        Region thumb = (Region) lookup(".thumb");
        if (thumb != null) {
          qualitySliderTxt.setLayoutX(
              thumb.getLayoutX()
                  + thumb.getWidth() / 2
                  - qualitySliderTxt.getLayoutBounds().getWidth() / 2
                  + START_X_SLIDER
          );
        }
      }
    };

    qualitySldr.setLayoutY(40);
    qualitySldr.setPrefWidth(SLIDER_WIDTH);
    qualitySldr.setShowTickLabels(true);
    qualitySldr.setShowTickMarks(true);


    requestQualityLbl.textProperty().set("Qualität");
    requestQualityLbl.getStyleClass().add("sliderLbls");

    qualitySliderTxt.setTextOrigin(VPos.TOP);
    qualitySliderTxt.textProperty().bind(qualitySldr.valueProperty().asString("%,.0f"));

    requestBitSettingsLbl.textProperty().set("8-Bit-Farben");
    requestBitSettingsLbl.setId("requestBitSettingsLbl");

    requestViewOnlyLbl.textProperty().set("View only");
    requestViewOnlyLbl.setId("requestViewOnlyLbl");

    requestBitCurrentSettingsLbl.textProperty().set("Ihre momentane Einstellung ist");
    requestBitCurrentSettingsLbl.setId("requestBitCurrentSettingsLbl");

    compressionSliderPane.getChildren().addAll(compressionSldr, compressionSliderTxt);
    qualitySliderPane.getChildren().addAll(qualitySldr, qualitySliderTxt);


    requestSettingsBox.getChildren().add(new VBox(compressionSliderPane, requestCompressionLbl));
    requestSettingsBox.getChildren().add(new VBox(qualitySliderPane, requestQualityLbl));
    requestSettingsBox.getChildren().add(new HBox(eightBitTgl, requestBitSettingsLbl));
    requestSettingsBox.getChildren().add(new HBox(viewOnlyTgl, requestViewOnlyLbl));
    requestSettingsBox.getChildren().add(requestBitCurrentSettingsLbl);

    qualitySldr.setLayoutX(START_X_SLIDER);
    compressionSldr.setLayoutX(START_X_SLIDER);
    requestSettingsBox.setPrefWidth(OVERLAY_WIDTH);
    requestSettingsBox.setPrefHeight(OVERLAY_HEIGHT);


    requestSettingsPane.getChildren().add(requestSettingsBox);

    // Help popover - request
    requestHelpLbl.textProperty().set("The remote support tool allows you to get help " +
        "or help someone in need");
    requestHelpLbl.setId("requestHelpLbl");

    // TODO: If we have more labels, we can add it to the box.
    requestHelpBox.getChildren().addAll(requestHelpLbl);

    // Settings PopOver - supporter

    pictureScalingSldr = new Slider();
    pictureScalingSldr.setId("pictureScalingSldr");

    // TODO: Check what we can really use in the settings.
    // TODO: SA, please let UM know which settings we need.
    showOnlyToggl.textProperty().set("Show only");

    supporterSettingsBox.getChildren().addAll(pictureScalingSldr, showOnlyToggl);

    // Help popover - supporter
    supporterHelpLbl.textProperty().set("Here you can add the ID you received from your partner.");
    supporterHelpLbl.setId("supporterHelpLbl");

    // TODO: If we have more labels, we can add it to the box.
    supporterHelpBox.getChildren().addAll(supporterHelpLbl);

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
    viewParent.nameActiveViewProperty().addListener((observable, oldValue, newValue)
        -> changingView(newValue));
  }

  private void changingView(String newValue) {
    switch (newValue) {
      case "home":
        helpPopOver.setContentNode(homeHelpBox);
        settingsPopOver.setContentNode(null);
        break;
      case "requestHelp":
        helpPopOver.setContentNode(requestHelpBox);
        settingsPopOver.setContentNode(requestSettingsPane);
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

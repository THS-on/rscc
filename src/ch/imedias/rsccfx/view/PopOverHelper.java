package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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

  private final double overlayHeight = primaryScreenBounds.getHeight() / 4;
  private final double overlayWidth = primaryScreenBounds.getWidth() / 9;

  private final double sliderWidth = overlayWidth / 1.2;
  private final double startXSlider = (overlayWidth / 2) - (sliderWidth / 2);

  RsccApp rsccApp = new RsccApp();

  private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);

  private final Rscc model;
  private ViewController viewParent;

  ToggleSwitch eightBitTgl = new ToggleSwitch();
  ToggleSwitch viewOnlyTgl = new ToggleSwitch();

  VBox homeHelpBox = new VBox();
  VBox supportSettingsBox = new VBox();
  VBox requestHelpBox = new VBox();
  VBox requestSettingsBox = new VBox();
  VBox supporterHelpBox = new VBox();

  PopOver settingsPopOver = new PopOver();
  PopOver helpPopOver = new PopOver();

  Text compressionSliderTxt = new Text();
  Text qualitySliderTxt = new Text();

  Label requestCompressionLbl = new Label();
  Label requestQualityLbl = new Label();
  Label requestBgr233Lbl = new Label();
  Label requestBitCurrentSettingsLbl = new Label();
  Label requestViewOnlyLbl = new Label();
  Label homeHelpLbl = new Label();
  Label requestHelpLbl = new Label();
  Label supporterHelpLbl = new Label();
  Slider compressionSldr;

  Slider qualitySldr;

  Pane compressionSliderPane = new Pane();
  Pane requestSettingsPane = new Pane();
  Pane qualitySliderPane = new Pane();

  // TODO: 8 bit Toggle is according to SA not needed anymore.

  /**
   * Initializes PopOver according to view.
   */
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
    homeHelpLbl.textProperty().set("Diese Applikation erlaubt Ihnen, "
        + "jemandem zu helfen oder Hilfe zu bekommen");
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
                  + startXSlider
          );
        }
      }
    };

    compressionSldr.setLayoutY(40);
    compressionSldr.setPrefWidth(sliderWidth);
    compressionSldr.setShowTickLabels(true);
    compressionSldr.setShowTickMarks(true);

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
                  + startXSlider
          );
        }
      }
    };

    qualitySldr.setLayoutY(40);
    qualitySldr.setPrefWidth(sliderWidth);
    qualitySldr.setShowTickLabels(true);
    qualitySldr.setShowTickMarks(true);


    requestQualityLbl.textProperty().set("Qualität");
    requestQualityLbl.getStyleClass().add("sliderLbls");

    qualitySliderTxt.setTextOrigin(VPos.TOP);
    qualitySliderTxt.textProperty().bind(qualitySldr.valueProperty().asString("%,.0f"));

    requestBgr233Lbl.textProperty().set("bgr233");
    requestBgr233Lbl.setId("requestBgr233Lbl");

    requestViewOnlyLbl.textProperty().set("View only");
    requestViewOnlyLbl.setId("requestViewOnlyLbl");

    requestBitCurrentSettingsLbl.textProperty().set("Ihre momentane Einstellung ist");
    requestBitCurrentSettingsLbl.setId("requestBitCurrentSettingsLbl");

    compressionSliderPane.getChildren().addAll(compressionSldr, compressionSliderTxt);
    qualitySliderPane.getChildren().addAll(qualitySldr, qualitySliderTxt);

    supportSettingsBox.setPadding(new Insets(10));

    supportSettingsBox.getChildren().add(new VBox(compressionSliderPane, requestCompressionLbl));
    supportSettingsBox.getChildren().add(new VBox(qualitySliderPane, requestQualityLbl));
    supportSettingsBox.getChildren().add(new HBox(eightBitTgl, requestBgr233Lbl));
    supportSettingsBox.getChildren().add(new HBox(requestViewOnlyLbl));
    supportSettingsBox.getChildren().add(requestBitCurrentSettingsLbl);

    qualitySldr.setLayoutX(startXSlider);
    compressionSldr.setLayoutX(startXSlider);
    supportSettingsBox.setPrefWidth(overlayWidth);
    supportSettingsBox.setPrefHeight(overlayHeight);


    requestSettingsPane.getChildren().add(requestSettingsBox);

    // Help popover - request
    requestHelpLbl.textProperty().set("The remote support tool allows you to get help "
        + "or help someone in need");
    requestHelpLbl.setId("requestHelpLbl");

    // TODO: If we have more labels, we can add it to the box.
    requestHelpBox.getChildren().addAll(requestHelpLbl);

    // Settings PopOver - supporter


    // TODO: Check what we can really use in the settings.
    // TODO: SA, please let UM know which settings we need.


    requestSettingsBox.getChildren().addAll(viewOnlyTgl,requestViewOnlyLbl);

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
        settingsPopOver.setContentNode(supportSettingsBox);
        break;
      default:
        helpPopOver.setContentNode(null);
        settingsPopOver.setContentNode(null);
        break;
    }

  }

}

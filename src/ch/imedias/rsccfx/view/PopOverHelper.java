package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.view.util.TextSlider;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

/**
 * Shows popover for settings and help buttons.
 */
public class PopOverHelper {
  // Get Screensize
  Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

  private static final Logger LOGGER =
      Logger.getLogger(PopOverHelper.class.getName());

  private static final int COMPRESSION_MIN = 0;
  private static final int COMPRESSION_MAX = 9;
  private static final int COMPRESSION_VALUE = 6;

  private static final int QUALITY_MIN = 0;
  private static final int QUALITY_MAX = 9;
  private static final int QUALITY_VALUE = 6;

  private final double overlayHeight = primaryScreenBounds.getHeight() / 4;
  private final double overlayWidth = primaryScreenBounds.getWidth() / 9;

  private final double sliderWidth = overlayWidth / 1.2;

  private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);

  private final Rscc model;

  ToggleSwitch eightBitTgl = new ToggleSwitch();
  ToggleSwitch viewOnlyTgl = new ToggleSwitch();

  VBox homeHelpBox = new VBox();
  VBox supportSettingsBox = new VBox();
  VBox requestHelpBox = new VBox();
  VBox requestSettingsBox = new VBox();
  VBox supporterHelpBox = new VBox();

  PopOver settingsPopOver = new PopOver();
  PopOver helpPopOver = new PopOver();

  Label requestCompressionLbl = new Label();
  Label requestQualityLbl = new Label();
  Label requestBgr233Lbl = new Label();
  Label requestBitCurrentSettingsLbl = new Label();
  Label requestViewOnlyLbl = new Label();
  Label homeHelpLbl = new Label();
  Label requestHelpLbl = new Label();
  Label supporterHelpLbl = new Label();

  TextSlider compressionSldr;
  TextSlider qualitySldr;

  Pane requestSettingsPane = new Pane();

  // TODO: 8 bit Toggle is according to SA not needed anymore.

  /**
   * Initializes PopOver according to view.
   */
  public PopOverHelper(Rscc model, String viewName) {
    this.model = model;
    layoutPopOver();
    switch (viewName) {
      case RsccApp.HOME_VIEW:
        layoutHome();
        helpPopOver.setContentNode(homeHelpBox);
        settingsPopOver.setContentNode(null);
        break;
      case RsccApp.REQUEST_VIEW:
        layoutRequest();
        helpPopOver.setContentNode(requestHelpBox);
        settingsPopOver.setContentNode(requestSettingsPane);
        break;
      case RsccApp.SUPPORT_VIEW:
        layoutSupport();
        helpPopOver.setContentNode(supporterHelpBox);
        settingsPopOver.setContentNode(supportSettingsBox);
        break;
      default:
        LOGGER.info("PopOver couldn't find view: " + viewName);
    }
  }

  private void layoutPopOver() {
    //setup layout (aka setup specific pane etc.)
    settingsPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
    settingsPopOver.setDetachable(false);

    helpPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
    helpPopOver.setDetachable(false);
  }

  private void layoutHome() {
    // Settings
    // none

    // Help
    homeHelpLbl.textProperty().set("Diese Applikation erlaubt Ihnen, "
        + "jemandem zu helfen oder Hilfe zu bekommen");
    homeHelpLbl.setId("homeHelpLbl");

    homeHelpBox.getChildren().add(new HBox(homeHelpLbl));
  }

  private void layoutRequest() {
    // Settings
    viewOnlyTgl.getStyleClass().add("toggles");

    requestViewOnlyLbl.textProperty().set("View only");
    requestViewOnlyLbl.setId("requestViewOnlyLbl");

    requestSettingsBox.getChildren().addAll(viewOnlyTgl,requestViewOnlyLbl);
    requestSettingsPane.getChildren().add(requestSettingsBox);

    // Help
    requestHelpLbl.textProperty().set("The remote support tool allows you to get help "
        + "or help someone in need");
    requestHelpLbl.setId("requestHelpLbl");

    requestHelpBox.getChildren().addAll(requestHelpLbl);
  }

  private void layoutSupport() {
    // Settings
    // TODO: SA, please let UM know which settings we need.
    compressionSldr = new TextSlider(COMPRESSION_MIN,COMPRESSION_MAX,COMPRESSION_VALUE);
    compressionSldr.setPrefWidth(sliderWidth);

    requestCompressionLbl.textProperty().set("Kompression");
    requestCompressionLbl.getStyleClass().add("sliderLbls");


    qualitySldr = new TextSlider(QUALITY_MIN,QUALITY_MAX,QUALITY_VALUE);
    qualitySldr.setPrefWidth(sliderWidth);

    requestQualityLbl.textProperty().set("Qualität");
    requestQualityLbl.getStyleClass().add("sliderLbls");


    eightBitTgl.getStyleClass().add("toggles");

    requestBgr233Lbl.textProperty().set("bgr233");
    requestBgr233Lbl.setId("requestBgr233Lbl");


    requestBitCurrentSettingsLbl.textProperty().set("Ihre momentane Einstellung ist");
    requestBitCurrentSettingsLbl.setId("requestBitCurrentSettingsLbl");

    // TODO: why set pref width for only support settings box?
    supportSettingsBox.setPrefWidth(overlayWidth);
    supportSettingsBox.setPrefHeight(overlayHeight);
    supportSettingsBox.setPadding(new Insets(10));
    supportSettingsBox.getChildren().add(new VBox(compressionSldr, requestCompressionLbl));
    supportSettingsBox.getChildren().add(new VBox(qualitySldr, requestQualityLbl));
    supportSettingsBox.getChildren().add(new HBox(eightBitTgl, requestBgr233Lbl));
    supportSettingsBox.getChildren().add(new HBox(requestViewOnlyLbl)); // TODO: why just a label here and in request?
    supportSettingsBox.getChildren().add(requestBitCurrentSettingsLbl);


    // Help
    supporterHelpLbl.textProperty().set("Here you can add the ID you received from your partner.");
    supporterHelpLbl.setId("supporterHelpLbl");

    supporterHelpBox.getChildren().addAll(supporterHelpLbl);
  }

}

package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.view.util.TextSlider;

import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

  private final Strings strings = new Strings();

  //SettingsProperties
  BooleanProperty isViewOnly = new SimpleBooleanProperty(false);

  private static final int COMPRESSION_MIN = 0;
  private static final int COMPRESSION_MAX = 9;
  private static final int COMPRESSION_VALUE = 6;

  private static final int QUALITY_MIN = 0;
  private static final int QUALITY_MAX = 9;
  private static final int QUALITY_VALUE = 6;

  private final double overlayWidth = primaryScreenBounds.getWidth() / 9;

  private final double sliderWidth = overlayWidth / 1.2;

  private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);

  private final Rscc model;

  ToggleSwitch supportBgr233Tgl = new ToggleSwitch();
  ToggleSwitch requestViewOnlyTgl = new ToggleSwitch();

  VBox homeHelpBox = new VBox();

  VBox supportSettingsBox = new VBox();
  VBox supportHelpBox = new VBox();

  VBox requestHelpBox = new VBox();
  VBox requestSettingsBox = new VBox();

  VBox supportCompressionSliderBox = new VBox();
  VBox supportQualitySliderBox = new VBox();

  HBox supportBgr233ToggleBox = new HBox();

  PopOver settingsPopOver = new PopOver();
  PopOver helpPopOver = new PopOver();

  Label supportCompressionLbl = new Label();
  Label supportQualityLbl = new Label();
  Label supportBgr233Lbl = new Label();
  Label requestViewOnlyLbl = new Label();

  Label homeHelpLbl = new Label();

  Label requestHelpLbl = new Label();
  Label supportHelpLbl = new Label();

  TextSlider supportCompressionSldr;
  TextSlider supportQualitySldr;

  Button expertSettingsBtn = new Button();

  /**
   * Initializes PopOver according to view.
   */
  public PopOverHelper(Rscc model, String viewName) {
    this.model = model;
    initFieldData();
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
        settingsPopOver.setContentNode(requestSettingsBox);
        requestValueChangeListener();
        handleRequestSettings();
        requestSettingsBindings();
        invokeExpertSettings();
        break;
      case RsccApp.SUPPORT_VIEW:
        layoutSupport();
        helpPopOver.setContentNode(supportHelpBox);
        settingsPopOver.setContentNode(supportSettingsBox);
        supportSettingsBindings();
        invokeExpertSettings();
        break;
      default:
        LOGGER.info("PopOver couldn't find view: " + viewName);
    }
  }

  private void initFieldData() {
    homeHelpLbl.textProperty().set(strings.homeHelpLbl);
    requestHelpLbl.textProperty().set(strings.requestHelpLbl);
    supportHelpLbl.textProperty().set(strings.supportHelpLbl);

    requestViewOnlyLbl.textProperty().set(strings.requestViewOnlyLbl);

    supportCompressionLbl.textProperty().set(strings.supportCompressionLbl);
    supportQualityLbl.textProperty().set(strings.supportQualityLbl);
    supportBgr233Lbl.textProperty().set(strings.supportBgr233Lbl);

    expertSettingsBtn.textProperty().set(strings.expertSettingsBtn);
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
    homeHelpLbl.setId("homeHelpLbl");

    homeHelpBox.getChildren().add(new HBox(homeHelpLbl));
  }

  private void layoutRequest() {
    // Settings
    requestViewOnlyTgl.getStyleClass().add("toggles");

    requestViewOnlyLbl.setId("requestViewOnlyLbl");

    requestSettingsBox.getChildren().addAll(requestViewOnlyTgl, requestViewOnlyLbl,
        expertSettingsBtn);

    // Help
    requestHelpLbl.setId("requestHelpLbl");

    requestHelpBox.getChildren().addAll(requestHelpLbl);
  }

  private void layoutSupport() {
    // Settings
    supportCompressionSldr = new TextSlider(COMPRESSION_MIN, COMPRESSION_MAX, COMPRESSION_VALUE);
    supportCompressionSldr.setPrefWidth(sliderWidth);
    supportCompressionSldr.getStyleClass().add("slider");

    supportSettingsBox.setAlignment(Pos.CENTER);

    supportCompressionLbl.getStyleClass().add("sliderLbls");

    supportQualitySldr = new TextSlider(QUALITY_MIN, QUALITY_MAX, QUALITY_VALUE);
    supportQualitySldr.setPrefWidth(sliderWidth);

    supportQualityLbl.getStyleClass().add("sliderLbls");

    supportBgr233Tgl.getStyleClass().add("toggles");

    supportBgr233Lbl.setId("supportBgr233Lbl");

    supportCompressionSliderBox.getChildren().addAll(supportCompressionSldr, supportCompressionLbl);
    supportQualitySliderBox.getChildren().addAll(supportQualitySldr, supportQualityLbl);
    supportBgr233ToggleBox.getChildren().addAll(supportBgr233Tgl, supportBgr233Lbl);

    supportBgr233ToggleBox.setAlignment(Pos.CENTER);
    supportCompressionSliderBox.setAlignment(Pos.CENTER);
    supportQualitySliderBox.setAlignment(Pos.CENTER);

    supportSettingsBox.setSpacing(10);

    supportSettingsBox.getChildren().add(supportCompressionSliderBox);
    supportSettingsBox.getChildren().add(supportQualitySliderBox);
    supportSettingsBox.getChildren().add(supportBgr233ToggleBox);
    supportSettingsBox.getChildren().add(expertSettingsBtn);

    // Help
    supportHelpLbl.setId("supportHelpLbl");

    supportHelpBox.getChildren().addAll(supportHelpLbl);
  }

  private void requestValueChangeListener() {
    requestViewOnlyTgl.selectedProperty().addListener(observable -> { });
  }


  private void requestSettingsBindings() {
    model.vncOptionViewOnlyProperty().bindBidirectional(requestViewOnlyTgl.selectedProperty());
  }

  private void supportSettingsBindings() {
    model.vncOptionQualitySliderValueProperty().bindBidirectional(supportQualitySldr
        .sliderValueProperty());
  }

  /**
   * Kills the VncServer if settings Popover is showing.
   * Starts the VncServer after popover is closed.
   */
  private void handleRequestSettings() {
    settingsPopOver.showingProperty().addListener((observableValue, aBoolean, t1) -> {
      if (t1) {
        model.stopVncServer();
      } else {
        model.startVncServer();
      }
    });
  }

  private void invokeExpertSettings() {
    expertSettingsBtn.setOnAction(actionEvent -> new ExpertSettingsDialog());
  }

}



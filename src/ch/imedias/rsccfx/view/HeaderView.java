package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Defines all elements shown in the header.
 */
public class HeaderView extends HBox {
  private static final Logger LOGGER =
      Logger.getLogger(Rscc.class.getName());

  private static final double HEADER_HEIGHT = 250d;
  private static final double ICON_SIZE = 20;

  final Pane spacer = new Pane();
  final Button backBtn = new Button();
  final Button helpBtn = new Button();
  final Button settingsBtn = new Button();
  private final Strings strings = new Strings();
  private final Rscc model;

  /**
   * Initializes all the GUI components needed in the Header.
   *
   * @param model the model to handle the data.
   */
  public HeaderView(Rscc model) {
    this.model = model;
    SvgImageLoaderFactory.install();
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data
    FontAwesomeIconView questionIcon = new FontAwesomeIconView(FontAwesomeIcon.QUESTION);
    questionIcon.setGlyphSize(ICON_SIZE);
    helpBtn.setGraphic(questionIcon);

    FontAwesomeIconView settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.GEAR);
    settingsIcon.setGlyphSize(ICON_SIZE);
    settingsBtn.setGraphic(settingsIcon);

    FontAwesomeIconView backIcon = new FontAwesomeIconView(FontAwesomeIcon.CARET_LEFT);
    backIcon.setGlyphSize(ICON_SIZE);
    backBtn.setGraphic(backIcon);
  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    HBox.setHgrow(spacer, Priority.ALWAYS);

    this.getChildren().addAll(backBtn, spacer, settingsBtn, helpBtn);
    this.setId("header");

    backBtn.setId("backBtn");

    helpBtn.setId("helpBtn");

    settingsBtn.setId("settingsBtn");
  }

  private void bindFieldsToModel() {
    // make bindings to the model
  }


}


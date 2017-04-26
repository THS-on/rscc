package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Defines all elements shown in the request section.
 */
public class RsccRequestView extends BorderPane {
  private static final Logger LOGGER =
      Logger.getLogger(RsccRequestView.class.getName());

  private static final double BUTTON_SIZE = 50d;
  private static final double GENERATEDKEYFLD_HEIGHT = 60d;

  final HeaderView headerView;
  private final Rscc model;
  private final Strings strings = new Strings();

  final Label keyGenerationLbl = new Label();
  final Label supporterAdminLbl = new Label();
  final Label descriptionLbl = new Label();

  final VBox descriptionBox = new VBox();
  final VBox bottomBox = new VBox();

  final HBox supporterAdminBox = new HBox();
  final HBox centerBox = new HBox();
  final HBox keyGeneratingBox = new HBox();

  final TitledPane keyGeneratorPane = new TitledPane();
  final TitledPane supporterAdminPane = new TitledPane();

  final TextField generatedKeyFld = new TextField();

  final Button reloadKeyBtn = new Button();
  final Button readyBtn = new Button();
  final Button supporterOneBtn = new Button();

  Image reloadImg;

  ImageView reloadImgView;

  /**
   * Initializes all the GUI components needed generate the key the supporter needs.
   *
   * @param model the model to handle the data.
   */
  public RsccRequestView(Rscc model) {
    this.model = model;
    headerView = new HeaderView(model);
    SvgImageLoaderFactory.install();
    initFieldData();
    layoutForm();
    bindFieldsToModel();
  }

  private void initFieldData() {
    // populate fields which require initial data

    keyGenerationLbl.textProperty().set("Generate key");
    keyGenerationLbl.setId("keyGenerationLbl");

    descriptionLbl.textProperty().set("Send this code to your supporter and click ready. "
        + "Once your supporter enters this code, the remote support will start.");
    descriptionLbl.setId("descriptionLbl"); // TODO: Styling

    generatedKeyFld.setPrefHeight(GENERATEDKEYFLD_HEIGHT); // FIXME: Has this to be in the CSS?
    generatedKeyFld.setEditable(false); // FIXME: Has this to be in the CSS?
    generatedKeyFld.setId("generatedKeyFld");

    InputStream reloadImagePath = getClass().getClassLoader()
        .getResourceAsStream("images/reload.svg");
    reloadImg = new Image(reloadImagePath);
    reloadImgView = new ImageView(reloadImg);
    reloadImgView.fitWidthProperty().set(BUTTON_SIZE); // FIXME: Has this to be in the CSS?
    reloadImgView.fitHeightProperty().set(BUTTON_SIZE); // FIXME: Has this to be in the CSS?
    reloadImgView.setPreserveRatio(true);
    reloadKeyBtn.setGraphic(reloadImgView);
    reloadKeyBtn.setPrefWidth(BUTTON_SIZE); // FIXME: Has this to be in the CSS?
    reloadKeyBtn.setPrefHeight(BUTTON_SIZE); // FIXME: Has this to be in the CSS?

    readyBtn.textProperty().set("Ready");

    keyGeneratorPane.setText("Key generator");
    keyGeneratorPane.setExpanded(true);

    supporterAdminLbl.textProperty().set("Addressbook");
    supporterAdminPane.setText("Addressbook");
    //supporterAdminPane.setPrefHeight(25d);
    supporterAdminPane.setExpanded(false);
    supporterAdminPane.setId("supporterAdminPane");

    // TODO: Finish all the buttons here according to mockup.
    // Admin Buttons
    // label, six Buttons, six images
    /*supporterOneBtn.setGraphic();*/
    supporterOneBtn.textProperty().setValue("Supporter 1");
    supporterOneBtn.getStyleClass().add("supporterBtn");
    /*supporterTwoBtn.setGraphic();*/
    // two HBox'es

  }

  private void layoutForm() {
    //setup layout (aka setup specific pane etc.)
    centerBox.setId("centerBox");
    bottomBox.setId("bottomBox");

    supporterAdminBox.getChildren().addAll(supporterAdminLbl);
    keyGeneratingBox.getChildren().addAll(generatedKeyFld, reloadKeyBtn);
    keyGeneratingBox.setId("keyGeneratingBox");

    readyBtn.setId("readyBtn");
    reloadKeyBtn.setId("reloadKeyBtn");

    descriptionBox.getChildren().addAll(keyGenerationLbl, descriptionLbl, readyBtn);

    centerBox.getChildren().addAll(keyGeneratingBox, descriptionBox);
    bottomBox.getChildren().add(supporterAdminBox);

    keyGeneratorPane.setContent(centerBox);
    supporterAdminPane.setContent(bottomBox);

    setTop(headerView);
    setCenter(keyGeneratorPane);
    setBottom(supporterAdminPane);
  }

  private void bindFieldsToModel() {
    // make bindings to the model
    generatedKeyFld.textProperty().bind(model.keyProperty());
  }
}


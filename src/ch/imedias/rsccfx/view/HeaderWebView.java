package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.localization.Strings;
import ch.imedias.rsccfx.model.Rscc;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.geometry.Insets;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Class that created the help View
 */
public class HeaderWebView extends VBox {

  String wikiUrl="https://wiki.lernstick.ch";

  public HeaderWebView() {
    createWebHelp();
  }

  public void createWebHelp() {

    ProgressBar progressBar = new ProgressBar();

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    // A Worker load the page
    Worker<Void> worker = webEngine.getLoadWorker();

    // Bind the progress property of ProgressBar
    // with progress property of Worker
    progressBar.progressProperty().bind(worker.progressProperty());
    progressBar.setPrefWidth(1000);

    this.getChildren().addAll(browser, progressBar);

    String url1 = getClass().getClassLoader().getResource("helpPage.html").toExternalForm();

    //If local Help Page should be shown
    webEngine.load(url1);

    //If external Wiki should be shown
    //webEngine.load(wikiUrl);
  }
}

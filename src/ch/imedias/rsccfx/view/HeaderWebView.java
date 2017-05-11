package ch.imedias.rsccfx.view;

import javafx.concurrent.Worker;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Class that created the help View
 */
public class HeaderWebView extends VBox {

  final String wikiUrl="https://wiki.lernstick.ch";
  final ProgressBar progressBar = new ProgressBar();
  final WebView browser = new WebView();
  final WebEngine webEngine = browser.getEngine();
  final Worker<Void> worker = webEngine.getLoadWorker();

  public HeaderWebView() {
    createWebHelp();
  }

  public void createWebHelp() {

    // Bind the progress property of ProgressBar
    // with progress property of Worker
    progressBar.progressProperty().bind(worker.progressProperty());
    progressBar.setPrefWidth(1000);

    this.getChildren().addAll(browser, progressBar);

    String url1 = getClass().getClassLoader().getResource("helpPage.html").toExternalForm();

    //If local Help Page should be shown
    //webEngine.load(url1);

    //If external Wiki should be shown
    webEngine.load(wikiUrl);
  }
}


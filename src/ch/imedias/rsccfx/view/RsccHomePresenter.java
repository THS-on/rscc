package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

// TODO: add header
// TODO: put buttons in a VBox
// TODO: Check mockup for reference here:
// https://www.cs.technik.fhnw.ch/confluence16/display/VTDESGB/Mockups+-+Remote+Support+-+Version+0.8?preview=/15991708/15991716/Startscreen.png
// TODO: Put all things into the box


/**
 * Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class RsccHomePresenter implements ControlledPresenter {
  private static final int VIEW_BTN_DIVISOR = 2;
  private static final int IMG_VIEW_DIVISOR = 8;

  private final Rscc model;
  private final RsccHomeView view;
  HeaderPresenter headerPresenter;
  private ViewController viewParent;

  /**
   * Initializes a new RsccHomePresenter with the matching view.
   */
  public RsccHomePresenter(Rscc model, RsccHomeView view) {
    this.model = model;
    this.view = view;
    headerPresenter = new HeaderPresenter(model, view.headerView);
    attachEvents();
    initHeader();
  }

  /**
   * Defines the ViewController to allow changing of views.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  /**
   * Initializes the size of the RsccHomeView.
   */
  public void initSize(Scene scene) {
    // view.supportViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(VIEW_BTN_DIVISOR));
    // view.supportViewBtn.prefHeightProperty().bind(scene.heightProperty());
    // view.requestViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(VIEW_BTN_DIVISOR));
    // view.requestViewBtn.prefHeightProperty().bind(scene.heightProperty());
    headerPresenter.initSize(scene);

    view.requestImgView.fitWidthProperty().bind(scene.widthProperty().divide(IMG_VIEW_DIVISOR));
    view.supportImgView.fitWidthProperty().bind(scene.widthProperty().divide(IMG_VIEW_DIVISOR));

    view.supportViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(2));
    view.supportViewBtn.prefHeightProperty().bind(scene.heightProperty()
        .subtract(view.headerView.heightProperty()).divide(2.5));

    view.requestViewBtn.prefWidthProperty().bind(scene.widthProperty().divide(2));
    view.requestViewBtn.prefHeightProperty().bind(scene.heightProperty()
        .subtract(view.headerView.heightProperty()).divide(2.5));

    view.mainView.setAlignment(Pos.CENTER);
    view.mainView.setPadding(new Insets(25));
    view.mainView.setSpacing(20);
  }

  private void attachEvents() {
    view.supportViewBtn.setOnAction(event -> viewParent.setView(RsccApp.SUPPORT_VIEW));
    view.requestViewBtn.setOnAction(event -> viewParent.setView(RsccApp.REQUEST_VIEW));
  }

  private void initHeader() {
    // set all the actions regarding buttons in this method
    headerPresenter.setVisibilityOfBackBtn(false);
  }
}

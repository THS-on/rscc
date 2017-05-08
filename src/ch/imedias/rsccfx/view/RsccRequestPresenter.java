package ch.imedias.rsccfx.view;

import static ch.imedias.rscc.RemoteSupportFrame.getDefaultList;

import ch.imedias.rscc.SupportAddress;
import ch.imedias.rsccfx.ControlledPresenter;
import ch.imedias.rsccfx.RsccApp;
import ch.imedias.rsccfx.ViewController;
import ch.imedias.rsccfx.model.Rscc;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.scene.Scene;

/**
 * Defines the behaviour of interactions
 * and initializes the size of the GUI components.
 */
public class RsccRequestPresenter implements ControlledPresenter {
  private static final Logger LOGGER =
      Logger.getLogger(RsccRequestPresenter.class.getName());
  private static final double WIDTH_SUBTRACTION_GENERAL = 50d;
  private static final double WIDTH_SUBTRACTION_KEYFIELD = 100d;

  private final Rscc model;
  private final RsccRequestView view;
  private final HeaderPresenter headerPresenter;
  private ViewController viewParent;

  /**
   * List of supporter stuff here.
   */
  private final static String SUPPORT_ADDRESSES = "supportAddresses";
  private final static String COMPRESSION_LEVEL = "compressionLevel";
  private final static String QUALITY = "quality";
  private List<SupportAddress> supportAddresses;
  private final Preferences preferences = Preferences.userNodeForPackage(RsccApp.class);

  private void supporterListStuff() {
    // load preferences
    String supportAddressesXML = preferences.get(SUPPORT_ADDRESSES, null);
    if (supportAddressesXML == null) {
      // use some hardcoded defaults
      supportAddresses = getDefaultList();
    } else {
      byte[] array = supportAddressesXML.getBytes();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
      XMLDecoder decoder = new XMLDecoder(inputStream);
      supportAddresses = (List<SupportAddress>) decoder.readObject();
    }
    for (int counter = 0; counter < supportAddresses.size(); counter++) {
      view.predefinedAddressesLbl.textProperty().set(supportAddresses.get(counter).getAddress() + "\n" +
          supportAddresses.get(counter).getDescription());
    }
//    compressionSpinnerModel.setValue(
//        preferences.getInt(COMPRESSION_LEVEL, 6));
//    qualitySpinnerModel.setValue(preferences.getInt(QUALITY, 6));
  }

  private void exit() {
    // save preferences
    ByteArrayOutputStream byteArrayOutputStream =
        new ByteArrayOutputStream();
    XMLEncoder encoder = new XMLEncoder(byteArrayOutputStream);
    encoder.setPersistenceDelegate(SupportAddress.class,
        SupportAddress.getPersistenceDelegate());
    encoder.writeObject(supportAddresses);
    encoder.close();
    String supportAddressesXML = byteArrayOutputStream.toString();
    preferences.put(SUPPORT_ADDRESSES, supportAddressesXML);
//    preferences.putInt(COMPRESSION_LEVEL,
//            compressionSpinnerModel.getNumber().intValue());
//    preferences.putInt(QUALITY,
//            qualitySpinnerModel.getNumber().intValue());
  }
  /**
   * End of supporter stuff.
   */


  /**
   * Initializes a new RsccRequestPresenter with the matching view.
   *
   * @param model model with all data.
   * @param view the view belonging to the presenter.
   */
  public RsccRequestPresenter(Rscc model, RsccRequestView view) {
    this.model = model;
    this.view = view;
    headerPresenter = new HeaderPresenter(model, view.headerView);
    attachEvents();
    initHeader();
    supporterListStuff();
  }

  /**
   * Defines the ViewController to allow changing views.
   */
  public void setViewParent(ViewController viewParent) {
    this.viewParent = viewParent;
  }

  private void attachEvents() {
    view.reloadKeyBtn.setOnAction(
        event -> model.refreshKey()
    );

    // Closes the other TitledPane so that just one TitledPane is shown on the screen.
    view.keyGeneratorPane.setOnMouseClicked(
        event -> view.predefinedAddressesPane.setExpanded(false)
    );
    view.predefinedAddressesPane.setOnMouseClicked(
        event -> view.keyGeneratorPane.setExpanded(false)
    );
  }

  /**
   * Initializes the size of the whole RsccRequestView elements.
   *
   * @param scene must be initialized and displayed before calling this method;
   *              The size of all header elements are based on it.
   * @throws NullPointerException if called before this object is fully initialized.
   */
  public void initSize(Scene scene) {
    // initialize header
    headerPresenter.initSize(scene);

    // initialize view
    // TODO: requestHelpView --> generatedKeyFld should not take the whole width!
    view.generatedKeyFld.prefWidthProperty().bind(scene.widthProperty()
        .subtract(WIDTH_SUBTRACTION_KEYFIELD));
    view.descriptionLbl.prefWidthProperty().bind(scene.widthProperty()
        .subtract(WIDTH_SUBTRACTION_GENERAL));
    view.keyGeneratorPane.prefWidthProperty().bind(scene.widthProperty());

    // FIXME: need the height of the titlePane itself...
    view.centerBox.prefHeightProperty().bind(scene.heightProperty()
        .subtract(159d));

  }

  /**
   * Initializes the functionality of the header, e.g. back button and settings button.
   */
  private void initHeader() {
    // Set all the actions regarding buttons in this method.
    headerPresenter.setBackBtnAction(event -> {
      model.killConnection();
      exit();
      viewParent.setView(RsccApp.HOME_VIEW);
    });
  }
}

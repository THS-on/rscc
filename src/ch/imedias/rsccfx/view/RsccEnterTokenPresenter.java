package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RsccEnterTokenPresenter {

  private final Rscc model;
  private final RsccEnterTokenView view;

  /**
   * Javadoc comment here.
   */
  public RsccEnterTokenPresenter(Rscc model, RsccEnterTokenView view) {
    this.model = model;
    this.view = view;
    attachEvents();
  }

  private void attachEvents() {
    //TODO put all setOnAction/addListeners in here
  }

  public String validationImage(String token) {

    if (validateToken(token)) {
      return getClass().getClassLoader().getResource("sample.jpeg").toExternalForm();
    } else {
      return getClass().getClassLoader().getResource("sample.jpeg").toExternalForm();
    }
  }

  private static boolean validateToken(String token) {
    return (int) (Math.random() * 2) == 1;
  }
}

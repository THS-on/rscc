package ch.imedias.rsccfx.view.util;

import javafx.geometry.VPos;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Created by user on 08.05.17.
 */
public class TextSlider extends Slider{

  private Pane sliderPane = new Pane();

  Text sliderTxt = new Text();

  public TextSlider(int min, int max, int value) {
    super(min, max, value);
  }

  @Override
  protected void layoutChildren() {
    super.layoutChildren();

    Region thumb = (Region) lookup(".thumb");
    if (thumb != null) {
      sliderTxt.setLayoutX(
          thumb.getLayoutX()
              + thumb.getWidth() / 2
              - sliderTxt.getLayoutBounds().getWidth() / 2
      );
    }

    sliderTxt.setTextOrigin(VPos.TOP);
    sliderTxt.textProperty().bind(this.valueProperty().asString("%,.0f"));
    sliderTxt.getStyleClass().add("sliderTxts");
    this.setLayoutY(40);

  }

}

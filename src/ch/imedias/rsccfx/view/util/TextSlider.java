package ch.imedias.rsccfx.view.util;

import javafx.geometry.VPos;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Created by user on 08.05.17.
 */
public class TextSlider extends Pane {

  Slider slider;
  Text valueText = new Text();

  public TextSlider(int min, int max, int value) {
    slider = new Slider(min, max, value) {
      @Override
      protected void layoutChildren() {
        super.layoutChildren();

        Region thumb = (Region) lookup(".thumb");
        if (thumb != null) {
          valueText.setLayoutX(
              thumb.getLayoutX()
                  + thumb.getWidth() / 2
                  - valueText.getLayoutBounds().getWidth() / 2
          );
        }
      }
    };

    slider.setLayoutY(40);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);

    valueText.setTextOrigin(VPos.TOP);
    valueText.textProperty().bind(
        slider.valueProperty().asString("%,.0f"));
    valueText.getStyleClass().add("sliderTxts");

    getChildren().addAll(valueText, slider);
  }

}

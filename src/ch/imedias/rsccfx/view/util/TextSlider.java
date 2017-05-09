package ch.imedias.rsccfx.view.util;

import javafx.geometry.VPos;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Represents a custom slider which has the value on the top of the thumb at all times.
 */
public class TextSlider extends Pane {

  Slider slider;
  Text valueText = new Text();
  private final double startXSlider;
  private final double sliderWidth;

  /**
   * Initializes a new slider with the value as a text on top of the thumb.
   * @param min minimum value of the slider.
   * @param max maximum value of the slider.
   * @param value standard value of the slider.
   */
  public TextSlider(int min, int max, int value) {
    sliderWidth = this.getWidth() / 1.2;
    startXSlider = (this.getWidth() / 2) - (sliderWidth / 2);
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
                  + startXSlider
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

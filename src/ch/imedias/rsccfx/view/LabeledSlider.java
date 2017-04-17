package ch.imedias.rsccfx.view;

/**
 * Created by user on 17.04.17.
 */
import javafx.application.Application;
    import javafx.geometry.*;
    import javafx.scene.Scene;
    import javafx.scene.control.Slider;
    import javafx.scene.layout.*;
    import javafx.scene.text.Text;
    import javafx.stage.Stage;

public class LabeledSlider extends Application {
  @Override
  public void start(Stage stage) {
    Text text = new Text();
    text.setTextOrigin(VPos.TOP);
    Slider slider = new Slider(7, 7_700, 4_200) {
      @Override
      protected void layoutChildren() {
        super.layoutChildren();

        Region thumb = (Region) lookup(".thumb");
        if (thumb != null) {
          text.setLayoutX(
              thumb.getLayoutX()
                  + thumb.getWidth() / 2
                  - text.getLayoutBounds().getWidth() / 2
          );
        }
      }
    };
    slider.setLayoutY(20);
    text.textProperty().bind(slider.valueProperty().asString("%,.0f"));

    Pane sliderPane = new Pane(slider, text);
    slider.prefWidthProperty().bind(sliderPane.widthProperty());
    sliderPane.setPrefWidth(200);

    StackPane layout = new StackPane(sliderPane);
    layout.setPadding(new Insets(10));

    stage.setScene(new Scene(layout));
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
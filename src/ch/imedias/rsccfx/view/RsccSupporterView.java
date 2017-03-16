package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.model.Rscc;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


/**
 *
 */
public class RsccSupporterView extends BorderPane implements View {
    private final Rscc model;

    private HeaderView headerView;
    protected HeaderPresenter headerPresenter;

    private TitledPane adminSupporterPane;
    private TitledPane mainPane;

    protected Label enterTokenLbl;
    private Label loremIpsumLbl;        // TODO: Rename it...
    private Label exampleLbl;
    private Label instructionLbl;

    protected VBox topBox;
    private VBox centerBox;
    private VBox groupingBox;
    private HBox tokenValidationBox;

    protected TextField tokenTxt;
    protected ImageView isValidImg;

    protected Button connectBtn;
    private Button expandOptionBtn;     // TODO: Double check if private access is ok.



    /**
     * Initializes all the GUI components needed to enter the token the supporter received.
     *
     * @param model defines what is displayed.
     */
    public RsccSupporterView(Rscc model) {
      this.model = model;
      initFieldData();
      layoutForm();
      bindFieldsToModel();
    }

    private void layoutForm() {
      // TODO: import CSS accordingly. Ask SA where it needs to be defined.
      // this.setPadding(new Insets(5, 25, 5, 25));
      this.setId("SupporterView");

      enterTokenLbl.setFont(new Font(25)); // TODO: Move this to CSS.

      loremIpsumLbl.setWrapText(true);

      tokenValidationBox = new HBox();
      tokenTxt = new TextField();
      tokenTxt.setFont(new Font(30));

      isValidImg.setSmooth(true);
      tokenValidationBox.getChildren().addAll(tokenTxt, isValidImg);
      tokenValidationBox.setSpacing(5);
      tokenValidationBox.setHgrow(tokenTxt, Priority.ALWAYS);
      tokenValidationBox.setAlignment(Pos.CENTER_LEFT);

      groupingBox = new VBox();
      groupingBox.getChildren().addAll(tokenValidationBox, instructionLbl);

      centerBox.getChildren().addAll(enterTokenLbl,
          loremIpsumLbl,
          exampleLbl,
          groupingBox,
          connectBtn,
          expandOptionBtn);

      connectBtn.setFont(new Font(30));
    }

    private void initFieldData() {

      adminSupporterPane = new TitledPane();
      mainPane = new TitledPane();

      headerView = new HeaderView(model);
      headerPresenter = new HeaderPresenter(model, headerView);
      topBox = new VBox();
      centerBox = new VBox();

      isValidImg = new ImageView(getClass()
              .getClassLoader()
              .getResource("dialog-error.png")
              .toExternalForm());
      enterTokenLbl = new Label("EnterToken");
      loremIpsumLbl = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
      exampleLbl = new Label("Number of characters: 8\nexample: 666xx666");
      instructionLbl = new Label("Instructions");
      connectBtn = new Button("Connect");
      expandOptionBtn = new Button("More");

      setCenter(centerBox);
      topBox.getChildren().add(headerView);
      setTop(topBox);
    }

    private void bindFieldsToModel() {
      //make the bindings to the model

    }

  }


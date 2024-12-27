package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.configuration.MutationVariant;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.configuration.WorldMapVariant;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import java.util.function.UnaryOperator;

public class ConfigurationPresenter {

  private Configuration configuration;
  @FXML
  private GridPane mainGridPane;

  //  Map configuration elements
  @FXML
  private TextField heightInput;
  @FXML
  private TextField widthInput;

  //  Animal configuration elements
  @FXML
  private MenuButton mutationVariantMenuButton;
  @FXML
  private MenuItem littleCorrectionVariant;
  @FXML
  private MenuItem fullRandomVariant;
  @FXML
  private TextField startEnergyInput;
  @FXML
  private TextField minMutationCountInput;
  @FXML
  private TextField maxMutationCountInput;
  @FXML
  private TextField genomeLengthInput;

  //  Simulation configuration elements
  @FXML
  private MenuButton plantVariantMenuButton;
  @FXML
  private MenuItem forestEquatorsVariant;
  @FXML
  private TextField energyGainInput;
  @FXML
  private TextField plantGrowthInput;
  @FXML
  private TextField wellFedEnergyInput;
  @FXML
  private TextField lossCopulateEnergyInput;
  @FXML
  private TextField fireFrequencyInput;
  @FXML
  private TextField startPlantCountInput;
  @FXML
  private TextField startAnimalCountInput;

  public void initialize() {
    setTextFormatters();

    littleCorrectionVariant.setOnAction(event -> chooseMutationVariant(MutationVariant.LITTLE_CORRECTION));
    fullRandomVariant.setOnAction(event -> chooseMutationVariant(MutationVariant.FULL_RANDOM));
    forestEquatorsVariant.setOnAction(event -> choosePlantVariant(PlantVariant.FORESTED_EQUATORS));
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
    chooseMutationVariant(MutationVariant.FULL_RANDOM);
    choosePlantVariant(PlantVariant.FORESTED_EQUATORS);

  }

  public void switchWorldMapVariant(WorldMapVariant newVariant) {
    switch (newVariant) {
      case WorldMapVariant.GRASS_FIELD -> setGrassFieldModeVisibility();
      case WorldMapVariant.EARTH -> setEarthFieldModeVisibility();
      case WorldMapVariant.FIRE -> setFireEarthFieldModeVisibility();
    }
  }

  public void setGrassFieldModeVisibility() {
    disableNumericTextField(heightInput);
    disableNumericTextField(widthInput);
    disableNumericTextField(plantGrowthInput);
    disableNumericTextField(fireFrequencyInput);
  }

  public void setEarthFieldModeVisibility() {
    enableNumericTextField(heightInput);
    enableNumericTextField(widthInput);
    enableNumericTextField(plantGrowthInput);
    disableNumericTextField(fireFrequencyInput);
  }

  public void setFireEarthFieldModeVisibility() {
    setEarthFieldModeVisibility();
    enableNumericTextField(fireFrequencyInput);
  }

  private void chooseMutationVariant(MutationVariant variant) {
    mutationVariantMenuButton.setText(variant.getDisplayText());
    configuration.getAnimalConfiguration().setMutationVariant(variant);
  }

  private void choosePlantVariant(PlantVariant variant) {
    plantVariantMenuButton.setText(variant.getDisplayText());
    configuration.getSimulationConfiguration().setPlantVariant(variant);
  }

  private void disableNumericTextField(TextField textField) {
    textField.setDisable(true);
    textField.setText("0");
  }

  private void enableNumericTextField(TextField textField) {
    textField.setDisable(false);
  }

  private void setTextFormatters() {
    mainGridPane.getChildren().stream()
        .filter(node -> node instanceof TextField)
        .map(node -> (TextField)node)
        .forEach(textField -> textField.setTextFormatter(createTextFormatter()));
  }

  private TextFormatter<String> createTextFormatter() {
    UnaryOperator<TextFormatter.Change> filter = change -> {
      String text = change.getText();
      if (text.matches("[0-9]*")) {
        if (change.getControlNewText().isEmpty() ||
            change.getControlNewText().equals("0") ||
            !change.getControlNewText().startsWith("0")) {
          return change;
        }
      }
      return null;
    };

    return new TextFormatter<>(filter);
  }

}

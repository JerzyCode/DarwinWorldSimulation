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
  @FXML
  private TextField startAnimalCountInput;

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

  public void initialize() {
    setTextFieldValidation();
    littleCorrectionVariant.setOnAction(event -> chooseMutationVariant(MutationVariant.LITTLE_CORRECTION));
    fullRandomVariant.setOnAction(event -> chooseMutationVariant(MutationVariant.FULL_RANDOM));
    forestEquatorsVariant.setOnAction(event -> choosePlantVariant(PlantVariant.FORESTED_EQUATORS));
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
    chooseMutationVariant(MutationVariant.FULL_RANDOM);
    choosePlantVariant(PlantVariant.FORESTED_EQUATORS);
    setInputValues(configuration);

  }

  public void switchWorldMapVariant(WorldMapVariant newVariant) {
    switch (newVariant) {
      case WorldMapVariant.GRASS_FIELD -> setGrassFieldModeVisibility();
      case WorldMapVariant.EARTH -> setEarthFieldModeVisibility();
      case WorldMapVariant.FIRE -> setFireEarthFieldModeVisibility();
    }
  }

  public void setGrassFieldModeVisibility() {
    heightInput.setDisable(true);
    heightInput.setText("5");

    widthInput.setDisable(true);
    widthInput.setText("5");

    disableNumericInputWithZeroValue(plantGrowthInput);
    disableNumericInputWithZeroValue(fireFrequencyInput);
  }

  public void setEarthFieldModeVisibility() {
    enableNumericTextField(heightInput);
    enableNumericTextField(widthInput);
    enableNumericTextField(plantGrowthInput);
    disableNumericInputWithZeroValue(fireFrequencyInput);
  }

  public void setFireEarthFieldModeVisibility() {
    setEarthFieldModeVisibility();
    enableNumericTextField(fireFrequencyInput);
  }

  public void updateConfiguration() {
    updateMapConfiguration();
    updateAnimalConfiguration();
    updateSimulationConfiguration();
  }

  private void chooseMutationVariant(MutationVariant variant) {
    mutationVariantMenuButton.setText(variant.getDisplayText());
    configuration.getAnimalConfiguration().setMutationVariant(variant);
  }

  private void choosePlantVariant(PlantVariant variant) {
    plantVariantMenuButton.setText(variant.getDisplayText());
    configuration.getSimulationConfiguration().setPlantVariant(variant);
  }

  private void disableNumericInputWithZeroValue(TextField textField) {
    textField.setDisable(true);
    textField.setText("0");
  }

  private void enableNumericTextField(TextField textField) {
    textField.setDisable(false);
  }

  private void setTextFieldValidation() {
    mainGridPane.getChildren().stream()
        .filter(node -> node instanceof TextField)
        .map(node -> (TextField)node)
        .forEach(textField -> {
          textField.setTextFormatter(createTextFormatter());
          textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
              if (textField.getText().isEmpty()) {
                textField.setText("0");
              }
            }
          });
        });
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

  private void updateMapConfiguration() {
    var height = Integer.parseInt(heightInput.getText());
    var width = Integer.parseInt(widthInput.getText());
    var startPlantCount = Integer.parseInt(startPlantCountInput.getText());

    var mapConfiguration = configuration.getWorldMapConfiguration();
    mapConfiguration.setHeight(height);
    mapConfiguration.setWidth(width);
    mapConfiguration.setStartPlantCount(startPlantCount);

  }

  private void updateAnimalConfiguration() {
    var startEnergy = Integer.parseInt(startEnergyInput.getText());
    var minMutationCount = Integer.parseInt(minMutationCountInput.getText());
    var maxMutationCount = Integer.parseInt(maxMutationCountInput.getText());
    var genomeLength = Integer.parseInt(genomeLengthInput.getText());

    var animalConfiguration = configuration.getAnimalConfiguration();
    animalConfiguration.setStartEnergy(startEnergy);
    animalConfiguration.setMinimumMutationCount(minMutationCount);
    animalConfiguration.setMaximumMutationCount(maxMutationCount);
    animalConfiguration.setGenomeLength(genomeLength);
  }

  private void updateSimulationConfiguration() {
    System.out.println("updateSimulationConfiguration()");
    var energyGain = Integer.parseInt(energyGainInput.getText());
    var plantGrowth = Integer.parseInt(plantGrowthInput.getText());
    var wellFedEnergy = Integer.parseInt(wellFedEnergyInput.getText());
    var lossCopulateEnergy = Integer.parseInt(lossCopulateEnergyInput.getText());
    var startAnimalCount = Integer.parseInt(startAnimalCountInput.getText());
    var fireFrequency = Integer.parseInt(fireFrequencyInput.getText());

    var simulationConfiguration = configuration.getSimulationConfiguration();
    simulationConfiguration.setEnergyGain(energyGain);
    simulationConfiguration.setPlantGrowth(plantGrowth);
    simulationConfiguration.setWellFedEnergy(wellFedEnergy);
    simulationConfiguration.setLossCopulateEnergy(lossCopulateEnergy);
    simulationConfiguration.setFireFrequency(fireFrequency);
    simulationConfiguration.setStartAnimalCount(startAnimalCount);
  }

  private void setInputValues(Configuration configuration) {
    var mapConfiguration = configuration.getWorldMapConfiguration();
    heightInput.setText(String.valueOf(mapConfiguration.getHeight()));
    widthInput.setText(String.valueOf(mapConfiguration.getWidth()));
    startPlantCountInput.setText(String.valueOf(mapConfiguration.getStartPlantCount()));

    var animalConfiguration = configuration.getAnimalConfiguration();
    startEnergyInput.setText(String.valueOf(animalConfiguration.getStartEnergy()));
    minMutationCountInput.setText(String.valueOf(animalConfiguration.getMinimumMutationCount()));
    maxMutationCountInput.setText(String.valueOf(animalConfiguration.getMaximumMutationCount()));
    genomeLengthInput.setText(String.valueOf(animalConfiguration.getGenomeLength()));

    var simulationConfiguration = configuration.getSimulationConfiguration();
    energyGainInput.setText(String.valueOf(simulationConfiguration.getEnergyGain()));
    plantGrowthInput.setText(String.valueOf(simulationConfiguration.getPlantGrowth()));
    wellFedEnergyInput.setText(String.valueOf(simulationConfiguration.getWellFedEnergy()));
    lossCopulateEnergyInput.setText(String.valueOf(simulationConfiguration.getLossCopulateEnergy()));
    fireFrequencyInput.setText(String.valueOf(simulationConfiguration.getFireFrequency()));
    startAnimalCountInput.setText(String.valueOf(simulationConfiguration.getStartAnimalCount()));
  }
}

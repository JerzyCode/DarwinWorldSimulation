package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.*;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import java.util.function.UnaryOperator;

public class ConfigurationPresenter {

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
    private MenuItem noneVariant;
    @FXML
    private TextField daysCountInput;
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
    private TextField fireDurationInput;
    @FXML
    private TextField startPlantCountInput;

    private WorldMapVariant mapVariant;
    private PlantVariant plantVariant;
    private MutationVariant mutationVariant;

    public void initialize() {
        setTextFieldValidation();
        littleCorrectionVariant.setOnAction(event -> chooseMutationVariant(MutationVariant.LITTLE_CORRECTION));
        fullRandomVariant.setOnAction(event -> chooseMutationVariant(MutationVariant.FULL_RANDOM));
        forestEquatorsVariant.setOnAction(event -> choosePlantVariant(PlantVariant.FORESTED_EQUATORS));
    }

    public void setConfiguration(Configuration configuration) {
        chooseMutationVariant(MutationVariant.FULL_RANDOM);
        choosePlantVariant(PlantVariant.FORESTED_EQUATORS);
        setInputValues(configuration);
    }

    public void switchWorldMapVariant(WorldMapVariant newVariant) {
        this.mapVariant = newVariant;
        switch (newVariant) {
            case WorldMapVariant.EARTH -> setEarthFieldModeVisibility();
            case WorldMapVariant.FIRE -> setFireEarthFieldModeVisibility();
        }
    }

    public void setEarthFieldModeVisibility() {
        enableNumericTextField(heightInput);
        enableNumericTextField(widthInput);
        enableNumericTextField(plantGrowthInput);
        disableNumericInputWithZeroValue(fireFrequencyInput);
        disableNumericInputWithZeroValue(fireDurationInput);
    }

    public void setFireEarthFieldModeVisibility() {
        setEarthFieldModeVisibility();
        enableNumericTextField(fireFrequencyInput);
        enableNumericTextField(fireDurationInput);
    }

    public Configuration createConfiguration() {
        var animalConfiguration = AnimalConfiguration.builder()
                .startEnergy(Integer.parseInt(startEnergyInput.getText()))
                .minimumMutationCount(Integer.parseInt(minMutationCountInput.getText()))
                .mutationVariant(mutationVariant)
                .wellFedEnergy(Integer.parseInt(wellFedEnergyInput.getText()))
                .lossCopulateEnergy(Integer.parseInt(lossCopulateEnergyInput.getText()))
                .maximumMutationCount(Integer.parseInt(maxMutationCountInput.getText()))
                .genomeLength(Integer.parseInt(genomeLengthInput.getText()))
                .build();

        var simulationConfiguration = SimulationConfiguration.builder()
                .daysCount(Integer.parseInt(daysCountInput.getText()))
                .startAnimalCount(Integer.parseInt(startAnimalCountInput.getText()))
                .build();

        var worldMapConfiguration = WorldMapConfiguration.builder()
                .width(Integer.parseInt(widthInput.getText()))
                .height(Integer.parseInt(heightInput.getText()))
                .energyGain(Integer.parseInt(energyGainInput.getText()))
                .plantGrowth(Integer.parseInt(plantGrowthInput.getText()))
                .fireFrequency(Integer.parseInt(fireFrequencyInput.getText()))
                .fireDuration(Integer.parseInt(fireDurationInput.getText()))
                .startPlantCount(Integer.parseInt(startPlantCountInput.getText()))
                .mapVariant(mapVariant)
                .plantVariant(plantVariant)
                .build();

        return Configuration.builder()
                .worldMapConfiguration(worldMapConfiguration)
                .animalConfiguration(animalConfiguration)
                .simulationConfiguration(simulationConfiguration)
                .build();
    }

    private void chooseMutationVariant(MutationVariant variant) {
        mutationVariantMenuButton.setText(variant.getDisplayText());
        this.mutationVariant = variant;
    }

    private void choosePlantVariant(PlantVariant variant) {
        plantVariantMenuButton.setText(variant.getDisplayText());
        this.plantVariant = variant;
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
                .map(node -> (TextField) node)
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

    private void setInputValues(Configuration configuration) {
        var mapConfiguration = configuration.getWorldMapConfiguration();
        heightInput.setText(String.valueOf(mapConfiguration.getHeight()));
        widthInput.setText(String.valueOf(mapConfiguration.getWidth()));
        energyGainInput.setText(String.valueOf(mapConfiguration.getEnergyGain()));
        plantGrowthInput.setText(String.valueOf(mapConfiguration.getPlantGrowth()));
        startPlantCountInput.setText(String.valueOf(mapConfiguration.getStartPlantCount()));
        fireFrequencyInput.setText(String.valueOf(mapConfiguration.getFireFrequency()));
        fireDurationInput.setText(String.valueOf(mapConfiguration.getFireDuration()));


        var animalConfiguration = configuration.getAnimalConfiguration();
        startEnergyInput.setText(String.valueOf(animalConfiguration.getStartEnergy()));
        minMutationCountInput.setText(String.valueOf(animalConfiguration.getMinimumMutationCount()));
        maxMutationCountInput.setText(String.valueOf(animalConfiguration.getMaximumMutationCount()));
        wellFedEnergyInput.setText(String.valueOf(animalConfiguration.getWellFedEnergy()));
        lossCopulateEnergyInput.setText(String.valueOf(animalConfiguration.getLossCopulateEnergy()));
        genomeLengthInput.setText(String.valueOf(animalConfiguration.getGenomeLength()));

        var simulationConfiguration = configuration.getSimulationConfiguration();
        daysCountInput.setText(String.valueOf(simulationConfiguration.getDaysCount()));
        startAnimalCountInput.setText(String.valueOf(simulationConfiguration.getStartAnimalCount()));
    }
}

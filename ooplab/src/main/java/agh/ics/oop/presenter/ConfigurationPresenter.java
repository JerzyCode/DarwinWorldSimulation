package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.exceptions.LoadConfigurationException;
import agh.ics.oop.model.exceptions.SaveFailedException;
import agh.ics.oop.model.repository.ConfigurationRepositoryPort;
import agh.ics.oop.model.repository.JsonConfigurationRepositoryAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.UnaryOperator;

public class ConfigurationPresenter {

    @FXML
    private GridPane mainGridPane;
    @FXML
    private ScrollPane savedScrollPane;
    @FXML
    private VBox configurationListContainer;
    @FXML
    private Button saveConfigurationButton;
    @FXML
    private TextField saveNameInput;
    @FXML
    private Label errorLabel;

    //  Map configuration elements
    @FXML
    private MenuButton menuButton;
    @FXML
    private MenuItem earthItem;
    @FXML
    private MenuItem fireEarthItem;
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
    private final ConfigurationRepositoryPort configurationRepositoryPort = new JsonConfigurationRepositoryAdapter();

    @FXML
    public void initialize() {
        setTextFieldValidation();
        littleCorrectionVariant.setOnAction(event -> chooseMutationVariant(MutationVariant.LITTLE_CORRECTION));
        fullRandomVariant.setOnAction(event -> chooseMutationVariant(MutationVariant.FULL_RANDOM));
        forestEquatorsVariant.setOnAction(event -> choosePlantVariant(PlantVariant.FORESTED_EQUATORS));
        earthItem.setOnAction(event -> chooseMap(WorldMapVariant.EARTH));
        fireEarthItem.setOnAction(event -> chooseMap(WorldMapVariant.FIRE));
        configurationRepositoryPort.getSaveNames().forEach(this::addLoadSavedConfigurationButton);
        savedScrollPane.setFitToWidth(true);
        saveNameInput.textProperty().addListener((observable, oldValue, newValue) -> clearError());
        errorLabel.setMaxWidth(200);

    }

    public void setConfiguration(Configuration configuration) {
        setInputValues(configuration);
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

    private void switchWorldMapVariant(WorldMapVariant newVariant) {
        this.mapVariant = newVariant;
        switch (newVariant) {
            case WorldMapVariant.EARTH -> setEarthFieldModeVisibility();
            case WorldMapVariant.FIRE -> setFireEarthFieldModeVisibility();
        }
    }

    private void defaultInputsVisibility() {
        enableNumericTextField(heightInput);
        enableNumericTextField(widthInput);
        enableNumericTextField(plantGrowthInput);
    }

    private void setEarthFieldModeVisibility() {
        defaultInputsVisibility();
        disableNumericInputWithZeroValue(fireFrequencyInput);
        disableNumericInputWithZeroValue(fireDurationInput);
    }

    private void setFireEarthFieldModeVisibility() {
        defaultInputsVisibility();
        enableNumericTextField(fireFrequencyInput);
        enableNumericTextField(fireDurationInput);
    }

    private void chooseMap(WorldMapVariant mapVariant) {
        menuButton.setText(mapVariant.getDisplayText());
        switchWorldMapVariant(mapVariant);
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

        chooseMap(configuration.getWorldMapConfiguration().getMapVariant());
        chooseMutationVariant(configuration.getAnimalConfiguration().getMutationVariant());
        choosePlantVariant(configuration.getWorldMapConfiguration().getPlantVariant());
    }


    public void onSaveConfiguration() {
        var inputName = saveNameInput.getText();
        try {
            configurationRepositoryPort.save(createConfiguration(), inputName);
            addLoadSavedConfigurationButton(inputName);
        } catch (SaveFailedException e) {
            System.out.println(e.getMessage());
            displayError(e.getMessage());
        }
    }

    private void loadSavedConfiguration(String savedName) {
        try {
            var configuration = configurationRepositoryPort.loadConfiguration(savedName);
            setInputValues(configuration);
        } catch (LoadConfigurationException e) {
            System.out.println(e.getMessage());
            displayError(e.getMessage());
        }
    }

    private void addLoadSavedConfigurationButton(String saveName) {
        Button button = new Button(saveName);
        button.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(button, Priority.ALWAYS);
        HBox.setHgrow(button, Priority.ALWAYS);
        configurationListContainer.getChildren().add(button);
        button.setOnAction(event -> loadSavedConfiguration(saveName));
    }

    private void displayError(String message) {
        errorLabel.setText(message);
    }

    private void clearError() {
        errorLabel.setText("");
    }
}

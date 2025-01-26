package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationContext;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.configuration.ConfigurationValidator;
import agh.ics.oop.model.exceptions.WrongConfigurationParameterException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainPanelPresenter {
    private static final String CONFIGURATION_VIEW_RESOURCE = "/configuration_view.fxml";
    @FXML
    private StackPane contentContainer;
    @FXML
    private Label errorLabel;

    private ConfigurationPresenter configurationPresenter;
    private final SimulationEngine simulationEngine = new SimulationEngine();
    private final ConfigurationValidator validator = new ConfigurationValidator();

    @FXML
    public void initialize() throws IOException {
        loadConfigurationViews();
    }


    @FXML
    public void openSimulationWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        try {
            var configuration = configurationPresenter.createConfiguration();
            validator.validate(configuration);
            System.out.println("Starting simulation with configuration: " + configuration);

            SimulationPresenter presenter = loader.getController();
            createSimulation(configuration, presenter);

            hideErrorMessage();
            configureStage(stage, viewRoot);
            stage.setOnCloseRequest(event -> presenter.endSimulation());
            stage.show();

        } catch (WrongConfigurationParameterException e) {
            showErrorMessage(e.getMessage());
        }
    }

    public void onCloseThreadPool() {
        simulationEngine.closeThreadPool();
    }

    private void loadConfigurationViews() throws IOException {
        var configurationViewLoader = new FXMLLoader(getClass().getResource(CONFIGURATION_VIEW_RESOURCE));
        AnchorPane configurationView = configurationViewLoader.load();
        configurationPresenter = configurationViewLoader.getController();
        configurationPresenter.setConfiguration(Configuration.getDefaultConfig());
        contentContainer.getChildren().add(configurationView);
    }

    private void createSimulation(Configuration configuration, SimulationPresenter presenter) {
        var simulationContext = new SimulationContext(configuration);
        var simulation = new Simulation(simulationContext, configuration.getSimulationConfiguration().getDaysCount());
        presenter.setConfiguration(configuration);
        presenter.setSimulation(simulation);
        var simulationId = simulationEngine.addSimulation(simulation);
        simulationEngine.runAsyncInThreadPool(simulationId);
    }


    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    private void showErrorMessage(String message) {
        errorLabel.setText(message);
    }

    private void hideErrorMessage() {
        errorLabel.setText("");
    }


}

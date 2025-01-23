package agh.ics.oop.presenter;

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
    private static final double LEFT_PANEL_PREF_WIDTH = 160;
    private static final int GRID_WIDTH = 15;
    private static final String CONFIGURATION_VIEW_RESOURCE = "/configuration_view.fxml";
    @FXML
    private StackPane contentContainer;
    @FXML
    private Label errorLabel;

    private ConfigurationPresenter configurationPresenter;

    private final ConfigurationValidator validator = new ConfigurationValidator();

    public void initialize() throws IOException {
        loadConfigurationViews();
    }

    private void loadConfigurationViews() throws IOException {
        var configurationViewLoader = new FXMLLoader(getClass().getResource(CONFIGURATION_VIEW_RESOURCE));
        AnchorPane configurationView = configurationViewLoader.load();
        configurationPresenter = configurationViewLoader.getController();
        configurationPresenter.setConfiguration(Configuration.getDefaultConfig());
        contentContainer.getChildren().add(configurationView);
    }


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
            presenter.setConfiguration(configuration);

            hideErrorMessage();
            configureStage(stage, viewRoot);
            stage.setOnCloseRequest(event -> presenter.stopSimulation());
            adjustWindowSize(stage, configuration.getWorldMapConfiguration().getWidth(), configuration.getWorldMapConfiguration().getHeight());
            stage.show();

        } catch (WrongConfigurationParameterException e) {
            System.out.println(e.getMessage());
            showErrorMessage(e.getMessage());
        }

    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    private void adjustWindowSize(Stage stage, int simulationWidth, int simulationHeight) {
        var adjustedWith = Math.min(1200, LEFT_PANEL_PREF_WIDTH + simulationWidth * GRID_WIDTH);
        var adjustedHeight = Math.min(800, simulationHeight * GRID_WIDTH);

        stage.setWidth(adjustedWith);
        stage.setHeight(adjustedHeight);
    }

    private void showErrorMessage(String message) {
        errorLabel.setText(message);
    }

    private void hideErrorMessage() {
        errorLabel.setText("");
    }


}

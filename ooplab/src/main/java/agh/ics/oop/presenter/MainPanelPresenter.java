package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button startSimulationButton;

    private ConfigurationPresenter configurationPresenter;

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

        var configuration = configurationPresenter.createConfiguration();
        System.out.println("Starting simulation with configuration: " + configuration);

        SimulationPresenter presenter = loader.getController();
        presenter.setConfiguration(configuration);

        configureStage(stage, viewRoot);
        stage.setOnCloseRequest(event -> presenter.stopSimulation());
        stage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}

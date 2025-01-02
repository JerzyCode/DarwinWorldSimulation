package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.configuration.WorldMapVariant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
  @FXML
  private MenuButton menuButton;
  @FXML
  private MenuItem grassFieldItem;
  @FXML
  private MenuItem earthItem;
  @FXML
  private MenuItem fireEarthItem;

  private ConfigurationPresenter configurationPresenter;

  private final Configuration configuration = new Configuration();

  public void initialize() throws IOException {
    loadConfigurationViews();
    chooseMap(WorldMapVariant.EARTH);
//    grassFieldItem.setOnAction(event -> chooseMap(WorldMapVariant.GRASS_FIELD));
    earthItem.setOnAction(event -> chooseMap(WorldMapVariant.EARTH));
    fireEarthItem.setOnAction(event -> chooseMap(WorldMapVariant.FIRE));

  }

  private void loadConfigurationViews() throws IOException {
    var configurationViewLoader = new FXMLLoader(getClass().getResource(CONFIGURATION_VIEW_RESOURCE));
    AnchorPane configurationView = configurationViewLoader.load();
    configurationPresenter = configurationViewLoader.getController();
    configurationPresenter.setConfiguration(configuration);

    contentContainer.getChildren().add(configurationView);
  }

  private void chooseMap(WorldMapVariant mapVariant) {
    menuButton.setText(mapVariant.getDisplayText());
    configuration.getWorldMapConfiguration().setMapVariant(mapVariant);
    configurationPresenter.switchWorldMapVariant(mapVariant);
  }

  public void startSimulation() throws IOException {
    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
    BorderPane viewRoot = loader.load();

    configurationPresenter.updateConfiguration();
    System.out.println("Starting simulation with configuration: " + configuration);

    SimulationPresenter presenter = loader.getController();
    presenter.setConfiguration(configuration);

    configureStage(stage, viewRoot);
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

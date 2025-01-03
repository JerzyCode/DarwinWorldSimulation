package agh.ics.oop.presenter;

import agh.ics.oop.model.GrassField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainPanelPresenter {
  @FXML
  private Button startSimulationButton;

  public void startSimulation() throws IOException {
    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
    BorderPane viewRoot = loader.load();

    var map = new GrassField(10);
//    var map = new RectangularMap(20,20);
    SimulationPresenter presenter = loader.getController();
    map.addListener(presenter);
    map.addListener((worldMap, message) -> System.out.println(message));
    presenter.setWorldMap(map);
    presenter.drawMap();

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

package agh.ics.oop;

import agh.ics.oop.model.GrassField;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SimulationApp extends Application {
  @Override
  public void start(Stage stage) throws IOException {

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
    BorderPane viewRoot = loader.load();

    var map = new GrassField(10);
    SimulationPresenter presenter = loader.getController();
    map.addListener(presenter);
    presenter.setWorldMap(map);
    presenter.drawMap();

    configureStage(stage, viewRoot);
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
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

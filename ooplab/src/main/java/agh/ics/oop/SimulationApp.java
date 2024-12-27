package agh.ics.oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("main_panel.fxml"));
    Pane viewRoot = loader.load();
    configureStage(stage, viewRoot);
    stage.show();
  }

  private void configureStage(Stage primaryStage, Pane viewRoot) {
    var scene = new Scene(viewRoot);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Simulation app");
  }
}

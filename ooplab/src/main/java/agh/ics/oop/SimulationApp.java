package agh.ics.oop;

import agh.ics.oop.presenter.MainPanelPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main_panel.fxml"));
        Pane viewRoot = loader.load();
        MainPanelPresenter mainPanelPresenter = loader.getController();
        configureStage(stage, viewRoot);
        stage.setOnCloseRequest(event -> mainPanelPresenter.onCloseThreadPool());
        stage.show();
    }

    private void configureStage(Stage primaryStage, Pane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Simulation app");
    }
}

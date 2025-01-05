package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.PresenterNoMapToPresentException;
import agh.ics.oop.ui.WorldElementBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationPresenter implements MapChangeListener {

  @FXML
  private GridPane mapGrid;
  @FXML
  private GridPane leftCoordinates;
  @FXML
  private GridPane topCoordinates;
  @FXML
  private TextArea historyTextArea;
  @FXML
  private Button startButton;
  @FXML
  private TextField movesTextField;

  private final Map<WorldElement, WorldElementBox> graphicElements = new HashMap<>();

  private WorldMap worldMap;

  private static final int GRID_WIDTH = 40;
  private static final String COORDINATE_LABEL_CLASS_NAME = "coordinate-label";

  public void drawMap() {
    var mapBoundary = worldMap.getCurrentBounds();
    clearGrid();
    fillCoordinates(mapBoundary);
    fillGrid(mapBoundary);
    drawElements(mapBoundary);
  }

  @Override
  public void mapChanged(WorldMap worldMap, MapChangeEventData data) {
    if (data.getType() == MapChangeEventType.PLACE) {
      this.worldMap.getElements()
          .stream()
          .filter(worldElement -> !graphicElements.containsKey(worldElement))
          .forEach(worldElement -> graphicElements.put(worldElement, new WorldElementBox(worldElement, GRID_WIDTH)));
    }
    Platform.runLater(() -> {
      historyTextArea.appendText(data.getMessage() + "\n");
      this.drawMap();
    });
  }

  public void onSimulationStartClicked() {
    if (worldMap == null) {
      throw new PresenterNoMapToPresentException("Presenter has no map to present!");
    }

    var args = movesTextField.getText().split(" ");
    var directions = OptionsParser.parse(args);
    var animalPositions = List.of(new Vector2d(-2, -2), new Vector2d(2, 2));
    var simulation = new Simulation(animalPositions, directions, worldMap);
    var simulationEngine = new SimulationEngine(List.of(simulation));

    simulationEngine.runAsyncInThreadPool();
    startButton.setDisable(true);
  }

  public void setWorldMap(WorldMap worldMap) {
    this.worldMap = worldMap;
    this.worldMap.getElements().forEach(element -> {
      graphicElements.put(element, new WorldElementBox(element, GRID_WIDTH));
    });
  }

  private void clearGrid() {
    if (mapGrid.getChildren() != null) {
      mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
    }

    if (topCoordinates.getChildren() != null) {
      topCoordinates.getChildren().retainAll(topCoordinates.getChildren().getFirst());
    }

    if (leftCoordinates.getChildren() != null) {
      leftCoordinates.getChildren().retainAll(leftCoordinates.getChildren().getFirst());
    }

    topCoordinates.getColumnConstraints().clear();
    leftCoordinates.getRowConstraints().clear();
    mapGrid.getColumnConstraints().clear();
    mapGrid.getRowConstraints().clear();
  }

  private void fillCoordinates(Boundary mapBoundary) {
    var maxRightX = mapBoundary.rightTopCorner().getX();
    var maxLeftX = mapBoundary.leftBottomCorner().getX();
    var maxTopY = mapBoundary.rightTopCorner().getY();
    var maxBottomY = mapBoundary.leftBottomCorner().getY();

    for (int i = 1; i <= maxRightX + calculateOffsetX(maxLeftX) + 1; i++) {
      topCoordinates.getColumnConstraints().add(new ColumnConstraints(GRID_WIDTH));
      topCoordinates.add(createCoordinateLabel(Integer.toString(maxLeftX + i - 1)), i, 0);
    }

    for (int i = 0; i <= maxTopY + calculateOffsetY(maxBottomY); i++) {
      leftCoordinates.getRowConstraints().add(new RowConstraints(GRID_WIDTH));
      leftCoordinates.add(createCoordinateLabel(Integer.toString(maxBottomY + i)), 0, i);
    }

  }

  private void fillGrid(Boundary mapBoundary) {
    int width = calculateGridWidth(mapBoundary.leftBottomCorner(), mapBoundary.rightTopCorner());
    int height = calculateGridHeight(mapBoundary.leftBottomCorner(), mapBoundary.rightTopCorner());

    for (int i = 0; i <= width; i++) {
      mapGrid.getColumnConstraints().add(new ColumnConstraints(GRID_WIDTH));
    }

    for (int i = 0; i <= height; i++) {
      mapGrid.getRowConstraints().add(new RowConstraints(GRID_WIDTH));
    }

  }

  private void drawElements(Boundary mapBoundary) {
    var maxLeftX = mapBoundary.leftBottomCorner().getX();
    var maxBottomY = mapBoundary.leftBottomCorner().getY();

    worldMap.getElements().forEach(element -> {
      int x = element.getPosition().getX() + calculateOffsetX(maxLeftX);
      int y = element.getPosition().getY() + calculateOffsetY(maxBottomY);
      mapGrid.add(graphicElements.get(element).getContainer(), x, y);
    });
  }

  private Label createCoordinateLabel(String text) {
    Label label = new Label(text);
    label.setPrefWidth(GRID_WIDTH);
    label.setPrefHeight(GRID_WIDTH);
    label.getStyleClass().add(COORDINATE_LABEL_CLASS_NAME);
    return label;
  }

  private int calculateGridWidth(Vector2d leftBot, Vector2d rightTop) {
    return Math.abs(leftBot.subtract(rightTop).getX());
  }

  private int calculateGridHeight(Vector2d leftBot, Vector2d rightTop) {
    return Math.abs(leftBot.subtract(rightTop).getY());
  }

  private int calculateOffsetX(int maxLeftX) {
    if (maxLeftX < 0) {
      return Math.abs(maxLeftX);
    }
    return 0;
  }

  private int calculateOffsetY(int maxBottomY) {
    if (maxBottomY < 0) {
      return Math.abs(maxBottomY);
    }
    return 0;
  }

}

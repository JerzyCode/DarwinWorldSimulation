package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.PresenterNoMapToPresentException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.List;

public class SimulationPresenter implements MapChangeListener {

  @FXML
  public GridPane mapGrid;
  @FXML
  public GridPane leftCoordinates;
  @FXML
  public GridPane topCoordinates;
  @FXML
  private TextArea historyTextArea;
  @FXML
  private Button startButton;
  @FXML
  private TextField movesTextField;

  private WorldMap worldMap;

  private static final int GRID_WIDTH = 40;
  private static final String COORDINATE_LABEL_CLASS_NAME = "coordinate-label";

  public void drawMap() {

    clearGrid();
    fillCoordinates();
    fillGrid();
    drawElements();
  }

  @Override
  public void mapChanged(WorldMap worldMap, String message) {
    historyTextArea.appendText(message + "\n");
    Platform.runLater(this::drawMap);
  }

  public void onSimulationStartClicked() {
    if (worldMap == null) {
      throw new PresenterNoMapToPresentException("Presenter has no map to present!");
    }

    var args = movesTextField.getText().split(" ");
    var directions = OptionsParser.parse(args);
    var animalPositions = List.of(new Vector2d(-5, -5), new Vector2d(5, 5));
    var simulation = new Simulation(animalPositions, directions, worldMap);
    var simulationEngine = new SimulationEngine(List.of(simulation));

    simulationEngine.runAsyncInThreadPool();
  }

  public void setWorldMap(WorldMap worldMap) {
    this.worldMap = worldMap;
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

  private void fillCoordinates() {

    for (int i = 1; i <= maxRightX() + calculateOffsetX() + 1; i++) {
      topCoordinates.getColumnConstraints().add(new ColumnConstraints(GRID_WIDTH));
      topCoordinates.add(createCoordinateLabel(Integer.toString(maxLeftX() + i - 1)), i, 0);
    }

    for (int i = 0; i <= maxTopY() + calculateOffsetY(); i++) {
      leftCoordinates.getRowConstraints().add(new RowConstraints(GRID_WIDTH));
      leftCoordinates.add(createCoordinateLabel(Integer.toString(maxBotY() + i)), 0, i);
    }

  }

  private void fillGrid() {
    int width = calculateGridWidth(worldMap.getCurrentBounds().leftBottomCorner(), worldMap.getCurrentBounds().rightTopCorner());
    int height = calculateGridHeight(worldMap.getCurrentBounds().leftBottomCorner(), worldMap.getCurrentBounds().rightTopCorner());

    for (int i = 0; i <= width; i++) {
      mapGrid.getColumnConstraints().add(new ColumnConstraints(GRID_WIDTH));
    }

    for (int i = 0; i <= height; i++) {
      mapGrid.getRowConstraints().add(new RowConstraints(GRID_WIDTH));
    }

  }

  private void drawElements() {
    worldMap.getElements().forEach(element -> {
      int x = element.getPosition().getX() + calculateOffsetX();
      int y = element.getPosition().getY() + calculateOffsetY();

      Shape rectangle = new Rectangle(GRID_WIDTH, GRID_WIDTH);

      if (element instanceof Animal animal) {
        var animalDrawing = createAnimalDrawing(animal.getOrientation());
        mapGrid.add(animalDrawing, x, y);
      }
      else {
        rectangle.setFill(Color.GREEN);
        mapGrid.add(rectangle, x, y);
      }

    });
  }

  private Pane createAnimalDrawing(MapDirection orientation) {
    Pane pane = new Pane();
    double width = (double)GRID_WIDTH / 2;
    double radius = width / 1.5;
    Circle head = new Circle(width, width, radius);
    head.setFill(Color.LIGHTBLUE);

    Text text = new Text(width, width, orientation.getSymbol());
    text.setFill(Color.BLACK);
    text.setStyle("-fx-font-size: 16; -fx-font-weight: bold");

    pane.getChildren().addAll(head, text);
    return pane;
  }

  private Circle createCircle(MapDirection orientation) {
    // TODO draw nice animal with circle and triangle
    return null;
  }

  private Polygon createTriangle(MapDirection orientation) {
    // TODO draw nice animal with circle and triangle
    return null;
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

  private int calculateOffsetX() {
    if (maxLeftX() < 0) {
      return Math.abs(maxLeftX());
    }
    return 0;
  }

  private int calculateOffsetY() {
    if (maxBotY() < 0) {
      return Math.abs(maxBotY());
    }
    return 0;
  }

  private int maxLeftX() {
    return worldMap.getCurrentBounds().leftBottomCorner().getX();
  }

  private int maxRightX() {
    return worldMap.getCurrentBounds().rightTopCorner().getX();
  }

  private int maxTopY() {
    return worldMap.getCurrentBounds().rightTopCorner().getY();
  }

  private int maxBotY() {
    return worldMap.getCurrentBounds().leftBottomCorner().getY();
  }
}

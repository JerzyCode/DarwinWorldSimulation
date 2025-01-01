package agh.ics.oop.presenter;

import agh.ics.oop.SimulationContext;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.exceptions.PresenterHasNoConfigurationException;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.elements.Animal;
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

  private WorldMap worldMap;
  private Configuration configuration;

  private static final int GRID_WIDTH = 40;
  private static final String COORDINATE_LABEL_CLASS_NAME = "coordinate-label";

  // TODO: obstawiam, że problem jest taki, że synchronizacja jest na elementach mapy, ale gdzies po drodze zacząłeś tworzyć w symulacji zwierzęta i trawy zamiast w mapie
  // TODO: Chyba się gdzieś spójność danych popsuła
  public void drawMap() {
    synchronized (worldMap.getElements()) {
      var mapBoundary = worldMap.getCurrentBounds();
      clearGrid();
      fillCoordinates(mapBoundary);
      fillGrid(mapBoundary);
      drawElements(mapBoundary);
    }
  }

  @Override
  public void mapChanged(WorldMap worldMap, String message) {
    Platform.runLater(() -> {
      historyTextArea.appendText(message + "\n");
      this.drawMap();
    });
  }

  public void onSimulationStartClicked() {
    if (configuration == null) {
      throw new PresenterHasNoConfigurationException("Presenter has no configuration!");
    }

    var simulationContext = new SimulationContext(configuration);
    worldMap = simulationContext.getWorldMap();
    simulationContext.setMapChangeListener(this);
    var simulation = new Simulation(simulationContext, configuration.getSimulationConfiguration().getDaysCount());
    var simulationEngine = new SimulationEngine(simulation);

    simulationEngine.runAsyncInThreadPool();
    startButton.setDisable(true);
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
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

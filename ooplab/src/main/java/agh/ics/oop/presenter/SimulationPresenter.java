package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationContext;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.listener.LoggerListener;
import agh.ics.oop.listener.MapChangeListener;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.event.EventType;
import agh.ics.oop.model.event.MapChangedEvent;
import agh.ics.oop.model.exceptions.PresenterHasNoConfigurationException;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.plant.Earth;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

public class SimulationPresenter implements MapChangeListener {
    private static final int GRID_WIDTH = 15;

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

    //    Stats
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label plantCountLabel;
    @FXML
    private Label freeFieldsLabel;
    @FXML
    private Label popularGenotypeLabel;
    @FXML
    private Label avgEnergyLabel;
    @FXML
    private Label avgLifespanLabel;
    @FXML
    private Label avgChildrenLabel;


    private WorldMap worldMap;
    @Setter
    private Configuration configuration;
    private SimulationEngine simulationEngine;
    private SimulationContext simulationContext;

    public void initialize() {
        System.out.println("initialize()");
        AnchorPane.setTopAnchor(topCoordinates, 0.0);
        AnchorPane.setLeftAnchor(topCoordinates, 0.0);

        AnchorPane.setTopAnchor(leftCoordinates, (double) GRID_WIDTH);
        AnchorPane.setLeftAnchor(leftCoordinates, 0.0);

        AnchorPane.setTopAnchor(mapGrid, (double) GRID_WIDTH);
        AnchorPane.setLeftAnchor(mapGrid, (double) GRID_WIDTH);

        leftCoordinates.setPrefWidth(GRID_WIDTH);
    }

    public void drawMap() {
        synchronized (worldMap.getElements()) {
            var mapBoundary = worldMap.getCurrentBounds();
            clearGrid();
            fillGrid(mapBoundary);
            drawElements();
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, MapChangedEvent event) {
        if (event.getEventType() == EventType.DAY_ENDS) {
            Platform.runLater(() -> {
                updateStatistics();
                this.drawMap();
            });
        } else {
            Platform.runLater(() -> {
                historyTextArea.appendText(event.getMessage() + "\n");
                updateStatistics();
            });
        }
    }

    public void onSimulationStartClicked() {
        if (configuration == null) {
            throw new PresenterHasNoConfigurationException("Presenter has no configuration!");
        }

        simulationContext = new SimulationContext(configuration);
        worldMap = simulationContext.getWorldMap();
        simulationContext.addMapChangedListener(this);
        simulationContext.addMapChangedListener(new LoggerListener());


        var simulation = new Simulation(simulationContext, configuration.getSimulationConfiguration().getDaysCount());
        simulationEngine = new SimulationEngine(simulation);

        simulationEngine.runAsyncInThreadPool();
        startButton.setDisable(true);
    }

    public void stopSimulation() {
        if (simulationEngine != null) {
            simulationEngine.shutDown();
        }
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

    private void drawElements() {
        worldMap.getElements().forEach(element -> {
            int x = element.getPosition().getX();
            int y = element.getPosition().getY();

            Shape rectangle = new Rectangle(GRID_WIDTH, GRID_WIDTH);

            if (element instanceof Animal animal) {
                var animalDrawing = createAnimalDrawing(animal.getOrientation(), animal.getEnergy());
                mapGrid.add(animalDrawing, x, y);
            } else if (element instanceof Plant) {
                rectangle.setFill(Color.LIGHTGREEN);
                mapGrid.add(rectangle, x, y);
            } else {
                rectangle.setFill(Color.RED);
                mapGrid.add(rectangle, x, y);
            }

        });
    }

    private Pane createAnimalDrawing(MapDirection orientation, int animalEnergy) {
        Pane pane = new Pane();
        double centerX = (double) GRID_WIDTH / 2;
        double centerY = (double) GRID_WIDTH / 2;
        double radius = (double) GRID_WIDTH / 2 - 2;
        Circle head = new Circle(centerX, centerY, radius);
        head.setFill(calculateColor(animalEnergy));
        pane.getChildren().add(head);
        return pane;
    }

    private Color calculateColor(int animalEnergy) {
        int maxEnergy = 40;
        animalEnergy = Math.max(0, Math.min(animalEnergy, maxEnergy));
        double energyFactor = (double) animalEnergy / maxEnergy;

        int red = (int) (255 * energyFactor);
        int green = 0;
        int blue = (int) (255 * energyFactor);

        return Color.rgb(red, green, blue);
    }

    private int calculateGridWidth(Vector2d leftBot, Vector2d rightTop) {
        return Math.abs(leftBot.subtract(rightTop).getX());
    }

    private int calculateGridHeight(Vector2d leftBot, Vector2d rightTop) {
        return Math.abs(leftBot.subtract(rightTop).getY());
    }

    private void updateStatistics() {
        animalCountLabel.setText(String.format("%d", simulationContext.getAnimalCount()));
        plantCountLabel.setText(String.format("%d", ((Earth) worldMap).getPlantCount()));
        freeFieldsLabel.setText(String.format("%d", ((Earth) worldMap).getCountOfEmptyFields()));
        avgEnergyLabel.setText(String.format("%.2f", simulationContext.getAverageAnimalEnergy().orElse(0.0)));
        Optional<List<Gen>> mostPopularGenotype = simulationContext.getMostPopularGenotype();
        if (mostPopularGenotype.isPresent()) {
            popularGenotypeLabel.setText(mostPopularGenotype.get().toString());
        } else {
            popularGenotypeLabel.setText("No animals");
        }
        avgLifespanLabel.setText(String.format("%.2f", simulationContext.getAverageDeadAnimalTimeLife().orElse(0.0)));
        avgChildrenLabel.setText(String.format("%.2f", simulationContext.getAverageAnimalCountOfChildren().orElse(0.0)));
    }

}

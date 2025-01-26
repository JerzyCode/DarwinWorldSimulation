package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.listener.MapChangeListener;
import agh.ics.oop.listener.SimulationFinishedListener;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.event.EventType;
import agh.ics.oop.model.event.MapChangedEvent;
import agh.ics.oop.model.exceptions.PresenterHasNoConfigurationException;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.simulation.SimulationWorldMap;
import agh.ics.oop.model.repository.CsvStatisticsRepositoryAdapter;
import agh.ics.oop.model.repository.StatisticsRepositoryPort;
import agh.ics.oop.model.util.PlantPreferableAreaCalculator;
import agh.ics.oop.presenter.components.AnimalComponent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Setter;

import java.io.IOException;
import java.util.stream.Collectors;

public class SimulationPresenter implements MapChangeListener, SimulationFinishedListener, PositionClickHandler, OnCancelTrackingHandler {
    private static final int GRID_SIZE = 20;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Label currentDayLabel;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button startStopButton;

    @FXML
    private Button highlightAnimalsWithMostPopularGenotypeButton;
    @FXML
    private Button showPreferredPlantPositionsButton;

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

    @Setter
    private Simulation simulation;
    @Setter
    private Configuration configuration;
    private SimulationWorldMap worldMap;
    private StatisticsRepositoryPort statisticsRepository;
    private Animal selectedAnimal;
    private AnimalStatisticsView animalStatisticsViewController;
    private AnimalListView animalListViewController;
    private boolean shouldHighlightAnimalsWithMostPopularGenotype = false;

    private Boundary preferablePlantArea;
    private double scaleFactor = 1.0;
    private double initialX;
    private double initialY;
    private boolean isRunning;
    private boolean preferredPlantPositionsVisible = false;


    @FXML
    void initialize() {
        setGridOnScrollEvent();
        onGridDrag();
    }

    public void drawMap() {
        var mapBoundary = worldMap.getCurrentBounds();
        clearGrid();
        fillGrid(mapBoundary);
        drawElements();
    }

    @Override
    public void mapChanged(WorldMap worldMap, MapChangedEvent event) {
        if (event.getEventType() == EventType.DAY_ENDS) {
            Platform.runLater(this::drawMap);
            Platform.runLater(this::updateStatisticsDisplay);
            saveStatistics();
        }
    }

    @Override
    public void onSimulationFinished() {
        closeRepositoryPort();
        isRunning = false;
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("simulation_summary_view.fxml"));
                Parent finishedView = loader.load();
                mainBorderPane.setCenter(finishedView);
                mainBorderPane.setLeft(null);

                SimulationSummaryPresenter presenter = loader.getController();
                presenter.setGraphData(simulation.getSimulationContext().getGraphData());
            } catch (IOException e) {
                System.out.println("Couldn't load simulation summary view, e=" + e.getMessage());
            }
        });
    }

    @FXML
    public void onHighlightAnimalsWithMostPopularGenotypeClicked() {
        shouldHighlightAnimalsWithMostPopularGenotype = !shouldHighlightAnimalsWithMostPopularGenotype;
        if (shouldHighlightAnimalsWithMostPopularGenotype) {
            highlightAnimalsWithMostPopularGenotypeButton.setText("Disable Highlighting Popular Genotype");
        } else {
            highlightAnimalsWithMostPopularGenotypeButton.setText("Enable Highlighting Popular Genotype");
        }
        Platform.runLater(this::drawMap);
    }

    @FXML
    public void showPreferredPlantPositions() {
        if (preferablePlantArea == null || isRunning) {
            return;
        }

        preferredPlantPositionsVisible = !preferredPlantPositionsVisible;
        if (preferredPlantPositionsVisible) {
            showPreferredPlantPositionsButton.setText("Hide Preferred Plant Positions");
        } else {
            showPreferredPlantPositionsButton.setText("Show Preferred Plant Positions");
        }
        Platform.runLater(this::drawMap);
    }

    @FXML
    public void onSimulationStartClicked() {
        if (configuration == null) {
            throw new PresenterHasNoConfigurationException("Presenter has no configuration!");
        }
        isRunning = true;
        var simulationContext = simulation.getSimulationContext();

        worldMap = simulationContext.getWorldMap();
        simulationContext.addMapChangedListener(this);
//        simulationContext.addMapChangedListener(new LoggerListener());
        simulationContext.addSimulationFinishedListener(this);

        preferablePlantArea = PlantPreferableAreaCalculator.getPreferableArea(worldMap.getCurrentBounds());
        statisticsRepository = new CsvStatisticsRepositoryAdapter();

        simulation.resume();

        startStopButton.setText("Stop");
        startStopButton.setOnAction(event -> stopSimulation());
    }


    public void selectAnimal(Animal animal) {
        this.selectedAnimal = animal;
        Platform.runLater(this::drawMap);
    }


    private void stopSimulation() {
        if (simulation != null) {
            startStopButton.setText("Start");
            simulation.pause();
            startStopButton.setOnAction(event -> resumeSimulation());
            isRunning = false;
        }
    }

    private void resumeSimulation() {
        if (simulation != null) {
            startStopButton.setText("Stop");
            simulation.resume();
            startStopButton.setOnAction(event -> stopSimulation());
            isRunning = true;
        }
    }

    public void endSimulation() {
        if (simulation != null) {
            simulation.end();
        }
    }

    void displayAnimalStatistics() {
        Platform.runLater(() -> {
            try {
                startStopButton.setDisable(false);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("animal_statistics_view.fxml"));
                Parent animalsView = loader.load();

                animalStatisticsViewController = loader.getController();
                animalStatisticsViewController.setOnCancelTrackingHandler(this);
                animalStatisticsViewController.setAnimal(selectedAnimal);
                animalStatisticsViewController.updateLabels(simulation.getSimulationContext().getStatistics().getCurrentDay());
                mainBorderPane.setRight(animalsView);
            } catch (IOException e) {
                System.out.println("Couldn't load animal statistics view, e=" + e.getMessage());
            }
        });
    }


    @Override
    public void onPositionClick(Vector2d position) {
        if (isRunning) {
            return;
        }

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("animals_list_view.fxml"));
                Parent animalsView = loader.load();
                animalListViewController = loader.getController();
                animalListViewController.setPresenter(this);
                animalListViewController.setAnimals(worldMap.getAnimals()
                        .stream()
                        .filter(animal -> animal.getPosition().equals(position))
                        .collect(Collectors.toSet()));
                mainBorderPane.setRight(animalsView);
            } catch (IOException e) {
                System.out.println("Couldn't load animal statistics view, e=" + e.getMessage());
            }

            selectAnimal(null);
            startStopButton.setDisable(true);
        });
    }

    @Override
    public void onCancelTrackingAnimal() {
        mainBorderPane.setRight(null);
        startStopButton.setDisable(false);
        selectAnimal(null);
    }

    private void clearGrid() {
        if (mapGrid.getChildren() != null) {
            mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        }

        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }


    private void fillGrid(Boundary mapBoundary) {
        int width = calculateGridWidth(mapBoundary.leftBottomCorner(), mapBoundary.rightTopCorner());
        int height = calculateGridHeight(mapBoundary.leftBottomCorner(), mapBoundary.rightTopCorner());

        for (int i = 0; i <= width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(GRID_SIZE));
        }

        for (int i = 0; i <= height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(GRID_SIZE));
        }
    }

    private void highlightPlantPreferableArea() {
        if (!preferredPlantPositionsVisible || isRunning) {
            return;
        }

        for (int x = preferablePlantArea.leftBottomCorner().getX(); x <= preferablePlantArea.rightTopCorner().getX(); x++) {
            for (int y = preferablePlantArea.leftBottomCorner().getY(); y <= preferablePlantArea.rightTopCorner().getY(); y++) {
                mapGrid.add(createRectangle(Color.valueOf("F3FAC4FF").brighter()), x, y);
            }
        }

    }

    private void drawElements() {
        highlightPlantPreferableArea();
        worldMap.getElements().stream()
                .collect(Collectors.groupingBy(WorldElement::getPosition))
                .forEach((position, elements) -> {
                    int x = position.getX();
                    int y = position.getY();

                    StackPane positionContainer = new StackPane();

                    for (WorldElement element : elements) {
                        if (element instanceof Plant) {
                            positionContainer.getChildren().add(createRectangle(Color.LIGHTGREEN));
                        } else if (element instanceof Animal animal) {
                            positionContainer.getChildren().add(createAnimalComponent(animal));
                        } else {
                            positionContainer.getChildren().add(createRectangle(Color.RED));
                        }
                    }
                    mapGrid.add(positionContainer, x, y);
                });
    }

    private int calculateGridWidth(Vector2d leftBot, Vector2d rightTop) {
        return Math.abs(leftBot.subtract(rightTop).getX());
    }

    private int calculateGridHeight(Vector2d leftBot, Vector2d rightTop) {
        return Math.abs(leftBot.subtract(rightTop).getY());
    }

    private void updateStatisticsDisplay() {
        var statistics = simulation.getSimulationContext().getStatistics();
        animalCountLabel.setText(String.valueOf(statistics.getAnimalCount()));
        plantCountLabel.setText(String.format("%d", statistics.getPlantCount()));
        freeFieldsLabel.setText(String.format("%d", statistics.getFreeFieldsCount()));
        avgEnergyLabel.setText(String.format("%.2f", statistics.getAverageEnergy()));
        var mostPopularGenotype = statistics.getMostPopularGenotype();
        if (mostPopularGenotype != null && !mostPopularGenotype.isEmpty()) {
            popularGenotypeLabel.setText(mostPopularGenotype.toString());
        } else {
            popularGenotypeLabel.setText("No animals");
        }
        avgLifespanLabel.setText(String.format("%.2f", statistics.getAverageLifespan()));
        avgChildrenLabel.setText(String.format("%.2f", statistics.getAverageChildren()));
        currentDayLabel.setText(String.format("%d", statistics.getCurrentDay()));
        if (animalStatisticsViewController != null) {
            animalStatisticsViewController.updateLabels(statistics.getCurrentDay());
        }
    }

    private void saveStatistics() {
        if (configuration.getSimulationConfiguration().isSaveStatisticsCsv()) {
            statisticsRepository.save(simulation.getSimulationContext().getStatistics(), worldMap.getId().toString());
        }
    }

    private void closeRepositoryPort() {
        if (configuration.getSimulationConfiguration().isSaveStatisticsCsv()) {
            try {
                statisticsRepository.close();
            } catch (IOException e) {
                System.out.println("Error closing statistics repository");
            }
        }
    }

    private void setGridOnScrollEvent() {
        mapGrid.setOnScroll((ScrollEvent event) -> {
            double delta = event.getDeltaY();
            double zoomFactor = 1.1;

            if (delta < 0) {
                zoomFactor = 0.9;
            }

            scaleFactor *= zoomFactor;

            mapGrid.setScaleX(scaleFactor);
            mapGrid.setScaleY(scaleFactor);
        });
    }

    private void onGridDrag() {
        mapGrid.setOnMousePressed(event -> {
            initialX = event.getSceneX();
            initialY = event.getSceneY();
            mapGrid.setCursor(Cursor.CLOSED_HAND);

        });

        mapGrid.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - initialX;
            double deltaY = event.getSceneY() - initialY;
            double newTranslateX = mapGrid.getTranslateX() + deltaX;
            double newTranslateY = mapGrid.getTranslateY() + deltaY;

            mapGrid.setTranslateX(newTranslateX);
            mapGrid.setTranslateY(newTranslateY);

            initialX = event.getSceneX();
            initialY = event.getSceneY();
        });

        mapGrid.setOnMouseReleased(event -> mapGrid.setCursor(Cursor.DEFAULT));
    }


    private Rectangle createRectangle(Color color) {
        var rectangle = new Rectangle(GRID_SIZE, GRID_SIZE);
        rectangle.setFill(color);
        return rectangle;
    }

    private AnimalComponent createAnimalComponent(Animal animal) {
        boolean isSelected = animal.equals(selectedAnimal);
        boolean isHighlighted = simulation.getSimulationContext()
                .getStatistics()
                .getMostPopularGenotype()
                .equals(animal.getGenome().getGens()) && shouldHighlightAnimalsWithMostPopularGenotype;
        return new AnimalComponent(animal, isSelected, GRID_SIZE, isHighlighted, this);
    }


}

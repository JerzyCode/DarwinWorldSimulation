package agh.ics.oop.presenter.components;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.presenter.AnimalClickHandler;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Getter;

public class AnimalComponent extends StackPane {
    @Getter
    private final Animal animal;
    private final int gridSize;


    public AnimalComponent(Animal animal, boolean isSelected, int gridSize, boolean isHighlighted, AnimalClickHandler onAnimalClick) {
        this.animal = animal;
        this.gridSize = gridSize;

        Pane animalDrawing = createAnimalDrawing(animal.getEnergy());
        getChildren().add(animalDrawing);

        if (isSelected) {
            displayBorder();
        }

        if (isHighlighted) {
            animalDrawing.getStyleClass().add("highlighted");
        }

        setOnClickHandler(onAnimalClick);
        setMouseHoverEffects(animalDrawing);
    }

    private Pane createAnimalDrawing(int animalEnergy) {
        Pane pane = new Pane();
        double centerX = (double) gridSize / 2;
        double centerY = (double) gridSize / 2;
        double radius = (double) gridSize / 2 - 2;
        Circle head = new Circle(centerX, centerY, radius);
        head.setFill(calculateColor(animalEnergy));
        pane.getChildren().add(head);
        pane.setPrefSize(gridSize, gridSize);

        return pane;
    }

    private Color calculateColor(int animalEnergy) {
        int maxEnergy = 40;
        animalEnergy = Math.max(0, Math.min(animalEnergy, maxEnergy));
        double energyFactor = (double) animalEnergy / maxEnergy;

        int red = 0;
        int green = (int) (255 * energyFactor);
        int blue = (int) (128 + (127 * energyFactor));

        return Color.rgb(red, green, blue);
    }


    private void setOnClickHandler(AnimalClickHandler animalHandler) {
        this.setOnMouseClicked(event -> {
            animalHandler.onAnimalClick(animal.getPosition());
            displayBorder();
        });
    }

    private void setMouseHoverEffects(Pane animalDrawing) {
        Circle head = (Circle) animalDrawing.getChildren().getFirst();
        this.setOnMouseEntered(event -> {
            animalDrawing.setOpacity(0.8);
            head.setFill(Color.ORANGE);
            this.setCursor(Cursor.HAND);
        });

        this.setOnMouseExited(event -> {
            animalDrawing.setOpacity(1.0);
            head.setFill(calculateColor(animal.getEnergy()));
            this.setCursor(Cursor.DEFAULT);
        });
    }


    private void displayBorder() {
        Shape border = new Rectangle(gridSize, gridSize);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.HOTPINK);
        border.setStrokeWidth(3);

        border.setLayoutX(0);
        border.setLayoutY(0);

        getChildren().add(border);
    }
}


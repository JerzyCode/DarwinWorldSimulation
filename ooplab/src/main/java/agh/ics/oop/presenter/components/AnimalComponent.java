package agh.ics.oop.presenter.components;
import agh.ics.oop.model.elements.Animal;
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

    public AnimalComponent(Animal animal, boolean isSelected, int gridSize) {
        this.animal = animal;
        this.gridSize = gridSize;

        Pane animalDrawing = createAnimalDrawing(animal.getEnergy());
        getChildren().add(animalDrawing);

        if (isSelected) {
            Shape border = new Rectangle(gridSize, gridSize);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.BLUE);
            border.setStrokeWidth(2.0);
            getChildren().add(border);
        }

        setOnMouseClicked(event -> handleClick());
    }

    private void handleClick() {
        System.out.println("Clicked on animal: " + animal);
    }

    private Pane createAnimalDrawing(int animalEnergy) {
        Pane pane = new Pane();
        double centerX = (double) gridSize / 2;
        double centerY = (double) gridSize / 2;
        double radius = (double) gridSize / 2 - 2;
        Circle head = new Circle(centerX, centerY, radius);
        head.setFill(calculateColor(animalEnergy));
        pane.getChildren().add(head);
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
}


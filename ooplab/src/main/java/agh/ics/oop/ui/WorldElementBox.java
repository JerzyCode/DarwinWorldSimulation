package agh.ics.oop.ui;

import agh.ics.oop.model.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class WorldElementBox {
  private static final String ASSETS_PATH = "/assets/";
  private final VBox container;

  public WorldElementBox(WorldElement worldElement, int gridSize) {
    gridSize = gridSize / 2;
    Image image = new Image(Objects.requireNonNull(getClass().getResource(ASSETS_PATH + worldElement.getImageName())).toExternalForm());

    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(gridSize);
    imageView.setFitWidth(gridSize);

    Label label = new Label(worldElement.getPosition().toString());

    container = new VBox(imageView, label);
    container.setPrefHeight(gridSize);
    container.setPrefWidth(gridSize);
    container.setAlignment(Pos.CENTER);
  }

  public VBox getContainer() {
    return container;
  }
}

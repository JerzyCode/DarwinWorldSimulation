package agh.ics.oop.model.map;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Grass;
import agh.ics.oop.model.map.elements.WorldElement;

import java.util.*;

public class Earth extends AbstractWorldMap {
  private final Boundary boundary;
  private final Map<Vector2d, Grass> grasses;

  public Earth(int width, int height, int startPlantCount) {
    super();
    boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
    grasses = new HashMap<>();
    placePlants(height, width, startPlantCount);
  }

  // TODO: removed unused constructor
  public Earth() {
    this(1, 1,1);
  }

  @Override
  public Boundary getCurrentBounds() {
    return boundary;
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    return true;
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    if (animals.containsValue(animal)) {
      animals.remove(animal.getPosition());
      animal.move(direction, this);
      fixAnimalPositionAfterMove(animal);
      animals.put(animal.getPosition(), animal);
      super.notifyListeners("Animal moved to position: " + animal.getPosition());
    }
  }

  @Override
  public Collection<WorldElement> getElements() {
    var elements = new ArrayList<WorldElement>();
    elements.addAll(grasses.values());
    elements.addAll(super.getElements());

    return Collections.unmodifiableCollection(elements);
  }

  public void placePlants(int plantCount){
      var height = this.boundary.rightTopCorner().getY() - this.boundary.leftBottomCorner().getY();
      var width = this.boundary.rightTopCorner().getX() - this.boundary.leftBottomCorner().getX();
      placePlants(height, width, plantCount);
  }

  public void eatGrass(){
    animals.values().forEach(this::eatGrass);
  }

  private void placePlants(int countOfRows, int countOfColumns, int plantCount) {
    int countOfPreferableRows = Math.max(1, (int) (countOfRows * 0.2));
    int notPreferableRows = countOfRows - countOfPreferableRows;
    int minIndexOfPreferableRow = notPreferableRows / 2;
//    int maxIndexOfPreferableRow = minIndexOfPreferableRow + countOfPreferableRows - 1;

    int CountOfPlacedPlants = 0;
    while(CountOfPlacedPlants < plantCount) {
      int row;
      if (Math.random() < 0.8) {
        row = minIndexOfPreferableRow + (int) (Math.random() * countOfPreferableRows);
      } else {
        row = (int) (Math.random() * notPreferableRows);
        if (row >= minIndexOfPreferableRow) {
          row += countOfPreferableRows;
        }
      }
      int col = (int) (Math.random() * countOfColumns);
      // TODO: z jakiejś przyczny tutaj col i row musi być tutaj odwrotnie żeby działało
      Vector2d position = new Vector2d(col, row);
      if (!grasses.containsKey(position)) {
        grasses.put(position, new Grass(position));
        CountOfPlacedPlants++;
      }
    }
  }

  private void fixAnimalPositionAfterMove(Animal animal){
    var position = animal.getPosition();

    Vector2d newPosition = animal.getPosition();
    MapDirection orientation = animal.getOrientation();
    if (position.getX() > boundary.rightTopCorner().getX()) {
      newPosition = new Vector2d(boundary.leftBottomCorner().getX(), position.getY());
    }
    else if (position.getX() < boundary.leftBottomCorner().getX()) {
      newPosition = new Vector2d(boundary.rightTopCorner().getX(), position.getY());
    }
    else if (position.getY() > boundary.rightTopCorner().getY()) {
      newPosition = new Vector2d(position.getX(), boundary.rightTopCorner().getY());
      orientation = MapDirection.SOUTH;
    }
    else if (position.getY() < boundary.leftBottomCorner().getY()) {
      newPosition = new Vector2d(position.getX(), boundary.leftBottomCorner().getY());
      orientation = MapDirection.NORTH;
    }

    animal.setOrientation(orientation);
    animal.setPosition(newPosition);

  }

  private void eatGrass(Animal animal){
    var position = animal.getPosition();
    if (grasses.containsKey(position)) {
      grasses.remove(position);
      // TODO: increase animal energy
    }
  }

}

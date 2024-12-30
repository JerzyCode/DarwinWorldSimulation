package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Grass;
import agh.ics.oop.model.map.elements.WorldElement;

import java.util.*;

public class Earth extends AbstractWorldMap implements PositionAdjuster {
  private final Boundary boundary;
  private final Map<Vector2d, Grass> grasses;

  public Earth(int width, int height, int startPlantCount) {
    super();
    boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
    grasses = new HashMap<>();
//    placePlants(height, width, startPlantCount);
  }

  // TODO: remove unused constructor
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
  public void adjustAnimalAfterMove(Animal animal) {
    var position = animal.getPosition();
    if (position.getY() < boundary.leftBottomCorner().getY()) {
      animal.setPosition(new Vector2d(position.getX(), boundary.leftBottomCorner().getY()));
      animal.setOrientation(MapDirection.NORTH);
    } else if (position.getY() > boundary.rightTopCorner().getY()) {
      animal.setPosition(new Vector2d(position.getX(), boundary.rightTopCorner().getY()));
    } else if (position.getX() < boundary.leftBottomCorner().getX()) {
      animal.setPosition(new Vector2d(boundary.rightTopCorner().getX(), position.getY()));
    }
    else if (position.getX() > boundary.rightTopCorner().getX()) {
      animal.setPosition(new Vector2d(boundary.leftBottomCorner().getX(), position.getY()));
    }
    //TODO: mogę najwyżej zwracać nowego animala i wtedy będzie bez setterów
  }

  @Override
  public Collection<WorldElement> getElements() {
    var elements = new ArrayList<WorldElement>();
    elements.addAll(grasses.values());
    elements.addAll(super.getElements());

    return Collections.unmodifiableCollection(elements);
  }

//  public void placePlants(int plantCount){
//      var height = this.boundary.rightTopCorner().getY() - this.boundary.leftBottomCorner().getY();
//      var width = this.boundary.rightTopCorner().getX() - this.boundary.leftBottomCorner().getX();
//      placePlants(height, width, plantCount);
//  }

  public boolean isGrassAtPosition(Vector2d position) {
    return grasses.containsKey(position);
  }

  public void removeGrass(Vector2d position){
      grasses.remove(position);
  }

  @Override
  public MoveDirection adjustPosition(Vector2d position) {
    return null;
  }

//  private void placePlants(int countOfRows, int countOfColumns, int plantCount) {
//    int countOfPreferableRows = Math.max(1, (int) (countOfRows * 0.2));
//    int notPreferableRows = countOfRows - countOfPreferableRows;
//    int minIndexOfPreferableRow = notPreferableRows / 2;
//
//    int CountOfPlacedPlants = 0;
//    while(CountOfPlacedPlants < plantCount) {
//      int row;
//      if (Math.random() < 0.8) {
//        row = minIndexOfPreferableRow + (int) (Math.random() * countOfPreferableRows);
//      } else {
//        row = (int) (Math.random() * notPreferableRows);
//        if (row >= minIndexOfPreferableRow) {
//          row += countOfPreferableRows;
//        }
//      }
//      int col = (int) (Math.random() * countOfColumns);
//      // TODO: z jakiejś przyczny tutaj col i row musi być tutaj odwrotnie żeby działało
//      Vector2d position = new Vector2d(col, row);
//      if (!grasses.containsKey(position)) {
//        grasses.put(position, new Grass(position));
//        CountOfPlacedPlants++;
//      }
//    }
  }

//}

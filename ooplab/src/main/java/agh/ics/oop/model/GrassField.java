package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class GrassField extends AbstractWorldMap {
    private final RandomPositionGenerator randomizer;
    private final Map<Vector2d, Grass> grasses;
    private Vector2d displayLeftBotCorner;
    private Vector2d displayRightTopCorner;

    public GrassField(int grassCount) {
        this(new RandomPositionGenerator(grassCount, (int) Math.sqrt(grassCount * 10) + 1, (int) Math.sqrt(grassCount * 10) + 1));
    }

    GrassField(RandomPositionGenerator randomizer) { //constructor for testing purposes
        super();
        this.grasses = new HashMap<>();
        this.randomizer = randomizer;
        placeGrass();
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        super.move(animal, direction);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grasses.containsKey(position);
    }

    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
      return super.objectAt(position)
          .or(() -> Optional.ofNullable(grasses.get(position)));
    }

    @Override
    public Collection<WorldElement> getElements() {
        return Stream
                .concat(animals.values().stream(), grasses.values().stream())
                .toList();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return objectAt(position)
                .filter(worldElement -> worldElement instanceof Animal)
                .isEmpty();
    }

    private void placeGrass() {
        randomizer.forEach(position -> {
            var grass = new Grass(position);
            grasses.put(grass.getPosition(), grass);
        });
    }

    @Override
    public Boundary getCurrentBounds() {
        adjustDisplayCorners();
        return new Boundary(displayLeftBotCorner, displayRightTopCorner);
    }

    void adjustDisplayCorners() {
        var botLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        var rightTop = new Vector2d(-Integer.MAX_VALUE, -Integer.MAX_VALUE);

        for (var el : getElements()) {
            botLeft = botLeft.lowerLeft(el.getPosition());
            rightTop = rightTop.upperRight(el.getPosition());
        }
        displayLeftBotCorner = botLeft;
        displayRightTopCorner = rightTop;
    }
}

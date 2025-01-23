package agh.ics.oop.model.map;

import agh.ics.oop.listener.MapChangeListener;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.event.EventCreator;
import agh.ics.oop.model.event.MapChangedEvent;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByWorldElementException;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final MapVisualizer mapVisualizer;
    private final List<MapChangeListener> mapChangeListeners;
    private final UUID id;
    private final Map<Vector2d, Animal> animals;

    protected AbstractWorldMap() {
        this.mapVisualizer = new MapVisualizer(this);
        this.mapChangeListeners = new ArrayList<>();
        this.animals = new HashMap<>();
        this.id = UUID.randomUUID();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        var position = animal.getPosition();
        if (!canMoveTo(position)) {
            throw new PositionOccupiedByWorldElementException(position, Animal.class.getSimpleName());
        }

        animals.put(position, animal);
        notifyListeners(EventCreator.createAnimalPlacedEvent(animal.getPosition()));
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        if (animals.containsValue(animal)) {
            animals.remove(animal.getPosition());
            animal.move(direction, this);
            animals.put(animal.getPosition(), animal);
            notifyListeners(EventCreator.createAnimalMovedEvent(animal.getPosition()));
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public Collection<WorldElement> getElements() {
        return Collections.unmodifiableCollection(new ArrayList<>(animals.values()));
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position);
    }

    public abstract Boundary getCurrentBounds();

    protected final boolean isPositionWithinMapBoundary(Vector2d position) {
        var boundary = getCurrentBounds();
        return position.follows(boundary.leftBottomCorner()) &&
                position.precedes(boundary.rightTopCorner());
    }

    @Override
    public final String toString() {
        var bounds = getCurrentBounds();
        return mapVisualizer.draw(bounds.leftBottomCorner(), bounds.rightTopCorner());
    }

    public void addListener(MapChangeListener listener) {
        mapChangeListeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        mapChangeListeners.remove(listener);
    }

    protected void notifyListeners(MapChangedEvent event) {
        mapChangeListeners.forEach(listener -> listener.mapChanged(this, event));
    }
}

package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FireEarth extends Earth {
//    private final HashMap<Vector2d, Fire> fires;
    public final HashMap<Vector2d, Fire> fires;

    public FireEarth(int width, int height){
        super(width, height);
        this.fires = new HashMap<>();
    }

    @Override
    public void placePlant(Plant plant) throws IncorrectPositionException {
        var position = plant.getPosition();
        if (isPlantAtPosition(position) || isFireAtPosition(position)) {
            throw new IncorrectPositionException(position);
        }
        plants.put(position, plant);
        notifyListeners("Plant was placed on position: " + position);
    }

    public boolean isFireAtPosition(Vector2d position){
        return fires.containsKey(position);
    }

    public void placeFire(Fire fire) throws IncorrectPositionException {
        var position = fire.getPosition();
        if(isFireAtPosition(position) || isPlantAtPosition(position)){
            throw new IncorrectPositionException(position);
        }
        this.fires.put(position, fire);
        notifyListeners("Fire was placed at position: " + position);
    }

    public void decreaseFireRemainingLifetime(){
        fires.values().forEach(Fire::decreaseRemainingLifetime);
    }

    //TODO: ma się rozprzestrzeniać na przylegające rośliny, ale czy roślina całkiem po prawej przylega do tej całkiem po lewej, a ta całkiem u góry do tej całkiem na dole?

    public Set<Vector2d> getNewFirePositions(Fire fire){
        var adjacentFields = getAdjacentFields(fire.getPosition());
        return adjacentFields
                .stream()
                .filter(position -> !isFireAtPosition(position))
                .filter(this::isPlantAtPosition)
                .collect(Collectors.toSet());
    }

    public void removeFire(Fire fire){
        fires.remove(fire.getPosition());
        notifyListeners("Fire was removed from position: " + fire.getPosition());
    }

    public Set<Fire> getBurnedFires(){
        return fires.values()
                .stream()
                .filter(Fire::isBurned)
                .collect(Collectors.toSet());
    }

    private List<Vector2d> getAdjacentFields(Vector2d position){
        var x = position.getX();
        var y = position.getY();

        int rightX = (x + 1) <= getCurrentBounds().rightTopCorner().getX() ? (x + 1) : getCurrentBounds().leftBottomCorner().getX();
        int upperY = (y + 1) <= getCurrentBounds().rightTopCorner().getY() ? (y + 1) : getCurrentBounds().leftBottomCorner().getY();
        int leftX = (x - 1) >= 0 ? (x - 1) : getCurrentBounds().rightTopCorner().getX();
        int downY = (y - 1) >= 0 ? (y - 1) : getCurrentBounds().rightTopCorner().getY();

        Vector2d neighbour1 = new Vector2d(x, upperY);
        Vector2d neighbour2 = new Vector2d(x, downY);
        Vector2d neighbour3 = new Vector2d(leftX, y);
        Vector2d neighbour4 = new Vector2d(rightX, y);

        return List.of(neighbour1, neighbour2, neighbour3, neighbour4);
    }

}

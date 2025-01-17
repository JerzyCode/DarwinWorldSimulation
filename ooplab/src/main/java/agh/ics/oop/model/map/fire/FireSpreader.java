package agh.ics.oop.model.map.fire;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;

import java.util.*;
import java.util.stream.Collectors;

class FireSpreader {
    private final int fireDuration;

    FireSpreader(int fireDuration) {
        this.fireDuration = fireDuration;
    }

    Optional<Fire> createNewFire(List<Vector2d> availablePositions, FireValidator fireValidator) {
        Collections.shuffle(availablePositions);
        return availablePositions.stream()
                .filter(fireValidator::canPlaceFire)
                .findAny()
                .map(position -> new Fire(position, fireDuration));
    }


    Set<Fire> getSpreadFires(Collection<Fire> currentFires, Boundary boundary, FireValidator fireValidator) {
        return currentFires.stream()
                .map(Fire::getPosition)
                .flatMap(pos -> getAdjacentFields(pos, boundary).stream())
                .distinct()
                .filter(fireValidator::canPlaceFire)
                .map(pos -> new Fire(pos, fireDuration))
                .collect(Collectors.toSet());
    }


    private Set<Vector2d> getAdjacentFields(Vector2d position, Boundary boundary) {
        var x = position.getX();
        var y = position.getY();

        int rightX = (x + 1) <= boundary.rightTopCorner().getX() ? (x + 1) : boundary.leftBottomCorner().getX();
        int upperY = (y + 1) <= boundary.rightTopCorner().getY() ? (y + 1) : boundary.leftBottomCorner().getY();
        int leftX = (x - 1) >= 0 ? (x - 1) : boundary.rightTopCorner().getX();
        int downY = (y - 1) >= 0 ? (y - 1) : boundary.rightTopCorner().getY();

        Vector2d neighbour1 = new Vector2d(x, upperY);
        Vector2d neighbour2 = new Vector2d(x, downY);
        Vector2d neighbour3 = new Vector2d(leftX, y);
        Vector2d neighbour4 = new Vector2d(rightX, y);

        return Set.of(neighbour1, neighbour2, neighbour3, neighbour4);
    }
}

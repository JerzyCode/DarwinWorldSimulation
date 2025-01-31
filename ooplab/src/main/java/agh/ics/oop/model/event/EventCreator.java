package agh.ics.oop.model.event;

import agh.ics.oop.model.Vector2d;

public class EventCreator {

    private EventCreator() {
    }

    public static MapChangedEvent createFirePlacedEvent(Vector2d firePosition) {
        return MapChangedEvent.builder()
                .eventType(EventType.FIRE_PLACED)
                .message(String.format("Fire was placed at %s", firePosition))
                .build();
    }

    public static MapChangedEvent createPlantPlacedEvent(Vector2d plantPosition) {
        return MapChangedEvent.builder()
                .eventType(EventType.PLANT_PLACED)
                .message(String.format("Plant was placed at %s", plantPosition))
                .build();
    }

    public static MapChangedEvent createAnimalMovedEvent(Vector2d animalPosition) {
        return MapChangedEvent.builder()
                .eventType(EventType.ANIMAL_MOVED)
                .message(String.format("Animal moved to position: %s", animalPosition))
                .build(); // gdyby nie wzorzec builder to zajęłoby to aż... 1 linijkę
    }

    public static MapChangedEvent createAnimalPlacedEvent(Vector2d animalPosition) {
        return MapChangedEvent.builder()
                .eventType(EventType.ANIMAL_PLACED)
                .message(String.format("Animal was placed at: %s", animalPosition))
                .build();
    }

    public static MapChangedEvent createDayEndsEvent(int dayCount) {
        return MapChangedEvent.builder()
                .eventType(EventType.DAY_ENDS)
                .message(String.format("Day %d has ended.", dayCount))
                .build();
    }

}

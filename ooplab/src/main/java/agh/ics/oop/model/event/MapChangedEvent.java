package agh.ics.oop.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MapChangedEvent {
    private final EventType eventType;
    private final String message;
}

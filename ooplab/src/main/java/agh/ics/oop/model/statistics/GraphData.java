package agh.ics.oop.model.statistics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GraphData {
    private final int currentDay;
    private final int animalCount;
    private final int plantCount;
}

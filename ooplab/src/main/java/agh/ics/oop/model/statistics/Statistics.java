package agh.ics.oop.model.statistics;

import agh.ics.oop.model.elements.Gen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Statistics {
    private int currentDay;
    private final int animalCount;
    private final int plantCount;
    private final int freeFieldsCount;
    private final double averageEnergy;
    private final List<Gen> mostPopularGenotype;
    private final double averageLifespan;
    private final double averageChildren;
}

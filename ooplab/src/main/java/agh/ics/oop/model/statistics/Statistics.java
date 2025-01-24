package agh.ics.oop.model.statistics;

import agh.ics.oop.model.elements.Gen;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private int currentDay;
    private int animalCount;
    private int plantCount;
    private int freeFieldsCount;
    private double averageEnergy;
    private List<Gen> mostPopularGenotype;
    private double averageLifespan;
    private double averageChildren;
}

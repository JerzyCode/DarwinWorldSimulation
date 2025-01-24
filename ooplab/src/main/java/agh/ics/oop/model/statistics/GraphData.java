package agh.ics.oop.model.statistics;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class GraphData {
    private final int currentDay;
    private final int animalCount;
    private final int plantCount;
}

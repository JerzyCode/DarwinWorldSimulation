package agh.ics.oop.model.configuration;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SimulationConfiguration {
    private final int daysCount;
    private final int startAnimalCount;
}

package agh.ics.oop.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class SimulationConfiguration {
    private final int daysCount;
    private final int startAnimalCount;

    @JsonCreator
    SimulationConfiguration(
            @JsonProperty("daysCount") int daysCount,
            @JsonProperty("startAnimalCount") int startAnimalCount) {
        this.daysCount = daysCount;
        this.startAnimalCount = startAnimalCount;
    }
}

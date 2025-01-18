package agh.ics.oop.model.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class WorldMapConfiguration {
    private final int height;
    private final int width;
    private final int fireFrequency;
    private final int fireDuration;
    private final int energyGain;
    private final int plantGrowth;
    private final int startPlantCount;
    private final WorldMapVariant mapVariant;
    private final PlantVariant plantVariant;
}

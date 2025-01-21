package agh.ics.oop.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
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

    @JsonCreator
    public WorldMapConfiguration(
            @JsonProperty("height") int height,
            @JsonProperty("width") int width,
            @JsonProperty("fireFrequency") int fireFrequency,
            @JsonProperty("fireDuration") int fireDuration,
            @JsonProperty("energyGain") int energyGain,
            @JsonProperty("plantGrowth") int plantGrowth,
            @JsonProperty("startPlantCount") int startPlantCount,
            @JsonProperty("mapVariant") WorldMapVariant mapVariant,
            @JsonProperty("plantVariant") PlantVariant plantVariant) {
        this.height = height;
        this.width = width;
        this.fireFrequency = fireFrequency;
        this.fireDuration = fireDuration;
        this.energyGain = energyGain;
        this.plantGrowth = plantGrowth;
        this.startPlantCount = startPlantCount;
        this.mapVariant = mapVariant;
        this.plantVariant = plantVariant;
    }
}

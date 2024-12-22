package agh.ics.oop.model.configuration;

public record WorldMapConfiguration(
    int height,
    int width,
    WorldMapVariant mapVariant
) {
}

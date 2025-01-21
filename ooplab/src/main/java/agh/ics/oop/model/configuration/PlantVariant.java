package agh.ics.oop.model.configuration;

public enum PlantVariant {
    NONE("None"),
    FORESTED_EQUATORS("Forest Equators");

    private final String displayText;

    PlantVariant(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}

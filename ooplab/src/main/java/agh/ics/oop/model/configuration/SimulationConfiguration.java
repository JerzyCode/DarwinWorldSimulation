package agh.ics.oop.model.configuration;

public class SimulationConfiguration {
    private final int daysCount;
    private final int wellFedEnergy;
    private final int lossCopulateEnergy;
    private final int startAnimalCount;

    SimulationConfiguration(int daysCount, int wellFedEnergy, int lossCopulateEnergy, int startAnimalCount) {
        this.daysCount = daysCount;
        this.wellFedEnergy = wellFedEnergy;
        this.lossCopulateEnergy = lossCopulateEnergy;
        this.startAnimalCount = startAnimalCount;
    }

    public int getWellFedEnergy() {
        return wellFedEnergy;
    }

    public int getLossCopulateEnergy() {
        return lossCopulateEnergy;
    }

    public int getStartAnimalCount() {
        return startAnimalCount;
    }


    public int getDaysCount() {
        return daysCount;
    }


    @Override
    public String toString() {
        return "SimulationConfiguration{" +
                ", wellFedEnergy=" + wellFedEnergy +
                ", lossCopulateEnergy=" + lossCopulateEnergy +
                ", startAnimalCount=" + startAnimalCount +
                '}';
    }
}

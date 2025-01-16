package agh.ics.oop.model.configuration;

public class SimulationConfigurationBuilder {
    private int daysCount;
    private int wellFedEnergy;
    private int lossCopulateEnergy;
    private int startAnimalCount;


    private SimulationConfigurationBuilder() {

    }

    public static SimulationConfigurationBuilder create() {
        return new SimulationConfigurationBuilder();
    }

    public SimulationConfigurationBuilder daysCount(int daysCount) {
        this.daysCount = daysCount;
        return this;
    }


    public SimulationConfigurationBuilder wellFedEnergy(int wellFedEnergy) {
        this.wellFedEnergy = wellFedEnergy;
        return this;
    }

    public SimulationConfigurationBuilder lossCopulateEnergy(int lossCopulateEnergy) {
        this.lossCopulateEnergy = lossCopulateEnergy;
        return this;
    }


    public SimulationConfigurationBuilder startAnimalCount(int startAnimalCount) {
        this.startAnimalCount = startAnimalCount;
        return this;
    }


    public SimulationConfiguration build() {
        return new SimulationConfiguration(daysCount, wellFedEnergy, lossCopulateEnergy, startAnimalCount);
    }
}

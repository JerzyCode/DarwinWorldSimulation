package agh.ics.oop.model.configuration;

public class SimulationConfigurationBuilder {
    private int daysCount;
    private int energyGain;
    private int plantGrowth;
    private PlantVariant plantVariant;
    private int wellFedEnergy;
    private int lossCopulateEnergy;
    private int startAnimalCount;
    private int startPlantCount;


    private SimulationConfigurationBuilder() {

    }

    public static SimulationConfigurationBuilder create() {
        return new SimulationConfigurationBuilder();
    }

    public SimulationConfigurationBuilder daysCount(int daysCount) {
        this.daysCount = daysCount;
        return this;
    }

    public SimulationConfigurationBuilder energyGain(int energyGain) {
        this.energyGain = energyGain;
        return this;
    }

    public SimulationConfigurationBuilder plantGrowth(int plantGrowth) {
        this.plantGrowth = plantGrowth;
        return this;
    }

    public SimulationConfigurationBuilder plantVariant(PlantVariant plantVariant) {
        this.plantVariant = plantVariant;
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

    public SimulationConfigurationBuilder startPlantCount(int startPlantCount) {
        this.startPlantCount = startPlantCount;
        return this;
    }

    public SimulationConfiguration build() {
        return new SimulationConfiguration(daysCount, energyGain, plantGrowth, plantVariant, wellFedEnergy,
                lossCopulateEnergy, startAnimalCount, startPlantCount);
    }
}

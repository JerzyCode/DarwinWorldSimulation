package agh.ics.oop.model.configuration;

public class SimulationConfiguration {
    private int daysCount;
    private int energyGain;
    private int plantGrowth;
    private PlantVariant plantVariant;
    private int wellFedEnergy;
    private int lossCopulateEnergy;
    private int fireFrequency;
    private int fireDuration;
    private int startAnimalCount;
    private int startPlantCount;

    SimulationConfiguration() {
        this(200, 15, 2, PlantVariant.NONE, 5, 2, 0, 0, 15, 20);
    }

    public SimulationConfiguration(int daysCount, int energyGain, int plantGrowth, PlantVariant plantVariant,
                                   int wellFedEnergy, int lossCopulateEnergy, int fireFrequency, int fireDuration, int startAnimalCount, int startPlantCount) {
        this.daysCount = daysCount;
        this.energyGain = energyGain;
        this.plantGrowth = plantGrowth;
        this.plantVariant = plantVariant;
        this.wellFedEnergy = wellFedEnergy;
        this.lossCopulateEnergy = lossCopulateEnergy;
        this.fireFrequency = fireFrequency;
        this.fireDuration = fireDuration;
        this.startAnimalCount = startAnimalCount;
        this.startPlantCount = startPlantCount;
    }

    public int getEnergyGain() {
        return energyGain;
    }

    public void setEnergyGain(int energyGain) {
        this.energyGain = energyGain;
    }

    public int getPlantGrowth() {
        return plantGrowth;
    }

    public void setPlantGrowth(int plantGrowth) {
        this.plantGrowth = plantGrowth;
    }

    public PlantVariant getPlantVariant() {
        return plantVariant;
    }

    public void setPlantVariant(PlantVariant plantVariant) {
        this.plantVariant = plantVariant;
    }

    public int getWellFedEnergy() {
        return wellFedEnergy;
    }

    public void setWellFedEnergy(int wellFedEnergy) {
        this.wellFedEnergy = wellFedEnergy;
    }

    public int getLossCopulateEnergy() {
        return lossCopulateEnergy;
    }

    public void setLossCopulateEnergy(int lossCopulateEnergy) {
        this.lossCopulateEnergy = lossCopulateEnergy;
    }

    public int getFireFrequency() {
        return fireFrequency;
    }

    public void setFireFrequency(int fireFrequency) {
        this.fireFrequency = fireFrequency;
    }

    public int getFireDuration() {
        return fireDuration;
    }

    public void setFireDuration(int fireDuration) {
        this.fireDuration = fireDuration;
    }

    public int getStartAnimalCount() {
        return startAnimalCount;
    }

    public void setStartAnimalCount(int startAnimalCount) {
        this.startAnimalCount = startAnimalCount;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public int getStartPlantCount() {
        return startPlantCount;
    }

    public void setStartPlantCount(int startPlantCount) {
        this.startPlantCount = startPlantCount;
    }

    @Override
    public String toString() {
        return "SimulationConfiguration{" +
                "energyGain=" + energyGain +
                ", plantGrowth=" + plantGrowth +
                ", plantVariant=" + plantVariant +
                ", wellFedEnergy=" + wellFedEnergy +
                ", lossCopulateEnergy=" + lossCopulateEnergy +
                ", fireFrequency=" + fireFrequency +
                ", startAnimalCount=" + startAnimalCount +
                ", startPlantCount=" + startPlantCount +
                '}';
    }
}

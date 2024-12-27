package agh.ics.oop.model.configuration;

public class SimulationConfiguration {
  private int energyGain;
  private int plantGrowth;
  private PlantVariant plantVariant;
  private int wellFedEnergy;
  private int lossCopulateEnergy;
  private int fireFrequency;
  private int startAnimalCount;

  public SimulationConfiguration(int energyGain, int plantGrowth, PlantVariant plantVariant,
      int wellFedEnergy, int lossCopulateEnergy, int fireFrequency, int startAnimalCount) {
    this.energyGain = energyGain;
    this.plantGrowth = plantGrowth;
    this.plantVariant = plantVariant;
    this.wellFedEnergy = wellFedEnergy;
    this.lossCopulateEnergy = lossCopulateEnergy;
    this.fireFrequency = fireFrequency;
    this.startAnimalCount = startAnimalCount;
  }

  private SimulationConfiguration() {

  }

  static SimulationConfiguration getDefault() {
    var defaultConfiguration = new SimulationConfiguration();
    defaultConfiguration.energyGain = 2;
    defaultConfiguration.plantGrowth = 0;
    defaultConfiguration.plantVariant = PlantVariant.FORESTED_EQUATORS;
    defaultConfiguration.wellFedEnergy = 5;
    defaultConfiguration.lossCopulateEnergy = 2;
    defaultConfiguration.fireFrequency = 0;
    defaultConfiguration.startAnimalCount = 3;

    return defaultConfiguration;
  }

  public int getEnergyGain() {
    return energyGain;
  }

  public int getPlantGrowth() {
    return plantGrowth;
  }

  public PlantVariant getPlantVariant() {
    return plantVariant;
  }

  public int getWellFedEnergy() {
    return wellFedEnergy;
  }

  public int getLossCopulateEnergy() {
    return lossCopulateEnergy;
  }

  public int getFireFrequency() {
    return fireFrequency;
  }

  public int getStartAnimalCount() {
    return startAnimalCount;
  }

  public void setEnergyGain(int energyGain) {
    this.energyGain = energyGain;
  }

  public void setPlantGrowth(int plantGrowth) {
    this.plantGrowth = plantGrowth;
  }

  public void setPlantVariant(PlantVariant plantVariant) {
    this.plantVariant = plantVariant;
  }

  public void setWellFedEnergy(int wellFedEnergy) {
    this.wellFedEnergy = wellFedEnergy;
  }

  public void setLossCopulateEnergy(int lossCopulateEnergy) {
    this.lossCopulateEnergy = lossCopulateEnergy;
  }

  public void setFireFrequency(int fireFrequency) {
    this.fireFrequency = fireFrequency;
  }

  public void setStartAnimalCount(int startAnimalCount) {
    this.startAnimalCount = startAnimalCount;
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
        '}';
  }
}

package agh.ics.oop.presenter;

import agh.ics.oop.model.elements.Animal;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

public class AnimalStatisticsView {
    @FXML
    private Label genomeLabel;
    @FXML
    private Label activeGeneLabel;
    @FXML
    private Label energyLabel;
    @FXML
    private Label eatenPlantsLabel;
    @FXML
    private Label childrenLabel;
    @FXML
    private Label descendantsLabel;
    @FXML
    private Label lifespanLabel;
    @FXML
    private Label deathDayLabel;

    @Setter
    private Animal animal;

    public void updateLabels(int currentDay) {
        if (animal == null) {
            return;
        }

        genomeLabel.setText(animal.getGenome().getGens().toString());
        activeGeneLabel.setText(animal.getActivatedGen().toString());
        energyLabel.setText(String.format("%d", animal.getEnergy()));
        eatenPlantsLabel.setText(String.format("%d", animal.getCountOfEatenPlants()));
        childrenLabel.setText(String.format("%d", animal.getCountOfChildren()));
        descendantsLabel.setText(String.format("%d", animal.getCountOfDescendants()));
        if (animal.isDead()) {
            lifespanLabel.setText("Animal is dead");
            deathDayLabel.setText(String.format("%d", animal.getEndDay()));
            animal = null;
        } else {
            lifespanLabel.setText(String.format("%d", currentDay - animal.getStartDay() + 1));
            deathDayLabel.setText("Animal is alive");
        }
    }
}

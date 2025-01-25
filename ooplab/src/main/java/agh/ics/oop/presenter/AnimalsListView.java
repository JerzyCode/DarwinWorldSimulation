package agh.ics.oop.presenter;

import agh.ics.oop.model.elements.Animal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import lombok.Setter;

import java.util.Set;

public class AnimalsListView {

    @FXML
    private ListView<Animal> animalsListView;

    @FXML
    private Button selectButton;

    @Setter
    private SimulationPresenter presenter;

    public void initialize() {
        animalsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Animal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        selectButton.setOnAction(event -> onAnimalSelected());
    }

    public void setAnimals(Set<Animal> animals) {
        if (animalsListView != null) {
            animalsListView.getItems().setAll(animals);
        }
    }

    private void onAnimalSelected() {
        Animal selectedAnimal = animalsListView.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            presenter.selectAnimal(selectedAnimal);
        }
    }

}

package agh.ics.oop.presenter;

import agh.ics.oop.model.elements.Animal;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.Setter;

import java.util.Set;

public class AnimalListView {

    @FXML
    private ListView<Animal> animalCells;

    @FXML
    private Button cancelButton;

    @Setter
    private SimulationPresenter presenter;

    @FXML
    public void initialize() {
        animalCells.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Animal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());

                setOnMouseClicked(event -> onAnimalSelected());
                setOnMouseEntered(event -> {
                    setStyle("-fx-background-color: lightblue;");
                    setCursor(Cursor.HAND);
                });
                setOnMouseExited(event -> setStyle(""));
            }
        });

        cancelButton.setOnAction(event -> presenter.onCancelTrackingAnimal());
    }

    public void setAnimals(Set<Animal> animals) {
        if (animalCells != null) {
            animalCells.getItems().setAll(animals);
        }
    }

    private void onAnimalSelected() {
        Animal selectedAnimal = animalCells.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            presenter.selectAnimal(selectedAnimal);
            presenter.displayAnimalStatistics();
        }
    }

}

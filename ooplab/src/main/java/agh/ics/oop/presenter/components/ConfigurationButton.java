package agh.ics.oop.presenter.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class ConfigurationButton extends HBox {

    public ConfigurationButton(String saveName, EventHandler<MouseEvent> onLoadHandler, EventHandler<ActionEvent> onDeleteHandler) {
        this.setAlignment(Pos.CENTER_LEFT);
        var clickablePane = createClickablePane(saveName, onLoadHandler);
        this.setStyle("-fx-border-color: black;");


        var deleteButton = createDeleteButton(event -> {
            onDeleteHandler.handle(event);
            ((Pane) this.getParent()).getChildren().remove(this);
        });


        this.getChildren().addAll(clickablePane, deleteButton);
        HBox.setHgrow(clickablePane, Priority.ALWAYS);
    }

    private Pane createClickablePane(String saveName, EventHandler<MouseEvent> onLoadHandler) {
        Pane clickablePane = new Pane();
        clickablePane.setPrefHeight(30);
        clickablePane.setPrefWidth(200);
        Label saveNameLabel = new Label(saveName);
        saveNameLabel.setStyle("-fx-padding: 5;");
        clickablePane.getChildren().add(saveNameLabel);

        clickablePane.setOnMouseEntered(event -> clickablePane.setStyle("-fx-background-color: lightblue;"));
        clickablePane.setOnMouseExited(event -> clickablePane.setStyle(""));
        clickablePane.setOnMousePressed(event -> clickablePane.setStyle("-fx-background-color: deepskyblue;"));
        clickablePane.setOnMouseReleased(event -> clickablePane.setStyle("-fx-background-color: lightblue;"));
        clickablePane.setOnMouseClicked(onLoadHandler);
        clickablePane.setCursor(Cursor.HAND);

        return clickablePane;
    }

    private Button createDeleteButton(EventHandler<ActionEvent> onDeleteHandler) {
        Button deleteButton = new Button("X");
        deleteButton.setPrefHeight(30);
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(onDeleteHandler);
        deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;"));
        deleteButton.setOnMouseExited(event -> deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"));
        deleteButton.setOnMousePressed(event -> deleteButton.setStyle("-fx-background-color: firebrick; -fx-text-fill: white;"));
        deleteButton.setOnMouseReleased(event -> deleteButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;"));
        deleteButton.setCursor(Cursor.HAND);

        return deleteButton;
    }
}

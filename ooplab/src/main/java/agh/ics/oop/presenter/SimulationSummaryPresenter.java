package agh.ics.oop.presenter;

import agh.ics.oop.model.statistics.GraphData;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class SimulationSummaryPresenter {

    @FXML
    private NumberAxis yAxis;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private LineChart<Number, Number> chart;


    public void setGraphData(List<GraphData> graphDataList) {
        XYChart.Series<Number, Number> animalSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> plantSeries = new XYChart.Series<>();

        animalSeries.setName("Animal Count");
        plantSeries.setName("Plant Count");

        for (GraphData data : graphDataList) {
            animalSeries.getData().add(new XYChart.Data<>(data.getCurrentDay(), data.getAnimalCount()));
            plantSeries.getData().add(new XYChart.Data<>(data.getCurrentDay(), data.getPlantCount()));
        }

        chart.getData().add(animalSeries);
        chart.getData().add(plantSeries);

        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);

        for (XYChart.Series<Number, Number> series : chart.getData()) {
            for (XYChart.Data<Number, Number> data : series.getData()) {
                Node node = data.getNode();
                if (node != null) {
                    node.setStyle("-fx-background-radius: 1px; -fx-padding: 1px;");
                }
            }
        }
    }

}


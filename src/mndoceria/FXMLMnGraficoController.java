package mndoceria;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class FXMLMnGraficoController implements Initializable {

	@FXML
	BarChart<String, Number> Grafico;
	
	@FXML
	CategoryAxis gMeses;
	
	@FXML
	NumberAxis gVendas;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<Integer> dados = new ArrayList<Integer>();
		dados.addAll(getDados());
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		
		for (Integer i = 0; i < dados.size();i+=2) {
			series.getData().add(new XYChart.Data<String, Number>(String.valueOf(dados.get(i)), dados.get(i+1)));
		}
		Grafico.getData().add(series);
	}

	
	@FXML
	private void Voltar(ActionEvent event) throws IOException {
	        Parent root = FXMLLoader.load(getClass().getResource("/mndoceria/MnDoceria_Vendas.fxml"));
	        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        
	        Scene scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
	    }
	private ArrayList<Integer> getDados() {
		SqlConnection conn = new SqlConnection();
		
		try {
			return conn.GetVendasMes();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}

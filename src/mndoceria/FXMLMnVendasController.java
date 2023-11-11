/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package mndoceria;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class FXMLMnVendasController implements Initializable{
    private Stage stage;
    private Parent root;
    
    @FXML
    private Label lblPedido;
    
    @FXML
    Button btnGrafico;
    
	@FXML
	Button btnDelete;
	
    @FXML
    private TableView<PedidosModel> tbData;
    
    @FXML
    public TableColumn<PedidosModel, Integer> idpedido;

    @FXML
    public TableColumn<PedidosModel, String> total;

    @FXML
    public TableColumn<PedidosModel, String> data;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    
    public void initialize(URL location, ResourceBundle resources) {

        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        idpedido.setCellValueFactory(new PropertyValueFactory<PedidosModel, Integer>("Idpedido"));
        total.setCellValueFactory(new PropertyValueFactory<PedidosModel, String>("Total"));
        data.setCellValueFactory(new PropertyValueFactory<PedidosModel, String>("Data"));
        //add your data to the table here.
        refreshTable();
        tbData.setItems(PedidosList);
    }

 
	// add your data here from any source 
    private ObservableList<PedidosModel> PedidosList = FXCollections.observableArrayList(
      //      new PedidosModel(1,"R$20,00", "2018-12-27"),
      //     new PedidosModel(2,"R$20,00", "2018-12-27")
    );
    
	@FXML
	private void Delete() {
		SqlConnection conn = new SqlConnection();
		try {
			conn.DeletePedido(tbData.getSelectionModel().getSelectedItem().getIdpedido());
			JOptionPane.showMessageDialog(null, "Pedido Excluído com sucesso!");
			refreshTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    
    @FXML
    private void refreshTable(){
    	PedidosList.clear();
    	conn = SqlConnection.getConnected();
    	
    	
    	try {
    		ps = conn.prepareStatement("SELECT * FROM PEDIDOS");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				PedidosList.add(new PedidosModel(
						rs.getInt("ID_PEDIDO"),
						rs.getString("VL_PEDIDO"),
						rs.getString("DT_PEDIDO")
						));
				
		        tbData.setItems(PedidosList);
				
			}
			
			
		} catch (SQLException e) {
			System.out.println("Erro no Método refreshTable");
		}
    	finally {
    		try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    };
    
    
    
    @FXML
    public void clickItem(MouseEvent event) throws IOException
    {
        if (event.getClickCount() == 2) //Checking double click
        {
        	Integer idpedidoselecionado = tbData.getSelectionModel().getSelectedItem().getIdpedido();
        	String totalpedido = tbData.getSelectionModel().getSelectedItem().getTotal();
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("MnDoceria_VendasEdit.fxml"));
        	root = loader.load();
        	
        	FXMLMnVendasEditController VendasEditController = loader.getController();
        	
        	VendasEditController.mostrarPedido(idpedidoselecionado, totalpedido);
        	VendasEditController.CarregarPedido(idpedidoselecionado);
            //Parent root = FXMLLoader.load(getClass().getResource("/mndoceria/MnDoceria_VendasEdit.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            
            
            
            
           // System.out.println(tbData.getSelectionModel().getSelectedItem().getData());
        }
    }
    
    
    @FXML
    private void ButtonAdd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mndoceria/MnDoceria_VendasEdit.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }
    
    @FXML
    private void ButtonGrafico(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mndoceria/MnDoceria_Grafico.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

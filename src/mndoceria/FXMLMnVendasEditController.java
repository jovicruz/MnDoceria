/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package mndoceria;
import mndoceria.PedidosModel;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class FXMLMnVendasEditController implements Initializable{
    private Stage stage;
    private Parent root;
	
    @FXML
    ComboBox<String> cbBairros;
    
    @FXML
    Label lblEntrega;
    
	@FXML
	Label lblPedido;
	
	@FXML
	Label lblTotal;
	
	@FXML
	Label lblSubtotal;	
	
	@FXML
	Button btnSalvar;
	
	@FXML
	Button btnAddProduto;
	
	@FXML
	Button btnVoltar;
	
	@FXML
	Label lblTotalP1;
	
	@FXML
	GridPane gpProdutos;
	
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	Connection conn = null;		
	int idproduto = 0;
	int qntproduto = 0;
	double taxabairro = 0;
	double vlproduto = 0;
	ObservableList<String> ProdutosList = FXCollections.observableArrayList();
	ObservableList<String> BairrosList = FXCollections.observableArrayList();
	
	public void initialize(URL location, ResourceBundle resources) {
		getProdutos();
		getBairros();
		//AddProduto();
	}
    
	
	@FXML
	private void clickCbBairros() {
		UpdateEntrega(cbBairros.getValue());
		lblEntrega.setText(String.valueOf(taxabairro));
	}
	
	private void UpdateEntrega(String nmbairro) {
		SqlConnection conn = new SqlConnection();
		try {
			taxabairro = conn.GetTaxaBairro(nmbairro);
			AtualizarSubtotal();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void UpdateValores(ComboBox<String> cbProduto, ComboBox<Integer> cbQnt, Label lbl) {
		SqlConnection conn = new SqlConnection();
		try {
			if(cbProduto.getValue() != null && cbQnt.getValue() != null) {
			vlproduto = conn.getValorProduto(cbProduto.getValue());
			qntproduto = cbQnt.getValue();
			
			vlproduto = (vlproduto * qntproduto);
			
			lbl.setText(Double.toString(vlproduto));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void AtualizarSubtotal() {
		Double subtotal = 0.0;

		for (Node node : gpProdutos.getChildren()) {
		    if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == 2) {
		        if (node instanceof Label) {
		            Label label = (Label) node;
		            if(label.getText() != "") {
		            	
		            	subtotal = subtotal + Double.parseDouble(label.getText());
		            }
		        }
		    }
		}
		lblSubtotal.setText("Subtotal: R$" + subtotal);
		lblTotal.setText(Double.toString(subtotal+taxabairro));
	}
	
	public void CarregarPedido(Integer idpedido) {
		ArrayList<String> produtos = new ArrayList<>();
		ArrayList<Integer> quantidade = new ArrayList<>();
		SqlConnection conn = new SqlConnection();
		
		try {
			produtos.addAll(conn.GetDetalhesProdutos(idpedido));
			quantidade.addAll(conn.GetDetalhesQuantidade(idpedido));
			cbBairros.setValue(conn.GetNmBairro(idpedido));
			
			for (Integer i = 0; i < produtos.size();i++) {
				ComboBox<String> cbProdutos = new ComboBox<String>(ProdutosList);
				ComboBox<Integer> cbQnt = new ComboBox<Integer>();
				
				Label lblTotalP = new Label();
				
				cbQnt.getItems().addAll(gerador());
				cbQnt.getItems().remove(0);
				cbQnt.setValue(quantidade.get(i));
				
				gpProdutos.addRow(gpProdutos.getRowCount(), cbProdutos, cbQnt, lblTotalP);
				cbProdutos.setValue(produtos.get(i));
				UpdateValores(cbProdutos, cbQnt, lblTotalP);
				
				
				cbProdutos.setOnAction(e -> {
					UpdateValores(cbProdutos, cbQnt, lblTotalP);
					AtualizarSubtotal();
				});
				cbQnt.setOnAction(e -> {
					UpdateValores(cbProdutos, cbQnt, lblTotalP);
					AtualizarSubtotal();
				});
			}
			clickCbBairros();
			btnSalvar.setDisable(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void Salvar() {
		SqlConnection conn = new SqlConnection();
		try {
			ArrayList<String> produtos = new ArrayList<>();
			ArrayList<Integer> quantidades = new ArrayList<>();
			ArrayList<Integer> idprodutos = new ArrayList<>();
			Integer idbairro = conn.getIdBairro(cbBairros.getValue());
				
			for (Node node : gpProdutos.getChildren()) {
			    if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == 0) {
			        if (node instanceof ComboBox) {
			            @SuppressWarnings("unchecked")
						ComboBox<String> cb = (ComboBox<String>) node;
			            
			            produtos.add(cb.getValue());
			        }
			    }
			}
			
			for (Node node : gpProdutos.getChildren()) {
			    if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == 1) {
			        if (node instanceof ComboBox) {
			            @SuppressWarnings("unchecked")
						ComboBox<Integer> cbi = (ComboBox<Integer>) node;
			            
			            quantidades.add(cbi.getValue());
			        }
			    }
			    
			}

			System.out.println("Tamanho lista " + produtos.size());

			for (Integer i = 0; i < produtos.size(); i++) {
				idprodutos.add(conn.getIdProduto(produtos.get(i)));
	        }
			
			
			conn.SalvarPedido(idbairro, LocalDateTime.now().toLocalDate(), Double.parseDouble(lblTotal.getText()), idprodutos, quantidades);
			JOptionPane.showMessageDialog(null, "Pedido Salvo com Sucesso!");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Erro ao salvar o pedido! \nVerifique se as entradas estão corretas!");
		}
	}
	
	private Integer[] gerador() {
		Integer[] resultado = new Integer[20];
		for(int i=1; i < 20; i++) {
			resultado[i] = i;
		}
		return resultado;
	}
	
	@FXML
	private void AddProduto() {
		
		ComboBox<String> cbProdutos1 = new ComboBox<String>(ProdutosList);
		ComboBox<Integer> cbQnt1 = new ComboBox<Integer>();
		Label lblTotalP2 = new Label();
		
		cbQnt1.getItems().addAll(gerador());
		cbQnt1.getItems().remove(0);
		cbQnt1.setValue(1);		
		gpProdutos.addRow(gpProdutos.getRowCount(), cbProdutos1, cbQnt1, lblTotalP2);
		
		
		
		
		cbProdutos1.setId(Integer.toString(gpProdutos.getRowCount()));
		cbQnt1.setId(Integer.toString(gpProdutos.getRowCount()));
		lblTotalP2.setId("lblTotalPedido" + Integer.toString(gpProdutos.getRowCount()));
		cbQnt1.setDisable(true);
		

		cbProdutos1.setOnAction(e -> {
			UpdateValores(cbProdutos1, cbQnt1, lblTotalP2);
			AtualizarSubtotal();
			btnSalvar.setDisable(false);
			cbQnt1.setDisable(false);
		});
		cbQnt1.setOnAction(e -> {
			UpdateValores(cbProdutos1, cbQnt1, lblTotalP2);
			AtualizarSubtotal();
			btnSalvar.setDisable(false);
		});
	};
	
	
	
	private void getProdutos(){
    	ProdutosList.clear();
    	conn = SqlConnection.getConnected();
    	
    	
    	try {
    		ps = conn.prepareStatement("SELECT * FROM PRODUTOS");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ProdutosList.addAll(rs.getString("NM_PRODUTO"));
				
		        //cbProdutos.setItems(ProdutosList);
				
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
    
    
    private void getBairros(){
    	BairrosList.clear();
    	conn = SqlConnection.getConnected();
    	
    	
    	try {
    		ps = conn.prepareStatement("SELECT * FROM BAIRROS");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				BairrosList.addAll(rs.getString("NM_BAIRRO"));
				
			}
			cbBairros.setItems(BairrosList);
			
		} catch (SQLException e) {
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
    private void Voltar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mndoceria/MnDoceria_Vendas.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
	
	void mostrarPedido(Integer idpedido, String totalpedido) {
		//String npedido = Integer.toString(idpedido);
		lblTotal.setText(totalpedido);
	}
	}


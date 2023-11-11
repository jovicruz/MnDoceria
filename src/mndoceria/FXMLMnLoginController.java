/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package mndoceria;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
/**
 *
 * @author user
 */
public class FXMLMnLoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    TextField txtUsuario;
    
    @FXML
    TextField txtSenha;
    
    @FXML
    private void ButtonLogin(ActionEvent event) throws IOException {
    	SqlConnection conn = new SqlConnection();
    	try {
			if(conn.logar(txtUsuario.getText(), txtSenha.getText()) == true) {
				Parent root = FXMLLoader.load(getClass().getResource("/mndoceria/MnDoceria_Vendas.fxml"));
			    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			    
			    Scene scene = new Scene(root);
			    stage.setScene(scene);
			    stage.show();
			}
			else {
				JOptionPane.showMessageDialog(null, "Usuário ou Senha errados! ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
                
        
       // scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
         //   public void handle(MouseEvent event) {
           //     System.out.println("mouse click detected! "+event.getSource());
          //  }
        //});
    }
}

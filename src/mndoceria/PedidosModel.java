package mndoceria;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PedidosModel {

    private Integer idpedido;
    private String total;
    private String data;
    
    public PedidosModel(Integer idpedido, String total, String data) {
        this.idpedido = idpedido; 
        this.total = total;
        this.data = data;
    }

	public Integer getIdpedido() {
		return idpedido;
	}

	public void setIdpedido(Integer idpedido) {
		this.idpedido = idpedido;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}


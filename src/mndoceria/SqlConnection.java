package mndoceria;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
public class SqlConnection {
	
	static Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	
	public String GetNmBairro(Integer idpedido) throws SQLException{
		conn = getConnected();
		try {
			
			
			String sql = "SELECT NM_BAIRRO FROM PEDIDOS JOIN BAIRROS on BAIRROS.ID_BAIRRO = PEDIDOS.ID_BAIRRO WHERE ID_PEDIDO = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, idpedido);	
			rs = ps.executeQuery();
			rs.next();
			
			return 	rs.getString("NM_BAIRRO");
		} catch (SQLException e) {
			return "";
		}
		finally {
			if (conn != null){
	            conn.close();
			}
		}
		       }

	
	public Double GetTaxaBairro(String nmbairro) throws SQLException{
		conn = getConnected();
		try {
			String sql = "SELECT V_ENTREGA FROM Bairros WHERE NM_BAIRRO = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, nmbairro);	
			rs = ps.executeQuery();
			
			rs.next();
			
			return Double.parseDouble(rs.getString("V_ENTREGA"));
		} catch (SQLException e) {
			return (double) -1;
		}
		finally {
			if (conn != null){
	            conn.close();
			}
		}
		       }

	
	public Integer getIdBairro(String nmbairro) throws SQLException{
		conn = getConnected();
		try {
			rs = conn.createStatement().executeQuery("SELECT ID_BAIRRO FROM BAIRROS WHERE NM_BAIRRO = '" +nmbairro+ "'");
			rs.next();
		    return Integer.parseInt(rs.getString("ID_BAIRRO"));
		} catch (SQLException e) {
			System.out.print(rs.getString("ERRO METODO getVLProduto"));
			return -1;
		}
		finally {
			if (conn != null){
	            conn.close();
			}
		}
		       }

	
public static Connection getConnected() {
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/mndb","root", "root");

		} catch (SQLException e) {
			System.out.println("Erro na conexão com o banco de dados! ");
		}
	} catch (ClassNotFoundException e) {
		System.out.println("Driver de banco de dados não localizado! ");
	}
	return conn;
}

public boolean logar(String usuario, String senha) throws SQLException{
	try {
		conn = getConnected();
		rs = conn.createStatement().executeQuery("SELECT * FROM USUARIOS WHERE NM_USUARIO = '" +usuario+ "' AND SENHA_USUARIO = '" +senha+ "'");
		rs.next();
		if(rs.getString("ID_USUARIO") == null) {
			return false;
		}
		else {
			return true;
		}
	} catch (SQLException e) {
		return false;
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	
}


public Integer getIdProduto(String nmproduto) throws SQLException{
			try {
				conn = getConnected();
				rs = conn.createStatement().executeQuery("SELECT * FROM PRODUTOS WHERE NM_PRODUTO = '" +nmproduto+ "'");
				rs.next();
	            return Integer.parseInt(rs.getString("ID_PRODUTO"));
			} catch (SQLException e) {
				return -1;
			}
			finally {
				if (conn != null){
		            conn.close();
				}
			}
			
		}


public Double getValorProduto(String nmproduto) throws SQLException{
	conn = getConnected();
	try {
		rs = conn.createStatement().executeQuery("SELECT * FROM PRODUTOS WHERE NM_PRODUTO = '" +nmproduto+ "'");
		rs.next();
		//System.out.print(rs.getString("VL_PRODUTO"));
	    return Double.parseDouble(rs.getString("VL_PRODUTO"));
	} catch (SQLException e) {
		System.out.print(rs.getString("ERRO METODO getVLProduto"));
		return (double) -1;
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	       }




public void SalvarPedido(Integer idbairro, LocalDate data, Double valor, ArrayList<Integer> idprodutos, ArrayList<Integer> quantidades) throws SQLException{
	conn = getConnected();
	try {
		String sql = "INSERT INTO PEDIDOS (ID_BAIRRO, DT_PEDIDO, VL_PEDIDO, DS_PEDIDO, DS_ENDERECO) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, idbairro);
		ps.setDate(2, Date.valueOf(data));
		ps.setDouble(3, valor);
		ps.setString(4, "TOME");
		ps.setString(5, "Rua tal, nº1");
		
		ps.executeUpdate();
		
		
		rs = conn.createStatement().executeQuery("SELECT MAX(ID_PEDIDO) FROM PEDIDOS");
		rs.next();
		
		
		for (Integer i = 0; i < idprodutos.size(); i++) {
			SalvarDetalhes(Integer.parseInt(rs.getString("MAX(ID_PEDIDO)")), idprodutos.get(i), quantidades.get(i));
        }
		
		
		
		
	} catch (SQLException e) {
		System.out.print(e);
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	       }


public void SalvarDetalhes(Integer idpedido, Integer idproduto, Integer quantidade) throws SQLException{
	conn = getConnected();
	try {
		
		String sql = "INSERT INTO DetalhesVendas (ID_PEDIDO, ID_PRODUTO, QNT) VALUES (?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, idpedido);
		ps.setInt(2, idproduto);
		ps.setInt(3, quantidade);
		
		ps.executeUpdate();
		
		
	} catch (SQLException e) {
		System.out.print(e);
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	       }

public void AtualizarPedido(Integer idbairro, LocalDate data, Double valor, ArrayList<Integer> idprodutos, ArrayList<Integer> quantidades, Integer idpedido) throws SQLException{
	conn = getConnected();
	try {
		String sql = "UPDATE PEDIDOS SET ID_BAIRRO = ?, DT_PEDIDO = ?, VL_PEDIDO = ?, DS_PEDIDO = ?, DS_ENDERECO = ? WHERE ID_PEDIDO = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, idbairro);
		ps.setDate(2, Date.valueOf(data));
		ps.setDouble(3, valor);
		ps.setString(4, "TOME");
		ps.setString(5, "Rua tal, nº1");
		ps.setInt(6, idpedido);
		
		ps.executeUpdate();
		
		ps = conn.prepareStatement("DELETE FROM DetalhesVendas WHERE ID_PEDIDO = ?");
		ps.setInt(1, idpedido);
		ps.executeUpdate();
		
		rs = conn.createStatement().executeQuery("SELECT MAX(ID_PEDIDO) FROM PEDIDOS");
		rs.next();
		
		
		for (Integer i = 0; i < idprodutos.size(); i++) {
			SalvarDetalhes(idpedido, idprodutos.get(i), quantidades.get(i));
        }
		
		
		
		
	} catch (SQLException e) {
		System.out.print(e);
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	       }



public ArrayList<String> GetDetalhesProdutos(Integer idpedido) throws SQLException{
	conn = getConnected();
	ArrayList<String> produtos = new ArrayList<>();
	try {
		
		
		String sql = "SELECT NM_PRODUTO, ID_DETALHE FROM DetalhesVendas JOIN PRODUTOS on detalhesvendas.ID_PRODUTO = PRODUTOS.ID_PRODUTO WHERE ID_PEDIDO = ? ORDER BY ID_DETALHE asc";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, idpedido);	
		rs = ps.executeQuery();
		
		while(rs.next()){
			produtos.add(rs.getString("NM_PRODUTO"));
		}
		return produtos;
	} catch (SQLException e) {
		return produtos;
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	       }

public ArrayList<Integer> GetDetalhesQuantidade(Integer idpedido) throws SQLException{
	conn = getConnected();
	ArrayList<Integer> produtos = new ArrayList<>();
	try {
		
		
		String sql = "SELECT QNT FROM DetalhesVendas WHERE ID_PEDIDO = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, idpedido);	
		rs = ps.executeQuery();
		
		while(rs.next()){
			produtos.add(Integer.parseInt(rs.getString("QNT")));
		}
		return produtos;
	} catch (SQLException e) {
		return produtos;
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	       }

public void DeletePedido(Integer idpedido) throws SQLException{
	conn = getConnected();
	try {
		
		String sql = "DELETE FROM PEDIDOS WHERE ID_PEDIDO = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, idpedido);
		
		ps.executeUpdate();
		
		
	} catch (SQLException e) {
		System.out.print(e);
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	       }


public ArrayList<Integer> GetVendasMes() throws SQLException{
	conn = getConnected();
	ArrayList<Integer> dados = new ArrayList<>();
	try {
		
		
		String sql = "SELECT YEAR(DT_PEDIDO) AS Ano, MONTH(DT_PEDIDO) AS Mes, COUNT(*) AS Vendas FROM PEDIDOS GROUP BY YEAR(DT_PEDIDO), MONTH(DT_PEDIDO) ORDER BY ano, mes";
		PreparedStatement ps = conn.prepareStatement(sql);
			
		rs = ps.executeQuery();
		
		while(rs.next()){
			//dados.add(Integer.parseInt(rs.getString("Ano")));
			dados.add(Integer.parseInt(rs.getString("Mes")));
			dados.add(Integer.parseInt(rs.getString("Vendas")));
		}
		return dados;
	} catch (SQLException e) {
		return dados;
	}
	finally {
		if (conn != null){
            conn.close();
		}
	}
	       }




	public void Connection() throws SQLException {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost/mndb","root", "root");
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM PEDIDOS");
				

				while (rs.next()) {
				           System.out.print(rs.getString("DT_PEDIDO"));
				       }


				
				
			} catch (SQLException e) {
				System.out.println("Erro na conexão com o banco de dados! ");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Driver de banco de dados não localizado! ");
		}
		finally {
			if (conn != null){
				conn.close();
			}
		}
	}
}

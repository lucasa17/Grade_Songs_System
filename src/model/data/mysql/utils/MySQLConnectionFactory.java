package model.data.mysql.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.ModelException;

public class MySQLConnectionFactory {
	// Driver
	private static final String DRIVER_NAME = 
			"com.mysql.cj.jdbc.Driver";

	// URL de conexão
	private static final String DB_URL = 
			"jdbc:mysql://127.0.0.1/gradesystem";

	// Credenciais
	private static final String USER = "root";
	private static final String PW = "";
	
	public static Connection getConnection() throws ModelException {

		// Carga do Driver do Mysql
		try {
			
			Class.forName(DRIVER_NAME);
			
			return DriverManager.getConnection(DB_URL, USER, PW);
			
		} catch (ClassNotFoundException cnfe) {
			throw new ModelException("Driver JDBC não encontrado.", cnfe);
		} catch (SQLException sqle) {
			throw new ModelException(
					"Erro ao estabelecer conexão com o BD.", sqle);
		}
	}
}

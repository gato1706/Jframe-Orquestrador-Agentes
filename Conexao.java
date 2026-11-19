package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public static Connection getConexao() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/projeto_agentes", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco: " + e.getMessage());
        }
    }
}
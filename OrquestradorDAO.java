package DAO;

import config.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.ItemCombo;

public class OrquestradorDAO {

    public void carregarTabelaSkills(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        String sql = "SELECT id, nome, categoria FROM habilidades ORDER BY id";

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("categoria")
                });
            }
        } catch (SQLException e) {
            System.out.println("Erro ao carregar tabela de skills: " + e.getMessage());
        }
    }

    public void atualizarHabilidade(int id, String novoNome, String novaCategoria) {
        String sql = "UPDATE habilidades SET nome = ?, categoria = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoNome);
            stmt.setString(2, novaCategoria);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar habilidade: " + e.getMessage());
        }
    }

    public void excluirHabilidade(int id) {
        String sql = "DELETE FROM habilidades WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir habilidade: " + e.getMessage());
        }
    }

    public void carregarTabelaAgentes(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        String sql = "SELECT a.id AS agente_id, a.nome AS agente_nome, "
                + "h.nome AS skill_nome, h.categoria "
                + "FROM agentes a " //faz com que gentes recém-cadastrados sem nenhuma skill vinculada ainda apareçam na tabela
                + "LEFT JOIN agente_habilidade ah ON a.id = ah.agente_id "
                + "LEFT JOIN habilidades h ON ah.habilidade_id = h.id "
                + "ORDER BY a.id";

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String skill = rs.getString("skill_nome");
                String categoria = rs.getString("categoria");

                modelo.addRow(new Object[]{
                    rs.getInt("agente_id"),
                    rs.getString("agente_nome"),
                    skill != null ? skill : "Sem Skill Vinculada",
                    categoria != null ? categoria : "-"
                });
            }
        } catch (SQLException e) {
            System.out.println("Erro ao carregar tabela de agentes: " + e.getMessage());
        }
    }

    public void atualizarAgente(int id, String novoNome) {
        String sql = "UPDATE agentes SET nome = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoNome);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar agente: " + e.getMessage());
        }
    }

    public void excluirAgente(int id) {
        String sql = "DELETE FROM agentes WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir agente: " + e.getMessage());
        }
    }

    public boolean vincular(int idAgente, int idHabilidade) {
        String sql = "INSERT INTO agente_habilidade (agente_id, habilidade_id) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAgente);
            stmt.setInt(2, idHabilidade);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void cadastrarAgente(String nome) {
        String sql = "INSERT INTO agentes (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar agente: " + e.getMessage());
        }
    }

    public void cadastrarHabilidade(String nome, String categoria) {
        String sql = "INSERT INTO habilidades (nome, categoria) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, categoria);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar habilidade: " + e.getMessage());
        }
    }

    public List<ItemCombo> listarAgentes() {
        List<ItemCombo> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM agentes";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new ItemCombo(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ItemCombo> listarHabilidades() {
        List<ItemCombo> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM habilidades";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new ItemCombo(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean deletarVinciaulo(int idAgente, int idHabilidade) {
        String sql = "DELETE FROM agente_habilidade WHERE agente_id = ? AND habilidade_id = ?";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAgente);
            stmt.setInt(2, idHabilidade);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cadastrarCliente(String nome) {
        String sql = "INSERT INTO clientes (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cadastrarMissao(int idCliente, String descricao) {
        String sql = "INSERT INTO missoes (cliente_id, descricao) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, descricao);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean alocarAgenteNaMissao(int idMissao, int idAgente) {
        String sql = "INSERT INTO equipe_missao (missao_id, agente_id) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMissao);
            stmt.setInt(2, idAgente);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<ItemCombo> listarClientes() {
        List<ItemCombo> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM clientes";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new ItemCombo(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    public List<ItemCombo> listarMissoes() {
        List<ItemCombo> lista = new ArrayList<>();
        // Troquei para mostrar apenas: Missão #ID - Nome do Cliente
        String sql = "SELECT m.id, CONCAT('Missão #', m.id, ' - ', c.nome) AS resumo "
                + "FROM missoes m JOIN clientes c ON m.cliente_id = c.id";

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new ItemCombo(rs.getInt("id"), rs.getString("resumo")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    public void carregarTabelaEquipe(DefaultTableModel modelo, int idMissao) {
        modelo.setRowCount(0);
        // SQL corrigido com o WHERE e as colunas certas:
        String sql = "SELECT a.id, a.nome FROM agentes a "
                + "JOIN equipe_missao em ON a.id = em.agente_id "
                + "WHERE em.missao_id = ?";

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMissao);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nome")
                });
            }
        } catch (SQLException e) {
            System.out.println("Erro ao carregar tabela de equipe: " + e.getMessage());
        }
    }

    public void removerAgenteDaMissao(int idMissao, int idAgente) {
        String sql = "DELETE FROM equipe_missao WHERE missao_id = ? AND agente_id = ?";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMissao);
            stmt.setInt(2, idAgente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public String getDescricaoMissao(int idMissao) {
        String sql = "SELECT descricao FROM missoes WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMissao);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("descricao");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Sem descrição.";
    }
}

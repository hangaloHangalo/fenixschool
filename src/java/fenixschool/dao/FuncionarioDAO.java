/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenixschool.dao;

import fenixschool.modelo.Funcionario;
import fenixschool.modelo.Municipio;
import fenixschool.modelo.Sexo;
import fenixschool.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aisha Lubadika
 */
public class FuncionarioDAO implements  GenericoDAO<Funcionario>{
    private static final String INSERIR ="INSERT INTO funcionario (nome_funcionario, sobrenome_funcionario, data_nascimento, sexo, casa_funcionario, bairro_funcionario, distrito_funcionario, id_municipio, foto_funcionario, url_foto_funcionario, telefone_fixo, telefone_movel, email_funcionario)VALUES(?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String ACTUALIZAR="UPDATE funcionario SET  nome_funcionario= ?, sobrenome_funcionario= ?,data_nascimento =?,sexo= ?, casa_funcionario = ?,bairro_funcionario=?,distrito_funcionario = ?,id_municipio = ?,foto_funcionario = ?,url_foto_funcionario=?, telefone_fixo= ?, telefone_movel= ?, email_funcionario= ? WHERE id_funcionario = ?;";
    private static final String ELIMINAR="DELETE FROM funcionario WHERE id_funcionario=?";
    private static final String BUSCAR_POR_CODIGO="SELECT f.id_funcionario ,f.nome_funcionario ,f.sobrenome_funcionario ,f.data_nascimento , f.sexo ,f.casa_funcionario ,f.bairro_funcionario ,f.distrito_funcionario,m.nome_municipio ,f.foto_funcionario ,f.url_foto_funcionario ,f.telefone_fixo ,f.telefone_movel ,f.email_funcionario FROM funcionario f INNER JOIN municipio m ON m.id_municipio=f.id_municipio WHERE f.id_funcionario = ?";
    
    private static final String LISTAR_TUDO ="SELECT f.id_funcionario,f.nome_funcionario,f.sobrenome_funcionario,f.data_nascimento, f.sexo,casa_funcionario,f.bairro_funcionario,f.distrito_funcionario,m.nome_municipio,f.foto_funcionario,f.url_foto_funcionario,f.telefone_fixo,f.telefone_movel ,f.email_funcionario FROM funcionario f INNER JOIN municipio m ON m.id_municipio=f.id_municipio";
    
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    @Override
    public void save(Funcionario funcionario) {
        if (funcionario == null) {
             System.out.println("O valor passado não pode ser nulo");
        }
        try {
         
            conn = Conexao.getConnection();
            ps = conn.prepareStatement(INSERIR);
            ps.setString(1, funcionario.getNomeFuncionario());
            ps.setString(2, funcionario.getSobrenomeFuncionario());
            ps.setDate(3, new java.sql.Date(funcionario.getDataNascimentoFuncionario().getTime()));
            ps.setString(4, funcionario.getSexo().getAbreviatura());
             ps.setString(5, funcionario.getCasaFuncionario());
            ps.setString(6, funcionario.getBairroFuncionario());
           
            ps.setString(7, funcionario.getDistritoFuncionario());
            ps.setInt(8, funcionario.getMunicipio().getIdMunicipio());
            ps.setBytes(9, funcionario.getFotoFuncionario());
            ps.setString(10, funcionario.getUrlfotoFuncionario());
            
            ps.setString(11,funcionario.getTelefoneFixoFuncionario());
            ps.setString(12, funcionario.getTelefoneMovelFuncionario());
            ps.setString(13, funcionario.getEmailFuncionario());
            ps.executeUpdate();
                    } catch (SQLException ex) {
                          System.out.println("Erro ao inserir dados: " + ex.getMessage());
         }finally {
            Conexao.closeConnection(conn, ps);
        }
    }
 @Override
    public void update(Funcionario funcionario) {
        if (funcionario == null) {
             System.out.println("O valor passado não pode ser nulo");
        }
        try {
            conn = Conexao.getConnection();
            ps = conn.prepareStatement(INSERIR);
         
            ps.setString(1, funcionario.getNomeFuncionario());
            ps.setString(2, funcionario.getSobrenomeFuncionario());
            ps.setDate(3, new java.sql.Date(funcionario.getDataNascimentoFuncionario().getTime()));
            ps.setString(4, funcionario.getSexo().getAbreviatura());
            ps.setString(5, funcionario.getBairroFuncionario());
            ps.setString(6, funcionario.getCasaFuncionario());
            ps.setString(7, funcionario.getDistritoFuncionario());
            ps.setInt(8, funcionario.getMunicipio().getIdMunicipio());
            ps.setString(9, funcionario.getUrlfotoFuncionario());
            ps.setBytes(10, funcionario.getFotoFuncionario());
            ps.setString(11,funcionario.getTelefoneFixoFuncionario());
            ps.setString(12, funcionario.getTelefoneMovelFuncionario());
            ps.setString(13, funcionario.getEmailFuncionario());
               ps.setInt(14, funcionario.getIdFuncionario());
            ps.executeUpdate();
                    } catch (SQLException ex) {
                          System.out.println("Erro ao actualizar  dados: " + ex.getMessage());
         }finally {
            Conexao.closeConnection(conn, ps);
        }
    }
    @Override
    public void delete(Funcionario funcionario) {
        if (funcionario == null) {
            System.out.println("O valor passado nao pode ser nulo");

        }
        try {
            conn = Conexao.getConnection();
            ps = conn.prepareStatement(ELIMINAR);
            ps.setInt(1, funcionario.getIdFuncionario());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Erro ao eliminar dados: " + ex.getMessage());

        } finally {
            Conexao.closeConnection(conn, ps);
        }
    }

    @Override
    public Funcionario findById(Integer id) {
          ResultSet rs= null;
        Funcionario funcionario = new Funcionario();
        try {
            conn = Conexao.getConnection();
            ps = conn.prepareStatement(BUSCAR_POR_CODIGO);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Não foi encontrado nenhum registo com o id: " + id);
            }
            popularComDados(funcionario, rs);
            
        } catch (SQLException ex) {
            System.out.println("Erro ao ler dados: " + ex.getLocalizedMessage());
        
        }finally{
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao desalocar recursos! " +  ex.getLocalizedMessage());
            }
        }
      
        return funcionario;
    }

    @Override
    public List<Funcionario> findAll() {
             ResultSet rs = null;
        List<Funcionario> funcionarios = new ArrayList<>();
        try {
            conn = Conexao.getConnection();
            ps = conn.prepareStatement(LISTAR_TUDO);
            rs = ps.executeQuery();
            while (rs.next()) {                
                Funcionario funcionario = new Funcionario();
                popularComDados(funcionario, rs);
                funcionarios.add(funcionario);
            }
                 
        } catch (SQLException ex) {
            System.err.println("Erro ao ler dados: " + ex.getLocalizedMessage());
        }finally{
            Conexao.closeConnection(conn, ps);
        }
        
        return funcionarios;
    }

    @Override
    public void popularComDados(Funcionario funcionario, ResultSet rs) {
        try {
            funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
           
            funcionario.setNomeFuncionario(rs.getString("nome_funcionario"));
            funcionario.setSobrenomeFuncionario(rs.getString("sobrenome_funcionario"));
            funcionario.setDataNascimentoFuncionario(rs.getDate("data_nascimento"));
 
            funcionario.setSexo(Sexo.getAbreviatura(rs.getString("sexo")));

            funcionario.setCasaFuncionario(rs.getString("casa_funcionario"));
            funcionario.setBairroFuncionario(rs.getString("bairro_funcionario"));
            funcionario.setDistritoFuncionario(rs.getString("distrito_funcionario"));

            Municipio municipio = new Municipio();
            municipio.setNomeMunicipio("nome_municipio");
            funcionario.setMunicipio(municipio);
            funcionario.setUrlfotoFuncionario(rs.getString("url_foto_funcionario"));

            funcionario.setFotoFuncionario(rs.getBytes("foto_funcionario"));
            funcionario.setTelefoneFixoFuncionario(rs.getString("telefone_fixo"));
           funcionario.setTelefoneMovelFuncionario(rs.getString("telefone_movel"));
            funcionario.setEmailFuncionario(rs.getString("email_funcionario"));

            
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

   
    
}

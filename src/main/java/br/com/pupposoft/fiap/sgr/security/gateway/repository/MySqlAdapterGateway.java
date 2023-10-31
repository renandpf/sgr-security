package br.com.pupposoft.fiap.sgr.security.gateway.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import br.com.pupposoft.fiap.sgr.security.config.DatabasePool;
import br.com.pupposoft.fiap.sgr.security.domain.Perfil;
import br.com.pupposoft.fiap.sgr.security.domain.Usuario;
import br.com.pupposoft.fiap.sgr.security.gateway.DatabaseRepositoryGateway;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySqlAdapterGateway implements DatabaseRepositoryGateway {

	@Override
	public Optional<Usuario> findByCpf(String cpf) {
		log.trace("Start");

		try (Connection conn = DatabasePool.getConnection()) {

			
			createTableIfNotExist(conn);
			
			createUser(conn);
			
			final String query = "SELECT * FROM Usuario where cpf = ?;";
			
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, cpf);
			
			ResultSet resultSet = pstmt.executeQuery();
			
			Usuario usuario = null;
			
			if(resultSet.next()) {
				usuario = Usuario.builder()
							.id(resultSet.getLong("id"))
							.cpf(resultSet.getString("cpf"))
							.senha(resultSet.getString("senha"))
							.perfil(resultSet.getLong("perfil") == 0 ? Perfil.CLIENTE : Perfil.ADMINISTRADOR)
						.build();
			}

			resultSet.close();
			pstmt.close();
			
			Optional<Usuario> usuarioOp = Optional.ofNullable(usuario);

			log.trace("End={}", usuarioOp);
			return usuarioOp;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	private void createUser(Connection conn) throws SQLException {
		final String queryGetUser = "SELECT * FROM Usuario where cpf = 555;";
		PreparedStatement pstmt = conn.prepareStatement(queryGetUser);
		
		ResultSet resultSet = pstmt.executeQuery();
		
		if(resultSet.next()) {
			return;
		}

		String createUser = "INSERT INTO `sgrDbSecurity`.`Usuario` (`cpf`, `senha`, `perfil`) VALUES ('555', 'senha', '0')";
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(createUser);
	}
	
	private void createTableIfNotExist(Connection conn) throws SQLException {
		final String createQuery = "CREATE TABLE IF NOT EXISTS `sgrDbSecurity`.`Usuario` (\n"
				+ "  `id` int NOT NULL AUTO_INCREMENT,\n"
				+ "  `cpf` varchar(11) NOT NULL,\n"
				+ "  `senha` varchar(10) NOT NULL,\n"
				+ "  `perfil` bigint NOT NULL,\n"
				+ "  PRIMARY KEY (`id`)\n"
				+ ");";
		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(createQuery);
	}
}

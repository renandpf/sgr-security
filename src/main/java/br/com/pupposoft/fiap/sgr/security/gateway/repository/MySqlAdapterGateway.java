package br.com.pupposoft.fiap.sgr.security.gateway.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}

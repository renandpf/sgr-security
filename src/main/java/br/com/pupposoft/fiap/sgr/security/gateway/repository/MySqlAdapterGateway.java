package br.com.pupposoft.fiap.sgr.security.gateway.repository;

import java.sql.Connection;
import java.sql.Statement;

import br.com.pupposoft.fiap.sgr.security.config.DatabasePool;
import br.com.pupposoft.fiap.sgr.security.domain.Usuario;
import br.com.pupposoft.fiap.sgr.security.gateway.DatabaseRepositoryGateway;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySqlAdapterGateway implements DatabaseRepositoryGateway {

	@Override
	public Usuario findByCpf(String cpf) {
		log.trace("Start");

		//TODO: implementar
		
		try (Connection conn = DatabasePool.getConnection()) {

			Statement statement = conn.createStatement();

			String sql = "";
			statement.executeUpdate(sql);
			
			System.out.println("End");
			log.trace("End");
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
}

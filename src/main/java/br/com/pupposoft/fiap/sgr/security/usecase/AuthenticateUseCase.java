package br.com.pupposoft.fiap.sgr.security.usecase;

import br.com.pupposoft.fiap.sgr.security.gateway.DatabaseRepositoryGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticateUseCase {
	
	private DatabaseRepositoryGateway databaseRepositoryGateway;

	public String autenticar(String usuario, String senha) {
		return null;
	}
	
}

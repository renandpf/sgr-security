package br.com.pupposoft.fiap.sgr.security.gateway.repository;

import br.com.pupposoft.fiap.sgr.security.domain.Usuario;

public interface DatabaseRepositoryGateway {
	Usuario autenticar(String usuario, String senha);
}

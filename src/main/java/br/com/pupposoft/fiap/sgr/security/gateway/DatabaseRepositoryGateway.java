package br.com.pupposoft.fiap.sgr.security.gateway;

import java.util.Optional;

import br.com.pupposoft.fiap.sgr.security.domain.Usuario;

public interface DatabaseRepositoryGateway {
	Optional<Usuario> findByCpf(String cpf);
}

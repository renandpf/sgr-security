package br.com.pupposoft.fiap.sgr.security.gateway;

import br.com.pupposoft.fiap.sgr.security.domain.Usuario;

public interface DatabaseRepositoryGateway {

	Usuario findByCpf(String cpf);

}

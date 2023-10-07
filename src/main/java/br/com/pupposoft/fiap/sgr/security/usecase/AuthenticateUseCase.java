package br.com.pupposoft.fiap.sgr.security.usecase;

import java.util.HashMap;
import java.util.Map;

import br.com.pupposoft.fiap.sgr.security.domain.Usuario;
import br.com.pupposoft.fiap.sgr.security.exception.FalhaAutenticacaoException;
import br.com.pupposoft.fiap.sgr.security.gateway.DatabaseRepositoryGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.TokenGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AuthenticateUseCase {
	
	private DatabaseRepositoryGateway databaseRepositoryGateway;
	
	private TokenGateway tokenGateway;
	

	public String autenticar(String cpf, String senha) {
		
		Usuario usuario = databaseRepositoryGateway.findByCpf(cpf);
		
		if(usuario.getSenha().equals(senha)) {//TODO: trabalhar com senhas criptografadas no banco
			Map<String, Object> infos = new HashMap<>();
			
			infos.put("usuarioId", usuario.getId());
			infos.put("usuarioPerfil", usuario.getPerfil());
			
			return tokenGateway.generate(infos);
		}
		
		log.warn("Usuário ou senha invpalido!");
		throw new FalhaAutenticacaoException();
		
	}
	
}

package br.com.pupposoft.fiap.sgr.security.usecase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
		
		Optional<Usuario> usuarioOp = databaseRepositoryGateway.findByCpf(cpf);
		
		if(autentica(senha, usuarioOp)) {
			return getToken(usuarioOp);
		}
		
		log.warn("Usuário ou senha invpalido!");
		throw new FalhaAutenticacaoException();
		
	}

	//TODO: trabalhar com senhas criptografadas no banco
	private boolean autentica(String senha, Optional<Usuario> usuarioOp) {
		return usuarioOp.isPresent() && usuarioOp.get().getSenha().equals(senha);
	}


	private String getToken(Optional<Usuario> usuarioOp) {
		Map<String, Object> infos = new HashMap<>();
		
		Usuario usuario = usuarioOp.get();
		
		infos.put("usuarioId", usuario.getId());
		infos.put("usuarioPerfil", usuario.getPerfil());
		
		return tokenGateway.generate(infos);
	}
	
}

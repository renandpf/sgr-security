package br.com.pupposoft.fiap.sgr.security.gateway;

import br.com.pupposoft.fiap.sgr.security.gateway.dto.TokenDto;

public interface TokenGateway {

	public String generate(TokenDto dto);
	
}

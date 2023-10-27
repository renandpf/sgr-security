package br.com.pupposoft.fiap.sgr.security.gateway;

import java.util.Map;

public interface TokenGateway {

	public String generate(Map<String, Object> infos);

	public Map<String, Object> getData(String token);
	
}

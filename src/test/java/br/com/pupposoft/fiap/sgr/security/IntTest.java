package br.com.pupposoft.fiap.sgr.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.com.pupposoft.fiap.sgr.security.exception.FalhaAutenticacaoException;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.LambdaEntrypoint;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.ResponseJson;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;


@Disabled("Trata-se de um teste de integração. Deve ser usado pelo dev para testes (necessita de um database rodando)")
@ExtendWith(SystemStubsExtension.class)
class IntTest {
	
	@SystemStub
	private static EnvironmentVariables environmentVariables;

	@BeforeAll
	private static void config() {
		environmentVariables.set("DATABASE_URL", "jdbc:mysql://127.0.0.1/sgrDbSecurity");
		environmentVariables.set("DATABASE_USERNAME", "root");
		environmentVariables.set("DATABASE_PASSWORD", "senha");
		
		environmentVariables.set("TOKEN_SECRET_KEY", "XQG0TQqpEUkukgwK8TMVXbxeVU7Y5TWW2AZzkSAzYLFqq1BeZxKkAUqmrA3zeXvC");
		environmentVariables.set("TOKEN_EXPIRATION_TIME_IN_SECOND", "3600");
	}
	
	@Test
	void shouldAutenticadoComSucesso(){
		LambdaEntrypoint entrypoint = new LambdaEntrypoint();
		
		final String anyCpf = "666";
		final String anySenha = "senha";
		
		Object requestData = "{username="+ anyCpf +", password="+ anySenha +"}";
		
		ResponseJson handleResponse = entrypoint.handleRequest(requestData, null);
		
		assertNotNull(handleResponse);
		assertNotNull(handleResponse.getBody());
		System.out.println(handleResponse);
	}
	
	@Test
	void shouldNaoAutenticado(){
		LambdaEntrypoint entrypoint = new LambdaEntrypoint();
		
		final String anyCpf = "555";
		final String anySenha = "senhaaaaa";
		
		Object requestData = "{username="+ anyCpf +", password="+ anySenha +"}";
		
		assertThrows(FalhaAutenticacaoException.class, () -> {
			entrypoint.handleRequest(requestData, null);
		});
	}
	
}

package br.com.pupposoft.fiap.sgr.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.com.pupposoft.fiap.sgr.security.exception.FalhaAutenticacaoException;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.GenerateTokenEntrypoint;
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
	void shouldAutenticadoComSucessoFromApigateway(){
		GenerateTokenEntrypoint entrypoint = new GenerateTokenEntrypoint();
		
		final String anyCpf = "666";
		final String anySenha = "senha";
		
		Object requestData = "request={resource=/sgr/login, \n"
				+ "  path=/sgr/login, \n"
				+ "  httpMethod=POST, \n"
				+ "  headers=null, \n"
				+ "  multiValueHeaders=null, \n"
				+ "  queryStringParameters=null, \n"
				+ "  multiValueQueryStringParameters=null, \n"
				+ "  pathParameters=null, \n"
				+ "  stageVariables=null, \n"
				+ "  requestContext={resourceId=q206mq, resourcePath=/sgr/login, httpMethod=POST, extendedRequestId=Nb5r_HFJPHcFqng=, requestTime=27/Oct/2023:00:51:11 +0000, path=/sgr/login, accountId=057028502056, protocol=HTTP/1.1, stage=test-invoke-stage, domainPrefix=testPrefix, requestTimeEpoch=1698367871675, requestId=6e8a7754-10d9-4335-b069-2e47cabaf648, identity={cognitoIdentityPoolId=null, cognitoIdentityId=null, apiKey=test-invoke-api-key, principalOrgId=null, cognitoAuthenticationType=null,     userArn=arn:aws:iam::057028502056:user/renan.admin, apiKeyId=test-invoke-api-key-id, userAgent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36, accountId=057028502056, caller=AIDAIPMNJPQVHVRAGPXG2, sourceIp=test-invoke-source-ip, accessKey=ASIAQ2RZJ4IUI4MVUYNX, cognitoAuthenticationProvider=null, user=AIDAIPMNJPQVHVRAGPXG2}, domainName=testPrefix.testDomainName, apiId=38x0x1l1kg}, \n"
				+ "	body={\"username\":\""+ anyCpf +"\",\"password\":\""+ anySenha +"\"}, \n"
				+ "	isBase64Encoded=false\n"
				+ "}";
		
		ResponseJson handleResponse = entrypoint.handleRequest(requestData, null);
		
		assertNotNull(handleResponse);
		assertNotNull(handleResponse.getBody());
		System.out.println(handleResponse);
	}
	
	@Test
	void shouldNaoAutenticado(){
		GenerateTokenEntrypoint entrypoint = new GenerateTokenEntrypoint();
		
		final String anyCpf = "555";
		final String anySenha = "senhaaaaa";
		
		Object requestData = "{username="+ anyCpf +", password="+ anySenha +"}";
		
		assertThrows(FalhaAutenticacaoException.class, () -> {
			entrypoint.handleRequest(requestData, null);
		});
	}
	
}

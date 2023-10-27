package br.com.pupposoft.fiap.sgr.security.gateway.entrypoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.security.gateway.DatabaseRepositoryGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.TokenGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.LoginRequestJson;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.ResponseJson;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.TokenJson;
import br.com.pupposoft.fiap.sgr.security.gateway.repository.MySqlAdapterGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.token.JWTAdapterGateway;
import br.com.pupposoft.fiap.sgr.security.usecase.AutenticarUseCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateTokenEntrypoint implements RequestHandler<Object, ResponseJson> {
	
	private AutenticarUseCase autenticarUseCase;
	
	private ObjectMapper objectMapper;
	
	public GenerateTokenEntrypoint(){
		
		DatabaseRepositoryGateway databaseRepositoryGateway = new MySqlAdapterGateway();
		TokenGateway tokenGateway = new JWTAdapterGateway();
		
		autenticarUseCase = new AutenticarUseCase(databaseRepositoryGateway, tokenGateway);
		objectMapper = new ObjectMapper();
	}
	
	@Override
	public ResponseJson handleRequest(Object request, Context context) {
		try {
			
			/*
			 * request={
			 * resource=/sgr/login, path=/sgr/login, 
			 * httpMethod=POST, 
			 * headers=null, 
			 * multiValueHeaders=null, 
			 * queryStringParameters=null, 
			 * multiValueQueryStringParameters=null, 
			 * pathParameters=null, 
			 * stageVariables=null, 
			 * requestContext={resourceId=q206mq, resourcePath=/sgr/login, httpMethod=POST, extendedRequestId=Nb5r_HFJPHcFqng=, requestTime=27/Oct/2023:00:51:11 +0000, path=/sgr/login, accountId=057028502056, protocol=HTTP/1.1, stage=test-invoke-stage, domainPrefix=testPrefix, requestTimeEpoch=1698367871675, requestId=6e8a7754-10d9-4335-b069-2e47cabaf648, identity={cognitoIdentityPoolId=null, cognitoIdentityId=null, apiKey=test-invoke-api-key, principalOrgId=null, cognitoAuthenticationType=null, userArn=arn:aws:iam::057028502056:user/renan.admin, apiKeyId=test-invoke-api-key-id, userAgent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36, accountId=057028502056, caller=AIDAIPMNJPQVHVRAGPXG2, sourceIp=test-invoke-source-ip, accessKey=ASIAQ2RZJ4IUI4MVUYNX, cognitoAuthenticationProvider=null, user=AIDAIPMNJPQVHVRAGPXG2}, domainName=testPrefix.testDomainName, apiId=38x0x1l1kg}, 
			 * body={"username":"555","password":"senha"}, 
			 * isBase64Encoded=false}
			 * 
			 */
			
			System.out.println("request=" + request);
			
			LoginRequestJson userPassMap = getLoginRequestBody(request);

			final String token = autenticarUseCase.autenticar(userPassMap.getUsername(), userPassMap.getPassword());
			
			ResponseJson responseJson = getTokenResponse(token);
			
			System.out.println("responseJson=" + responseJson);
			return responseJson;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return new ResponseJson(false, 500, new HashMap<>(), e.getMessage()); 
			//throw new RuntimeException(e);
		}
	}

	private ResponseJson getTokenResponse(final String token) throws JsonProcessingException {
		final TokenJson response = new TokenJson(token);
		
		String responseJsonStr = objectMapper.writeValueAsString(response);
		
		return new ResponseJson(false, 200, new HashMap<>(), responseJsonStr);
	}

	private LoginRequestJson getLoginRequestBody(Object request) {
		List<String> itens = Arrays.asList(request.toString().replace("{", "").replace("}", "").split(", "));
		Map<String, String> userPassMap = new HashMap<String, String>();
		itens.forEach(i -> {
			String[] data = i.split("=");
			userPassMap.put(data[0], data[1]);
		});
		return new LoginRequestJson(userPassMap.get("username"), userPassMap.get("password"));
	}
}

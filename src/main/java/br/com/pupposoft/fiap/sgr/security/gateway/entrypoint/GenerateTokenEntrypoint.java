package br.com.pupposoft.fiap.sgr.security.gateway.entrypoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

	private LoginRequestJson getLoginRequestBody(Object request) throws Exception {
		return getRequestBodyFromApiGateway(request);
	}

	private LoginRequestJson getRequestBodyFromApiGateway(Object request)
			throws JsonProcessingException, JsonMappingException {
		final String requestAsString = request.toString();
		
		List<String> itens = Arrays.asList(requestAsString.substring(0, requestAsString.length()-1).replace("request={", "").split(", "));
		StringBuilder userPassSB = new StringBuilder();
		itens.stream().filter(i -> i.contains("body")).forEach(i -> {
			userPassSB.append(i.replace("body=", ""));
		});
		
		String bodyJsonAsString = userPassSB.toString();
		
		return objectMapper.readValue(bodyJsonAsString, LoginRequestJson.class);
	}
}

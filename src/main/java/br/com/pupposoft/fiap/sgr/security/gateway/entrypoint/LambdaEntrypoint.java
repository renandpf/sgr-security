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
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.RequestJson;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.ResponseJson;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.TokenJson;
import br.com.pupposoft.fiap.sgr.security.gateway.repository.MySqlAdapterGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.token.JWTAdapterGateway;
import br.com.pupposoft.fiap.sgr.security.usecase.AutenticarUseCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LambdaEntrypoint implements RequestHandler<Object, ResponseJson> {
	
	private AutenticarUseCase autenticarUseCase;
	
	private ObjectMapper objectMapper;
	
	public LambdaEntrypoint(){
		
		DatabaseRepositoryGateway databaseRepositoryGateway = new MySqlAdapterGateway();
		TokenGateway tokenGateway = new JWTAdapterGateway();
		
		autenticarUseCase = new AutenticarUseCase(databaseRepositoryGateway, tokenGateway);
		objectMapper = new ObjectMapper();
	}
	
	@Override
	public ResponseJson handleRequest(Object request, Context context) {
		try {
			System.out.println("request=" + request);
			
			RequestJson userPassMap = getLoginRequestBody(request);

			final String token = autenticarUseCase.autenticar(userPassMap.getUsername(), userPassMap.getPassword());
			
			ResponseJson responseJson = getTokenResponse(token);
			
			System.out.println("responseJson=" + responseJson);
			return responseJson;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private ResponseJson getTokenResponse(final String token) throws JsonProcessingException {
		final TokenJson response = new TokenJson(token);
		
		String responseJsonStr = objectMapper.writeValueAsString(response);
		
		ResponseJson responseJson = new ResponseJson(false, 200, new HashMap<>(), responseJsonStr);
		return responseJson;
	}

	private RequestJson getLoginRequestBody(Object request) {
		List<String> itens = Arrays.asList(request.toString().replace("{", "").replace("}", "").split(", "));
		Map<String, String> userPassMap = new HashMap<String, String>();
		itens.forEach(i -> {
			String[] data = i.split("=");
			userPassMap.put(data[0], data[1]);
		});
		return new RequestJson(userPassMap.get("username"), userPassMap.get("password"));
	}
}

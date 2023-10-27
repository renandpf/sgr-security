package br.com.pupposoft.fiap.sgr.security.gateway.entrypoint;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.security.gateway.TokenGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.ResponseJson;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.TokenRequestJson;
import br.com.pupposoft.fiap.sgr.security.gateway.token.JWTAdapterGateway;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidateTokenEntrypoint implements RequestHandler<Object, ResponseJson> {
	
	private TokenGateway tokenGateway;
	
	private ObjectMapper objectMapper;
	
	public ValidateTokenEntrypoint(){
		tokenGateway = new JWTAdapterGateway();
	}
	
	@Override
	public ResponseJson handleRequest(Object request, Context context) {
		try {
			System.out.println("request=" + request);
			
			TokenRequestJson requestJson = getTokenRequestBody(request);

			Map<String, Object> data = tokenGateway.getData(requestJson.getToken());
			
			ResponseJson responseJson = getTokenResponse(data);
			
			System.out.println("responseJson=" + responseJson);
			return responseJson;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private ResponseJson getTokenResponse(final Map<String, Object> data) throws JsonProcessingException {
		String responseJsonStr = objectMapper.writeValueAsString(data);
		return new ResponseJson(false, 200, new HashMap<>(), responseJsonStr);
	}

	private TokenRequestJson getTokenRequestBody(Object request) {
		return new TokenRequestJson("TESTE");//FIXME

	}
}

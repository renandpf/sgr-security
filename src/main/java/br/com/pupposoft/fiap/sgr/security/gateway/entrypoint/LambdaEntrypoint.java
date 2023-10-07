package br.com.pupposoft.fiap.sgr.security.gateway.entrypoint;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.pupposoft.fiap.sgr.security.gateway.DatabaseRepositoryGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.TokenGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.RequestJson;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.ResponseJson;
import br.com.pupposoft.fiap.sgr.security.gateway.repository.MySqlAdapterGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.token.JWTAdapterGateway;
import br.com.pupposoft.fiap.sgr.security.usecase.AutenticarUseCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LambdaEntrypoint implements RequestHandler<RequestJson, ResponseJson> {
	
	private AutenticarUseCase autenticarUseCase;
	
	public LambdaEntrypoint(){
		
		DatabaseRepositoryGateway databaseRepositoryGateway = new MySqlAdapterGateway();
		TokenGateway tokenGateway = new JWTAdapterGateway();
		
		autenticarUseCase = new AutenticarUseCase(databaseRepositoryGateway, tokenGateway);
	}
	
	@Override
	public ResponseJson handleRequest(RequestJson request, Context context) {
		try {
			log.trace("Start request={}, context={}", request, context);
			System.out.println("request=" + request);

			final String token = autenticarUseCase.autenticar(request.getUsername(), request.getPassword());
			
			final ResponseJson response = new ResponseJson(token);
			
			log.trace("End");
			return response;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}
}

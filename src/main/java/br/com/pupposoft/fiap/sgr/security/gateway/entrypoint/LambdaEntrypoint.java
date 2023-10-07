package br.com.pupposoft.fiap.sgr.security.gateway.entrypoint;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.RequestJson;
import br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json.ResponseJson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LambdaEntrypoint implements RequestHandler<RequestJson, ResponseJson> {
	
	
	@Override
	public ResponseJson handleRequest(RequestJson request, Context context) {
		try {
			log.trace("Start request={}, context={}", request, context);
			System.out.println("request=" + request);
			
			
			ResponseJson response = new ResponseJson("any token");
			
			log.trace("End");
			return response;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}
}

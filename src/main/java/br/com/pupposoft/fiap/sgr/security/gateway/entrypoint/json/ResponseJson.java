package br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResponseJson {
/* {
	"isBase64Encoded": true|false,
	"statusCode": httpStatusCode,
	"headers": { "headerName": "headerValue", ... },
	"body": "..."
	}
 */
	
	private boolean base64Encoded;
	private int statusCode;
	private Map<String, String> headers;
	private String body;
}
